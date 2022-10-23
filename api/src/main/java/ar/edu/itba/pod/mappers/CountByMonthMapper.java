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
    protected void emitter(Reading reading, Sensor sensor, Context<String, Long> context) {
        if (reading.getYear().equals(year))
            context.emit(sensor.getSensor_description()+";"+reading.getMonth(), Long.valueOf(reading.getHourly_Counts()));
    }
}
