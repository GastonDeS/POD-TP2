package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.MaxMonthlyMeanPerYearCollator;
import ar.edu.itba.pod.combiners.MaxMonthlyMeanPerYearCombinerFactory;
import ar.edu.itba.pod.combiners.MonthlyMeanPerYearCombinerFactory;
import ar.edu.itba.pod.mappers.MaxMonthlyMeanPerYearMapper;
import ar.edu.itba.pod.mappers.MonthlyMeanPerYearMapper;
import ar.edu.itba.pod.models.MonthlyMeanKey;
import ar.edu.itba.pod.models.MonthlyMeanValue;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.reducers.MaxMonthlyMeanPerYearReducerFactory;
import ar.edu.itba.pod.reducers.MonthlyMeanPerYearReducerFactory;
import ar.edu.itba.pod.utils.Arguments;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query4 extends GenericQuery<String, Double> {
    private final List<Sensor> activeSensors;
    private final int year;
    private final int n;

    public Query4(
            List<Sensor> sensors,
            final HazelcastInstance hazelcastInstance,
            final Arguments arguments
    ) {
        super(hazelcastInstance, arguments);
        this.activeSensors = filterActiveSensors(sensors);
        this.year = arguments.getYear();
        this.n = arguments.getN();
    }

    @Override
    protected ICompletableFuture<List<Map.Entry<String, Double>>> submit() throws ExecutionException, InterruptedException {
        final Job<String, Reading> job = getJobFromReadingsList("q4");
        ICompletableFuture<Map<MonthlyMeanKey, Double>> future = job
                .mapper(new MonthlyMeanPerYearMapper(activeSensors, year))
                .combiner(new MonthlyMeanPerYearCombinerFactory())
                .reducer(new MonthlyMeanPerYearReducerFactory())
                .submit();

        Map<MonthlyMeanKey, Double> monthlyMeanPerYear = future.get();

        final IMap<String, MonthlyMeanValue> meanIMap = hazelcastInstance.getMap("means");

        for (Map.Entry<MonthlyMeanKey, Double> entry : monthlyMeanPerYear.entrySet()) {
            meanIMap.put(entry.getKey().getSensor_ID(), new MonthlyMeanValue(entry.getKey().getMonth(), entry.getValue()));
        }

        Job<String, MonthlyMeanValue> finalJob = this.getJobFromMeansMap("q4");

        ICompletableFuture<List<Map.Entry<String, Double>>> finalFuture = finalJob
                .mapper(new MaxMonthlyMeanPerYearMapper())
                .combiner(new MaxMonthlyMeanPerYearCombinerFactory())
                .reducer(new MaxMonthlyMeanPerYearReducerFactory())
                .submit(new MaxMonthlyMeanPerYearCollator());

        meanIMap.destroy();
        return finalFuture;
    }

    @Override
    protected String getHeaders() {
        return "Sensor;Month;Max_Monthly_Avg\n";
    }

    @Override
    protected String formatData(Map.Entry<String, Double> entry) {
        return entry.getKey() + ";" + entry.getValue() +"\n";
    }

    private Job<String, MonthlyMeanValue> getJobFromMeansMap(String queryName) {
        final JobTracker jobTracker = hazelcastInstance.getJobTracker("g10_" + queryName);

        final IMap<String, MonthlyMeanValue> meanIMap = hazelcastInstance.getMap("means");
        final KeyValueSource<String, MonthlyMeanValue> source = KeyValueSource.fromMap(meanIMap);

        return jobTracker.newJob(source);
    }
}
