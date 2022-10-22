package ar.edu.itba.pod.collators;

import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// TODO check why we need K and V if not explodes
public class GroupByMillionsCollator<K, V> implements Collator<Map.Entry<String,Long>, List<Map.Entry<String,Long>>> {

    private static Long MILLION = 1000000L;

    @Override
    public List<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String,Long>> iterable) {
        List<Map.Entry> list = StreamSupport.stream(iterable.spliterator(),false).sorted((e1,e2) -> {
            long cmp = (e1.getValue()/MILLION) - (e2.getValue()/MILLION);
            if (cmp == 0) {
                cmp = -e1.getKey().compareTo(e2.getKey());
            }
            return (int) -cmp;
        }).collect(Collectors.toList());

        List<Map.Entry<String, Long>> list1 = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            Map.Entry<String, Long> prev = list.get(i-1);
            Map.Entry<String,Long> actual = list.get(i);
            if (prev.getValue() / MILLION == actual.getValue() / MILLION &&
                    prev.getValue() >= MILLION) {
                list1.add(new AbstractMap.SimpleEntry<>(prev.getKey()+";"+actual.getKey(), (prev.getValue()/MILLION)*MILLION));
            }

        }
        return list1;
    }
}