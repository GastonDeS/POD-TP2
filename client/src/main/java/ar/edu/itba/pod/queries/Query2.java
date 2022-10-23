package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.GroupByMillionsCollator;
import ar.edu.itba.pod.collators.YearlyCountCollator;
import ar.edu.itba.pod.mappers.SensorDescAndHourlyMapper;
import ar.edu.itba.pod.mappers.TypeOfDayMapper;
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

public class Query2 extends GenericQuery<String, Long>{

    public Query2(
            final HazelcastInstance hazelcastInstance,
            final Arguments arguments
    ) {
        super(hazelcastInstance, arguments);
    }

    @Override
    protected ICompletableFuture<List<Map.Entry<String, Long>>> submit() {
        final Job<String, Reading> job = getJobFromList("q2");
        return job
                .mapper(new TypeOfDayMapper<>())
                .reducer(new SumReducerFactory<>())
                .submit(new YearlyCountCollator());
    }

    private List<Sensor> filterActiveSensors(List<Sensor> sensors) {
        return sensors.stream().filter(s -> s.status == SensorStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    protected String getHeaders() {
        return "Year;Weekdays_Count;Weekends_Count;Total_Count\n";
    }

    @Override
    protected String formatData(Map.Entry<String, Long> entry) {
        return entry.getKey() + ";" + entry.getValue() + "\n";
    }
}
