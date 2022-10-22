package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;

import java.util.List;
import java.util.Optional;

public class SensorDescAndHourlyMapper<K> extends GenericReadingActiveMapper<K>{
    public SensorDescAndHourlyMapper(List<Sensor> activeSensors) {
        super(activeSensors);
    }

    @Override
    protected void emitter(Reading reading, Optional<Sensor> sensor, Context<String, Long> context) {
        sensor.ifPresent(value -> context.emit(sensor.get().sensor_description, Long.valueOf(reading.hourly_Counts)));
    }
}
