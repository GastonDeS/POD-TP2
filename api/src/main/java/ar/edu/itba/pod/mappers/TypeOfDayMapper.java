package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;
import java.util.Optional;
public class TypeOfDayMapper<K> implements Mapper<K, Reading, String, Long> {

    @Override
    public void map(K k, Reading reading, Context<String, Long> context) {
        if(isWeekend(reading.day))
            context.emit(reading.year + "_we", Long.valueOf(reading.hourly_Counts));
        else
            context.emit(reading.year + "_wd", Long.valueOf(reading.hourly_Counts));
    }
    private boolean isWeekend(String day){
        return day.equals("Saturday") || day.equals("Sunday");
    }
}
