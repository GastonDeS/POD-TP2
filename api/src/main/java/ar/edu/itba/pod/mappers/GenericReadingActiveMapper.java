package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.interfaces.SerializableMap;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SerializableHashMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;
import java.util.Optional;

public abstract class GenericReadingActiveMapper<KOut> implements Mapper<String, Reading, KOut, Long> {
    public SerializableMap<String, Sensor> activeSensors;

    public GenericReadingActiveMapper(List<Sensor> activeSensors) {
        this.activeSensors = new SerializableHashMap<>();
        activeSensors
                .forEach(s -> this.activeSensors.put(s.getSensor_ID(), s));
    }

    @Override
    public void map(String k, Reading reading, Context<KOut, Long> context) {
        Optional<Sensor> sensor = Optional.ofNullable(activeSensors.get(reading.getSensor_ID()));
        sensor.ifPresent(s -> emitter(reading, s, context));
    }

    /////////////  Abstract Methods  /////////////
    protected abstract void emitter(Reading reading, Sensor sensor, Context<KOut, Long> context);
}
