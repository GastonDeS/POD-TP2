package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.AverageCollator;
import ar.edu.itba.pod.collators.GroupByMillionsCollator;
import ar.edu.itba.pod.mappers.CountByMonthMapper;
import ar.edu.itba.pod.mappers.SensorDescAndHourlyMapper;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import ar.edu.itba.pod.reducers.SumReducerFactory;
import ar.edu.itba.pod.utils.Arguments;
import ar.edu.itba.pod.utils.TimeLog;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Query4 extends GenericQuery<String, Double> {
    private final List<Sensor> activeSensors;
    private final int n;
    private final int year;

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
    protected ICompletableFuture<List<Map.Entry<String, Double>>> submit() {
        final Job<String, Reading> job = getJobFromReadingsList("q4");
        return job
                .mapper(new CountByMonthMapper<>(activeSensors, ""+year))
                .reducer(new SumReducerFactory<>())
                .submit(new AverageCollator(n));
    }

    @Override
    protected String getHeaders() {
        return "Sensor;Month;Max_Monthly_Avg\n";
    }

    @Override
    protected String formatData(Map.Entry<String, Double> entry) {
        return entry.getKey() + ";" + entry.getValue() + "\n";
    }
}
