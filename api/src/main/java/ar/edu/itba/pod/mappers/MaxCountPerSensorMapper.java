package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;

//public class MaxCountPerSensorMapper<K> extends GenericReadingActiveMapper<K> {
//    @Override
//    protected void emitter(Reading reading, Sensor sensor, Context<String, Long> context) {
//        context.emit(sensor.sensor_description+, Long.valueOf(reading.hourly_Counts));
//    }
//}
