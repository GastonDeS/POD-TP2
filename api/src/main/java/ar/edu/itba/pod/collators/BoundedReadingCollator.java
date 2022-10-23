package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.MeasurementByHour;
import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BoundedReadingCollator implements Collator<Map.Entry<String, MeasurementByHour>, List<Map.Entry<String,MeasurementByHour>>> {

    @Override
    public List<Map.Entry<String, MeasurementByHour>> collate(Iterable<Map.Entry<String, MeasurementByHour>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).sorted((o1, o2) -> {
            int valComp = (o1.getValue()).compareTo(o2.getValue());
            if (valComp != 0)
                return -valComp;
            return (o1.getKey()).toLowerCase().compareTo(((o2.getKey()).toLowerCase()));
        }).collect(Collectors.toList());
    }
}
