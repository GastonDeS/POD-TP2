package ar.edu.itba.pod.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OrderByDescendingCollator<K, V> implements Collator<Map.Entry<String,Long>, List<Map.Entry<String,Long>>> {

    @Override
    public List<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String, Long>> iterable) {
        System.out.println(iterable.spliterator().estimateSize());
        List<Map.Entry<String, Long>> sorted = StreamSupport.stream(iterable.spliterator(),false).sorted((o1, o2) -> {
            System.out.println("o1" + o1.getKey() + o1.getValue());
            System.out.println("o2" + o2.getKey() + o2.getValue());
            int valComp = (o1.getValue()).compareTo(o2.getValue());
            if(valComp != 0)
                return -valComp;
            return  (o1.getKey()).toLowerCase().compareTo(((o2.getKey()).toLowerCase()));
        }).collect(Collectors.toList());

        return sorted;
    }
}