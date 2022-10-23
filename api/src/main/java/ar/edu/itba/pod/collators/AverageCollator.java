package ar.edu.itba.pod.collators;

import ar.edu.itba.pod.constants.Month;
import com.hazelcast.mapreduce.Collator;

import java.rmi.RemoteException;
import java.util.*;
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

//        Map<String, List<Map.Entry<String,Long>>> grouped = StreamSupport.stream(iterable.spliterator())
//                .collect(Collectors.groupingBy(e -> e.getKey().split(";")[0]));
        Comparator<Map.Entry<String, Double>> comparator = ((e1,e2) -> {
            int cmp = e1.getValue().compareTo(e2.getValue());
            if (cmp == 0) {
                cmp = -e1.getKey().compareTo(e2.getKey());
            }
            return -cmp;
        });

        Map<String, List<Map.Entry<String,Double>>> groupedBySensor = StreamSupport.stream(iterable.spliterator(),false)
                .map(e -> {
                    try {
                        return new AbstractMap.SimpleEntry<String, Double>(e.getKey(), e.getValue() / Double.parseDouble(Month.getMonthByName(e.getKey().split(";")[1]).daysQty+""));
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                        .collect(Collectors.groupingBy(e -> e.getKey().split(";")[0]));

        List<Map.Entry<String, Double>> result = new ArrayList<>();

        groupedBySensor.forEach((k,v) -> {
            Map.Entry<String, Double> max = v.stream().min(comparator).get(); // its min because we want the max
            result.add(new AbstractMap.SimpleEntry<>(max.getKey(), Math.round(max.getValue()*100.0)/100.0));
        });

        result.sort(comparator);
        return result.subList(0, n);
    }
}
