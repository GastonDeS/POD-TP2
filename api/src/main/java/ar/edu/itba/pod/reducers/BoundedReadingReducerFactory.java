package ar.edu.itba.pod.reducers;

import ar.edu.itba.pod.models.MeasurementByHour;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class BoundedReadingReducerFactory implements ReducerFactory<String, MeasurementByHour, MeasurementByHour> {

    @Override
    public Reducer<MeasurementByHour, MeasurementByHour> newReducer(String key) {
        return new BoundedReadingReducer();
    }

    private static class BoundedReadingReducer extends Reducer<MeasurementByHour, MeasurementByHour> {
        private MeasurementByHour maxMeasurement;

        @Override
        public void beginReduce() {
            this.maxMeasurement = null;
        }

        @Override
        public void reduce(MeasurementByHour value) {
            if (maxMeasurement == null) {
                maxMeasurement = value;
                return;
            }
            if (value.compareTo(maxMeasurement) > 0) {
                maxMeasurement = value;
            }
        }

        @Override
        public MeasurementByHour finalizeReduce() {
            return maxMeasurement;
        }
    }
}
