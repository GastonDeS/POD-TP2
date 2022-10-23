package ar.edu.itba.pod.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class YearlyCountCollator<K,V> implements Collator<Map.Entry<String,Long>, List<Map.Entry<String,Long>>> {
    @Override
    public List<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String, Long>> iterable) {
        List<Map.Entry> list = StreamSupport.stream(iterable.spliterator(),false).sorted((e1, e2) -> {
           return -e1.getKey().compareTo(e2.getKey());
        }).collect(Collectors.toList());

        List<Map.Entry<String, Long>> list1 = new ArrayList<>();
        for (int i = 1; i < list.size(); i+=2) {
            Map.Entry<String, Long> prev = list.get(i-1); // _we
            Map.Entry<String,Long> actual = list.get(i); // _wd
            System.out.println("prev: " + prev.getKey() + ", value: " + prev.getValue());
            System.out.println("actual: " + actual.getKey() + ", value: " + actual.getValue());
            String token = prev.getKey().split("_")[0];
            list1.add(new AbstractMap.SimpleEntry<>((token + ";" + actual.getValue() + ";" + prev.getValue()), (actual.getValue()) + prev.getValue()));
        }

        return list1;
    }
}
