package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.MeasurementByDayType;
import com.hazelcast.mapreduce.Collator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class YearlyCountCollator implements Collator<Map.Entry<String,Long>, List<Map.Entry<String, MeasurementByDayType>>> {
    @Override
    public List<Map.Entry<String, MeasurementByDayType>> collate(Iterable<Map.Entry<String, Long>> iterable) {

        List<Map.Entry<String, Long>> list = StreamSupport.stream(iterable.spliterator(),false).sorted((e1, e2) -> {
           return -e1.getKey().compareTo(e2.getKey());
        }).collect(Collectors.toList());

        List<Map.Entry<String, MeasurementByDayType>> results = new ArrayList<>();
        for (int i = 1; i < list.size(); i+=2) {
            Map.Entry<String, Long> prev = list.get(i-1); // _we
            Map.Entry<String, Long> actual = list.get(i); // _wd
            Long total = actual.getValue() + prev.getValue();
            String token = prev.getKey().split("_")[0];
            results.add(new AbstractMap.SimpleEntry<>(token, new MeasurementByDayType(actual.getValue(), prev.getValue(), total)));
        }

        return results;
    }
}
