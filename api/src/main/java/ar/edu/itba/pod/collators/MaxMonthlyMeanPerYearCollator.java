package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Collator;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MaxMonthlyMeanPerYearCollator implements Collator<Map.Entry<String, MonthlyMeanValue>, List<Map.Entry<String,Double>>> {

    @Override
    public List<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, MonthlyMeanValue>> iterable) {
        List<Map.Entry<String, MonthlyMeanValue>> list = StreamSupport.stream(iterable.spliterator(),false).collect(Collectors.toList());

        List<Map.Entry<String, Double>> results = new ArrayList<>();
        for (Map.Entry<String, MonthlyMeanValue> entry : list) {
            String key = entry.getKey() + ";" + entry.getValue().getMonth();
            results.add(new AbstractMap.SimpleEntry<>(key, entry.getValue().getMean()));
        }
        return results;
    }
}