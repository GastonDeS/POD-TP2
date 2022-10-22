package ar.edu.itba.pod.collators;

import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GroupByMillionsCollator<K extends Comparable, V extends Comparable> implements Collator<Map.Entry<K,V>, List<Map.Entry<String,Long>>> {

    private static Long MILLION = 1000000L;

    @Override
    public List<Map.Entry<String, Long>> collate(Iterable<Map.Entry<K, V>> iterable) {
        List<Map.Entry> list = StreamSupport.stream(iterable.spliterator(),false).sorted((e1,e2) -> {
            int cmp = e1.getValue().compareTo(e2.getValue());
            if (cmp == 0) {
                cmp = e1.getKey().compareTo(e2.getKey());
            }
            return -cmp;
        }).collect(Collectors.toList());

        List<Map.Entry<String, Long>> list1 = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            Map.Entry<K, Long> prev = list.get(i-1);
            Map.Entry<K,Long> actual = list.get(i);
            if (prev.getValue() / MILLION == prev.getValue() / MILLION &&
                    prev.getValue() >= MILLION) {
                list1.add(new AbstractMap.SimpleEntry<>(prev.getKey().toString()+";"+actual.getKey().toString(), (prev.getValue()/MILLION)*MILLION));
            }

        }
        return list1;
    }
}
