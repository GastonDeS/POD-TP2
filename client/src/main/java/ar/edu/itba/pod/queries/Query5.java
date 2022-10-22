package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.GroupByMillionsCollator;
import ar.edu.itba.pod.mappers.ReadingActiveMapper;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import ar.edu.itba.pod.reducers.SumReducerFactory;
import ar.edu.itba.pod.utils.Arguments;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Query5 extends GenericQuery<String, Long> {
    private final List<Sensor> activeSensors;

    public Query5(
            List<Sensor> sensors,
            final HazelcastInstance hazelcastInstance,
            final Arguments arguments
    ) {
        super(hazelcastInstance, arguments);
        activeSensors = filterActiveSensors(sensors);
    }

    @Override
    protected ICompletableFuture<List<Map.Entry<String, Long>>> submit() {
        final Job<String, Reading> job = getJobFromList("q5");
        return job
                .mapper(new ReadingActiveMapper<>(activeSensors))
                .reducer(new SumReducerFactory<>())
                .submit(new GroupByMillionsCollator());
    }

    private List<Sensor> filterActiveSensors(List<Sensor> sensors) {
        return sensors.stream().filter(s -> s.status == SensorStatus.ACTIVE)
                .collect(Collectors.toList());
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
