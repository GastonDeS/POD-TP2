package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxMonthlyMeanPerYearReducerFactory implements ReducerFactory<String, MonthlyMeanValue, MonthlyMeanValue> {

    @Override
    public Reducer<MonthlyMeanValue, MonthlyMeanValue> newReducer(String key) {
        return new MaxMonthlyMeanPerYearReducer();
    }

    private static class MaxMonthlyMeanPerYearReducer extends Reducer<MonthlyMeanValue, MonthlyMeanValue> {
        private List<MonthlyMeanValue> means;

        @Override
        public void beginReduce() {
            this.means = new ArrayList<>();
        }

        @Override
        public void reduce(MonthlyMeanValue value) {
            means.add(value);
        }

        @Override
        public MonthlyMeanValue finalizeReduce() {
            return Collections.max(means);
        }
    }
}