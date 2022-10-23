package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.MonthlyMeanKey;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class MonthlyMeanPerYearCombinerFactory implements CombinerFactory<MonthlyMeanKey, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(MonthlyMeanKey key) {
        return new MonthlyMeanPerYearCombiner();
    }

    private static class MonthlyMeanPerYearCombiner extends Combiner<Long, Long> {
        private Long readingsPerMonth = 0L;

        @Override
        public void combine(Long value) {
            readingsPerMonth += value;
        }

        @Override
        public Long finalizeChunk() {
            return readingsPerMonth;
        }

        @Override
        public void reset() {
            readingsPerMonth = 0L;
        }
    }
}
