package ar.edu.itba.pod.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OrderByDescendingCollator implements Collator<Map.Entry<String, Long>, List<Map.Entry<String, Long>>> {

    @Override
    public List<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String, Long>> iterable) {

        return StreamSupport.stream(iterable.spliterator(), false).sorted((o1, o2) -> {
            int valComp = (o1.getValue()).compareTo(o2.getValue());
            if (valComp != 0)
                return -valComp;
            return (o1.getKey()).toLowerCase().compareTo(((o2.getKey()).toLowerCase()));
        }).collect(Collectors.toList());
    }
}
