package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.OrderByDescendingCollator;
import ar.edu.itba.pod.mappers.SensorDescAndHourlyMapper;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.reducers.SumReducerFactory;
import ar.edu.itba.pod.utils.Arguments;
import ar.edu.itba.pod.utils.TimeLog;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;

import java.util.List;
import java.util.Map;

public class Query1 extends GenericQuery<String, Long> {
    private final List<Sensor> activeSensors;

    public Query1(
            List<Sensor> sensors,
            final HazelcastInstance hazelcastInstance,
            final Arguments arguments,
            final TimeLog timeLog
        ) {
        super(hazelcastInstance, arguments, timeLog);
        activeSensors = filterActiveSensors(sensors);
    }

    @Override
    protected ICompletableFuture<List<Map.Entry<String, Long>>> submit() {
        final Job<String, Reading> job = getJobFromReadingsList("q1");
        return job
                .mapper(new SensorDescAndHourlyMapper<>(activeSensors))
                .reducer(new SumReducerFactory<>())
                .submit(new OrderByDescendingCollator());
    }

    @Override
    protected String getHeaders() {
        return "Sensor; Total_Count\n";
    }

    @Override
    protected String formatData(Map.Entry<String, Long> entry) {
        return entry.getValue() + ";" + entry.getKey() + "\n";
    }
}
