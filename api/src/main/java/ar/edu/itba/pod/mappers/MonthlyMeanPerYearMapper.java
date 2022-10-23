package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.MonthlyMeanKey;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;
import java.util.Optional;

public class MonthlyMeanPerYearMapper implements Mapper<String, Reading, MonthlyMeanKey, Long> {
    private final List<Sensor> activeSensors;
    private final int year;

    public MonthlyMeanPerYearMapper(List<Sensor> activeSensors, int year) {
        this.activeSensors = activeSensors;
        this.year = year;
    }

    @Override
    public void map(String sensor, Reading reading, Context<MonthlyMeanKey, Long> context) {
        Optional<Sensor> optionalSensor = activeSensors
                .stream()
                .filter(s -> s.sensor_ID.equals(reading.sensor_ID) && Integer.parseInt(reading.year) == year)
                .findFirst();
        optionalSensor.ifPresent(value -> context.emit(
                new MonthlyMeanKey(value.sensor_description, reading.month),
                Long.parseLong(reading.hourly_Counts))
        );
    }
}
