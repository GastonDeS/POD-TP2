package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;
import java.util.Optional;

public class ReadingActiveMapper<K> implements Mapper<K, Reading, String, Long> {
    private static final long ONE = 1;
    private static final long ZERO = 0;

    // TODO try to transform this into a Map or like a map
    public List<Sensor> activeSensors;

    public ReadingActiveMapper(List<Sensor> activeSensors) {
        this.activeSensors = activeSensors;
    }

    @Override
    public void map(K k, Reading reading, Context<String, Long> context) {
        Optional<Sensor> sensor = activeSensors.stream().filter(s -> s.sensor_ID.equals(reading.sensor_ID)).findFirst();
        sensor.ifPresent(value -> context.emit(value.sensor_description, Long.valueOf(reading.hourly_Counts)));
    }
}