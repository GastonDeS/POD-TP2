package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.YearDayType;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TypeOfDayMapper implements Mapper<String, Reading, YearDayType, Long> {

    private final List<String> weekendDays = getWeekendDays();

    @Override
    public void map(String k, Reading reading, Context<YearDayType, Long> context) {
        if(isWeekend(reading.getDay()))
            context.emit(new YearDayType(reading.getYear(), "WE"), Long.valueOf(reading.getHourly_Counts()));
        else
            context.emit(new YearDayType(reading.getYear(), "WD"), Long.valueOf(reading.getHourly_Counts()));
    }

    private boolean isWeekend(String day) {
        return weekendDays.contains(day);
    }

    private List<String> getWeekendDays(){
        return Arrays.asList(DayOfWeek.SATURDAY.getDisplayName(TextStyle.FULL, Locale.ENGLISH), DayOfWeek.SUNDAY.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    }
}
