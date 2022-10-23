package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.MonthlyMeanKey;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MonthlyMeanPerYearReducerFactory implements ReducerFactory<MonthlyMeanKey, Long, Double> {

    private static final int MONTH = 30;

    @Override
    public Reducer<Long, Double> newReducer(MonthlyMeanKey key) {
        return new MonthlyMeanPerYearReducer();
    }

    private static class MonthlyMeanPerYearReducer extends Reducer<Long, Double> {
        private Long readingsPerMonth;

        @Override
        public void beginReduce() {
            this.readingsPerMonth = 0L;
        }

        @Override
        public void reduce(Long value) {
            readingsPerMonth += value;
        }

        @Override
        public Double finalizeReduce() {
            return (double) readingsPerMonth / MONTH;
        }
    }
}
