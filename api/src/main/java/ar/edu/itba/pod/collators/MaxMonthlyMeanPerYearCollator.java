package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.models.MonthlyMeanValue;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class MaxMonthlyMeanPerYearCollator implements Collator<Map.Entry<String, MonthlyMeanValue>, List<Map.Entry<String,Double>>> {

    private static final Comparator<Map.Entry<String, Double>> ENTRY_COMPARATOR =
            (o1, o2) -> {
                int cmp = o2.getValue().compareTo(o1.getValue());
                if (cmp == 0) {
                    return o2.getKey().compareTo(o1.getKey());
                }
                return cmp;
            };

    private final int n;

    public MaxMonthlyMeanPerYearCollator(int n) {
        this.n = n;
    }

    @Override
    public List<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, MonthlyMeanValue>> iterable) {
        TreeSet<Map.Entry<String, Double>> results = new TreeSet<>(ENTRY_COMPARATOR);

        iterable.forEach(entry -> results.add(
                new AbstractMap.SimpleEntry<>(entry.getKey() + ";" + entry.getValue().getMonth(), entry.getValue().getMean()))
        );

        if (results.size() < n) {
            return new ArrayList<>(results);
        }
        return new ArrayList<>(results).subList(0, n);
    }
}