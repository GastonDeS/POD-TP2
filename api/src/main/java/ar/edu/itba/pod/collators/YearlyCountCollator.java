package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.MeasurementByDayType;
import ar.edu.itba.pod.models.YearDayType;
import com.hazelcast.mapreduce.Collator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class YearlyCountCollator implements Collator<Map.Entry<YearDayType,Long>, List<Map.Entry<String, MeasurementByDayType>>> {
    @Override
    public List<Map.Entry<String, MeasurementByDayType>> collate(Iterable<Map.Entry<YearDayType, Long>> iterable) {


        List<Map.Entry<YearDayType, Long>> list = StreamSupport.stream(iterable.spliterator(),false).sorted((e1, e2) -> {
           return -e1.getKey().compareTo(e2.getKey());
        }).collect(Collectors.toList());

        List<Map.Entry<String, MeasurementByDayType>> results = new ArrayList<>();
        for (int i = 1; i < list.size(); i+=2) {
            Map.Entry<YearDayType, Long> prev = list.get(i-1); // _we
            Map.Entry<YearDayType, Long> actual = list.get(i); // _wd
            Long total = actual.getValue() + prev.getValue();
            results.add(new AbstractMap.SimpleEntry<>(actual.getKey().getYear(), new MeasurementByDayType(actual.getValue(), prev.getValue(), total)));
        }

        return results;
    }
}
