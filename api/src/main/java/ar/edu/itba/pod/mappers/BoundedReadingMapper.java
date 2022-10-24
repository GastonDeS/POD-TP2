package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.MeasurementByHour;
import ar.edu.itba.pod.models.MonthlyMeanKey;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public class BoundedReadingMapper extends GenericReadingActiveMapper<String, MeasurementByHour> {
    private final int min;

    public BoundedReadingMapper(List<Sensor> activeSensors, int min) {
        super(activeSensors);
        this.min = min;
    }

    @Override
    protected void emitter(Reading reading, Sensor sensor, Context<String, MeasurementByHour> context) {
        if (Long.parseLong(reading.getHourly_Counts()) >= min) {
            try {
                context.emit(
                        reading.getSensor_ID(),
                        new MeasurementByHour(Long.parseLong(reading.getHourly_Counts()), reading.getTime()));
            } catch (ParseException e) {
                // nothing to do
            }
        }
    }
}