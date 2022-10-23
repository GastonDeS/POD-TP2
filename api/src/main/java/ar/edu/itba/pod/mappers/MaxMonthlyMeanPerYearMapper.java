package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.MonthlyMeanKey;
import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MaxMonthlyMeanPerYearMapper implements Mapper<MonthlyMeanKey, Double, String, MonthlyMeanValue> {
    @Override
    public void map(MonthlyMeanKey key, Double monthlyMean, Context<String, MonthlyMeanValue> context) {
        context.emit(key.getSensor_ID(), new MonthlyMeanValue(key.getMonth(), monthlyMean));
    }
}
