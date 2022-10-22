package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Reading;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;

public class ReadingActiveMapper<K> implements Mapper<K, Reading, String, Long> {
    private static final long ONE = 1;
    private static final long ZERO = 0;
    public List<String> activeSensors;

    public ReadingActiveMapper(List<String> activeSensors) {
        this.activeSensors = activeSensors;
    }

    @Override
    public void map(K k, Reading reading, Context<String, Long> context) {
        if (activeSensors.contains(reading.sensor_ID)) {
            context.emit(reading.sensor_ID, Long.valueOf(reading.hourly_Counts));
//        } else {
//            context.emit(reading.sensor_ID, ZERO);
        }
    }
}
