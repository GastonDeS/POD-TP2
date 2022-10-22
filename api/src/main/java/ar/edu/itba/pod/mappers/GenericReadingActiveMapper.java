package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.interfaces.SerializableMap;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SerializableHashMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;
import java.util.Optional;

public abstract class GenericReadingActiveMapper<K> implements Mapper<K, Reading, String, Long> {
    // shared constants
    private static final long ONE = 1;
    private static final long ZERO = 0;

    public SerializableMap<String, Sensor> activeSensors;

    public GenericReadingActiveMapper(List<Sensor> activeSensors) {
        this.activeSensors = new SerializableHashMap<>();
        activeSensors
                .forEach(s -> this.activeSensors.put(s.sensor_ID, s));
    }

    @Override
    public void map(K k, Reading reading, Context<String, Long> context) {
        Optional<Sensor> sensor = Optional.ofNullable(activeSensors.get(reading.sensor_ID));
        emitter(reading, sensor, context);
    }

    /////////////  Abstract Methods  /////////////
    protected abstract void emitter(Reading reading, Optional<Sensor> sensor, Context<String, Long> context);
}
