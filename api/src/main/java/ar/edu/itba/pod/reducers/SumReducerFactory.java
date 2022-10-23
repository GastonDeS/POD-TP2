package ar.edu.itba.pod.reducers;


import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class SumReducerFactory<T> implements ReducerFactory<T, Long, Long> {

    @Override
    public Reducer<Long, Long> newReducer(T key) {
        return new SumReducer();
    }

    private static class SumReducer extends Reducer<Long, Long> {
        private long sum;

        @Override
        public void beginReduce() {
            sum = 0;
        }

        @Override
        public void reduce(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
