package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.GroupByMillionsCollator;
import ar.edu.itba.pod.mappers.SensorActiveMapper;
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

public class Query5 extends GenericQuery<String, Long> {
    private final List<Sensor> activeSensors;

    public Query5(
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
        final Job<String, Reading> job = getJobFromReadingsList("q5");
        return job
                .mapper(new SensorActiveMapper(activeSensors))
                .reducer(new SumReducerFactory<>())
                .submit(new GroupByMillionsCollator());
    }

    @Override
    protected String getHeaders() {
        return "Group;Sensor A;Sensor B\n";
    }

    @Override
    protected String formatData(Map.Entry<String, Long> entry) {
        return entry.getValue() + ";" + entry.getKey() +"\n";
    }
}
