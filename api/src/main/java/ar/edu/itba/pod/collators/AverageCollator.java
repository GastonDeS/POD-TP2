package ar.edu.itba.pod.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AverageCollator<K, V> implements Collator<Map.Entry<String,Long>, List<Map.Entry<String,Double>>> {

    final int n;

    public AverageCollator(int n) {
        this.n = n;
    }

    @Override
    public List<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, Long>> iterable) {
//        final long total = StreamSupport.stream(iterable.spliterator(), false)
//                .map(Map.Entry::getValue)
//                .reduce(0L, Long::sum);

        List<Map.Entry<String, Long>> sortedList = StreamSupport.stream(iterable.spliterator(),false).sorted((e1,e2) -> {
            int cmp = e1.getValue().compareTo(e2.getValue());
            if (cmp == 0) {
                cmp = -e1.getKey().compareTo(e2.getKey());
            }
            return -cmp;
        }).collect(Collectors.toList());

        List<Map.Entry<String, Double>> res = new ArrayList<>();
        for (Map.Entry<String, Long> entry : sortedList) {
            res.add(new AbstractMap.SimpleEntry<>(entry.getKey(), (double) entry.getValue() / 30));
        }

        return res.subList(0, n);
    }
}
