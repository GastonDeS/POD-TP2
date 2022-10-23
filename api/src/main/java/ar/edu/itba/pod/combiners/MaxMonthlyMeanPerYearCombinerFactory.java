package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class MaxMonthlyMeanPerYearCombinerFactory implements CombinerFactory<String, MonthlyMeanValue, MonthlyMeanValue> {
    @Override
    public Combiner<MonthlyMeanValue, MonthlyMeanValue> newCombiner(String key) {
        return new MaxMonthlyMeanPerYearCombiner();
    }

    private static class MaxMonthlyMeanPerYearCombiner extends Combiner<MonthlyMeanValue, MonthlyMeanValue> {
        private MonthlyMeanValue maxMean = new MonthlyMeanValue(null, 0.0);

        @Override
        public void combine(MonthlyMeanValue value) {
            if (value.getMean() > maxMean.getMean()) {
                maxMean = value;
            }
        }

        @Override
        public MonthlyMeanValue finalizeChunk() {
            return maxMean;
        }

        @Override
        public void reset() {
            maxMean = new MonthlyMeanValue(null, 0.0);
        }
    }
}
