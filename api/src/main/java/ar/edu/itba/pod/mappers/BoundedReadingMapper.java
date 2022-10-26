package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.MeasurementByHour;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;

import java.text.ParseException;
import java.util.List;

public class BoundedReadingMapper extends GenericReadingActiveMapper<String, MeasurementByHour> {
    private final int min;

    public BoundedReadingMapper(List<Sensor> activeSensors, int min) {
        super(activeSensors);
        this.min = min;
    }

    @Override
    protected void emitter(Reading reading, Sensor sensor, Context<String, MeasurementByHour> context) {
        if (Long.parseLong(reading.getHourly_Counts()) > min) {
            try {
                context.emit(
                        sensor.getSensor_description(),
                        new MeasurementByHour(
                                Long.parseLong(reading.getHourly_Counts()),
                                reading.getYear(),
                                reading.getMonth(),
                                reading.getMdate(),
                                reading.getTime()));
            } catch (ParseException e) {
                // Don't count this reading
            }
        }
    }
}