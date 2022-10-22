package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.GroupByMillionsCollator;
import ar.edu.itba.pod.mappers.ReadingActiveMapper;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import ar.edu.itba.pod.reducers.SumReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query5 extends QueryImpl {

    List<String> activeSensors;
    private Map<String, Long> result;
    public Query5(List<Sensor> sensors, final HazelcastInstance hazelcastInstance) {
        super(hazelcastInstance);
        activeSensors = filterActiveSensors(sensors);
        System.out.println(sensors.size());
        System.out.println(activeSensors.size());
    }

    @Override
    public void run() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getJobTracker("q5");

        final IList<Reading> readingIList = hazelcastInstance.getList("readings");
        final KeyValueSource<String, Reading> source = KeyValueSource.fromList(readingIList);
        System.out.println("size "+ readingIList.size());
        final Job<String, Reading> job = jobTracker.newJob(source);
        final ICompletableFuture<List<Map.Entry<String, Long>>> future = job
                .mapper(new ReadingActiveMapper<>(activeSensors))
                .reducer(new SumReducerFactory<>())
                .submit(new GroupByMillionsCollator());


//        result = future.get().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("finish map reduce");
        System.out.println(future.get().toString());//.forEach(k -> System.out.println(k ));
    }

    private List<String> filterActiveSensors(List<Sensor> sensors) {
        return sensors.stream().filter(s -> s.status == SensorStatus.ACTIVE)
                .map(s -> s.sensor_description).collect(Collectors.toList());
    }

    @Override
    protected String getHeaders() {
        return "Group;Sensor A;Sensor B\n";
    }


    @Override
    protected String formatData(String key, Long value) {
        return value+ ";" + key+"\n";
    }

    @Override
    public String getResult() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHeaders());
        if (result != null)
            result.forEach((k,v) -> stringBuilder.append(formatData(k, v)));
        return stringBuilder.toString();
    }
}
