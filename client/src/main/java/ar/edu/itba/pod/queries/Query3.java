package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.BoundedReadingCollator;
import ar.edu.itba.pod.combiners.BoundedReadingCombinerFactory;
import ar.edu.itba.pod.mappers.BoundedReadingMapper;
import ar.edu.itba.pod.models.MeasurementByHour;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.reducers.BoundedReadingReducerFactory;
import ar.edu.itba.pod.utils.Arguments;
import ar.edu.itba.pod.utils.TimeLog;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query3 extends GenericQuery<String, MeasurementByHour> {

    private final List<Sensor> activeSensors;

    private final int min;

    public Query3(List<Sensor> sensors, HazelcastInstance hazelcastInstance, Arguments arguments, TimeLog timeLog) {
        super(hazelcastInstance, arguments, timeLog);
        this.activeSensors = filterActiveSensors(sensors);
        this.min = arguments.getMin();
    }

    @Override
    protected ICompletableFuture<List<Map.Entry<String, MeasurementByHour>>> submit() throws ExecutionException, InterruptedException {
        final Job<String, Reading> job = getJobFromReadingsList("q3");
        return job
                .mapper(new BoundedReadingMapper(activeSensors, min))
                .combiner(new BoundedReadingCombinerFactory())
                .reducer(new BoundedReadingReducerFactory())
                .submit(new BoundedReadingCollator());

    }

    @Override
    protected String getHeaders() {
        return "Sensor;Max_Reading_Count;Max_Reading_DateTime\n";
    }

    @Override
    protected String formatData(Map.Entry<String, MeasurementByHour> entry) {
        return entry.getKey() + ";" + entry.getValue().getMeasurement() + ";" + entry.getValue().formatDate()+"\n";
    }
}
