package ar.edu.itba.pod.combiners;

import ar.edu.itba.pod.models.MeasurementByHour;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class BoundedReadingCombinerFactory implements CombinerFactory<String, MeasurementByHour, MeasurementByHour> {
    @Override
    public Combiner<MeasurementByHour, MeasurementByHour> newCombiner(String key) {
        return new BoundedReadingCombiner();
    }

    private static class BoundedReadingCombiner extends Combiner<MeasurementByHour, MeasurementByHour> {
        private MeasurementByHour maxMeasurement = null;

        @Override
        public void combine(MeasurementByHour value) {
            if (maxMeasurement == null) {
                maxMeasurement = value;
                return;
            }
            if (value.compareTo(maxMeasurement) > 0) {
                maxMeasurement = value;
            }
        }

        @Override
        public MeasurementByHour finalizeChunk() {
            return maxMeasurement;
        }

        @Override
        public void reset() {
            maxMeasurement = null;
        }
    }
}
