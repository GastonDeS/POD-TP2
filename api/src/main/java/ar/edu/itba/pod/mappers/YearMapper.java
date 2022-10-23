package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.MonthlyMeanKey;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;

import java.util.List;

public class YearMapper extends GenericReadingActiveMapper<MonthlyMeanKey> {
    private final int year;

    public YearMapper(List<Sensor> activeSensors, int year) {
        super(activeSensors);
        this.year = year;
    }

    @Override
    protected void emitter(Reading reading, Sensor sensor, Context<MonthlyMeanKey, Long> context) {
        if (reading.getYear().equals(String.valueOf(year))) {
            context.emit(new MonthlyMeanKey(sensor.getSensor_description(), reading.getMonth()), Long.valueOf(reading.getHourly_Counts()));
        }
    }
}
