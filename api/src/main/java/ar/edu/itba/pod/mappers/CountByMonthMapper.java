package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;

import java.util.List;
import java.util.Optional;

public class CountByMonthMapper<T> extends GenericReadingActiveMapper<T>{

    private final String year;

    public CountByMonthMapper(List<Sensor> activeSensors, String year) {
        super(activeSensors);
        this.year = year;
    }

    @Override
    protected void emitter(Reading reading, Optional<Sensor> sensor, Context<String, Long> context) {
        if (reading.year.equals(year))
            sensor.ifPresent(s1 -> context.emit(s1.sensor_description+";"+reading.month, Long.valueOf(reading.hourly_Counts)));
    }
}
