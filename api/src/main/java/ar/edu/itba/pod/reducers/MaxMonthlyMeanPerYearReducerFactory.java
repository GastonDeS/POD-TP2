package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MaxMonthlyMeanPerYearReducerFactory implements ReducerFactory<String, MonthlyMeanValue, MonthlyMeanValue> {

    @Override
    public Reducer<MonthlyMeanValue, MonthlyMeanValue> newReducer(String key) {
        return new MaxMonthlyMeanPerYearReducer();
    }

    private static class MaxMonthlyMeanPerYearReducer extends Reducer<MonthlyMeanValue, MonthlyMeanValue> {
        private MonthlyMeanValue maxMean;

        @Override
        public void beginReduce() {
            this.maxMean = new MonthlyMeanValue(null, 0.0);
        }

        @Override
        public void reduce(MonthlyMeanValue value) {
            if (value.getMean() > maxMean.getMean()) {
                maxMean = value;
            }
        }

        @Override
        public MonthlyMeanValue finalizeReduce() {
            return maxMean;
        }
    }
}