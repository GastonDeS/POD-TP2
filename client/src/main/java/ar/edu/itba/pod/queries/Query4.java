package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.MaxMonthlyMeanPerYearCollator;
import ar.edu.itba.pod.combiners.MaxMonthlyMeanPerYearCombinerFactory;
import ar.edu.itba.pod.combiners.MonthlyMeanPerYearCombinerFactory;
import ar.edu.itba.pod.mappers.MaxMonthlyMeanPerYearMapper;
import ar.edu.itba.pod.mappers.YearMapper;
import ar.edu.itba.pod.models.MonthlyMeanKey;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.reducers.MaxMonthlyMeanPerYearReducerFactory;
import ar.edu.itba.pod.reducers.MonthlyMeanPerYearReducerFactory;
import ar.edu.itba.pod.utils.Arguments;
import ar.edu.itba.pod.utils.TimeLog;
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
            final Arguments arguments,
            final TimeLog timeLog
            ) {
        super(hazelcastInstance, arguments, timeLog);
        this.activeSensors = filterActiveSensors(sensors);
        this.year = arguments.getYear();
        this.n = arguments.getN();
    }

    @Override
    protected ICompletableFuture<List<Map.Entry<String, Double>>> submit() throws ExecutionException, InterruptedException {
        final Job<String, Reading> job = getJobFromReadingsList("q4_first");
        ICompletableFuture<Map<MonthlyMeanKey, Double>> future = job
                .mapper(new YearMapper(activeSensors, year))
                .combiner(new MonthlyMeanPerYearCombinerFactory())
                .reducer(new MonthlyMeanPerYearReducerFactory())
                .submit();

        Map<MonthlyMeanKey, Double> monthlyMeanPerYear = future.get();

        final IMap<MonthlyMeanKey, Double> meanIMap = hazelcastInstance.getMap("means");
        meanIMap.clear();
        meanIMap.putAll(monthlyMeanPerYear);

        Job<MonthlyMeanKey, Double> finalJob = this.getJobFromMeansMap("q4_second");

        return finalJob
                .mapper(new MaxMonthlyMeanPerYearMapper())
                .combiner(new MaxMonthlyMeanPerYearCombinerFactory())
                .reducer(new MaxMonthlyMeanPerYearReducerFactory())
                .submit(new MaxMonthlyMeanPerYearCollator(n));
    }

    @Override
    protected String getHeaders() {
        return "Sensor;Month;Max_Monthly_Avg\n";
    }

    @Override
    protected String formatData(Map.Entry<String, Double> entry) {
        return entry.getKey() + ";" + entry.getValue() +"\n";
    }

    private Job<MonthlyMeanKey, Double> getJobFromMeansMap(String queryName) {
        final JobTracker jobTracker = hazelcastInstance.getJobTracker("g10_" + queryName);

        final IMap<MonthlyMeanKey, Double> meanIMap = hazelcastInstance.getMap("means");
        final KeyValueSource<MonthlyMeanKey, Double> source = KeyValueSource.fromMap(meanIMap);

        return jobTracker.newJob(source);
    }
}
