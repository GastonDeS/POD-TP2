package ar.edu.itba.pod.reducers;


import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class SumReducerFactory<K> implements ReducerFactory<K, Number, Number> {

    @Override
    public Reducer<Number, Number> newReducer(K key) {
        return new SumReducer();
    }
    private class SumReducer extends Reducer<Number, Number> {
        private long sum;
        @Override
        public void beginReduce() {
            sum = 0;
        }
        @Override
        public void reduce(Number value) {
            sum += value.longValue();
        }
        @Override
        public Number finalizeReduce() {
            return sum;
        }
    }
}
