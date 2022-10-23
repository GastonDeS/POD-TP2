package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxMonthlyMeanPerYearCombinerFactory implements CombinerFactory<String, MonthlyMeanValue, MonthlyMeanValue> {
    @Override
    public Combiner<MonthlyMeanValue, MonthlyMeanValue> newCombiner(String key) {
        return new MaxMonthlyMeanPerYearCombiner();
    }

    private static class MaxMonthlyMeanPerYearCombiner extends Combiner<MonthlyMeanValue, MonthlyMeanValue> {
        private List<MonthlyMeanValue> means = new ArrayList<>();

        @Override
        public void combine(MonthlyMeanValue value) {
            means.add(value);
        }

        @Override
        public MonthlyMeanValue finalizeChunk() {
            return Collections.max(means);
        }

        @Override
        public void reset() {
            means = new ArrayList<>();
        }
    }
}

