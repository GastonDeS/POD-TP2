package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.MeasurementByHour;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;
import java.util.Optional;

public class BoundedReadingMapper implements Mapper<String, Reading, String, MeasurementByHour> {
    private final List<Sensor> activeSensors;
    private final int min;

    public BoundedReadingMapper(List<Sensor> activeSensors, int min) {
        this.activeSensors = activeSensors;
        this.min = min;
    }

    @Override
    public void map(String sensor, Reading reading, Context<String, MeasurementByHour> context) {
        Optional<Sensor> optionalSensor = activeSensors
                .stream()
                .filter(s -> s.getSensor_ID().equals(reading.getSensor_ID()) && Integer.parseInt(reading.getHourly_Counts()) >= min)
                .findFirst();
        optionalSensor.ifPresent(value -> context.emit(
                reading.getSensor_ID(),
                new MeasurementByHour()
                        .withMeasurement(Long.parseLong(reading.getHourly_Counts()))
                        .withYear(Integer.parseInt(reading.getYear()))
                        .withMonth(Integer.parseInt(reading.getMonth()))
                        .withDay(Integer.parseInt(reading.getDay()))
                        .withTime(Integer.parseInt(reading.getTime())))
        );
    }
}