package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.MonthlyMeanKey;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.Month;

public class MonthlyMeanPerYearReducerFactory implements ReducerFactory<MonthlyMeanKey, Long, Double> {

    @Override
    public Reducer<Long, Double> newReducer(MonthlyMeanKey key) {
        int monthLength = Month.valueOf(key.getMonth().toUpperCase()).length(false);
        return new MonthlyMeanPerYearReducer(monthLength);
    }

    private static class MonthlyMeanPerYearReducer extends Reducer<Long, Double> {
        private Long readingsPerMonth;
        private final int monthLength;

        private MonthlyMeanPerYearReducer(int monthLength) {
            this.monthLength = monthLength;
        }

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
            return (double) readingsPerMonth / monthLength;
        }
    }
}
