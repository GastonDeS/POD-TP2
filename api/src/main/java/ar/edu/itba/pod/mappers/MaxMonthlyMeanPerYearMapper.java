package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MaxMonthlyMeanPerYearMapper implements Mapper<String, MonthlyMeanValue, String, MonthlyMeanValue> {
    @Override
    public void map(String sensor, MonthlyMeanValue monthlyMean, Context<String, MonthlyMeanValue> context) {
        context.emit(sensor, monthlyMean);
    }
}
