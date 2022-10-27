package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.util.Objects;

public class MonthlyMeanValue implements Serializable, Comparable<MonthlyMeanValue> {
    private final String month;
    private final Double mean;

    public MonthlyMeanValue(String month, Double mean) {
        this.month = month;
        this.mean = mean;
    }

    public String getMonth() {
        return month;
    }

    public Double getMean() {
        return mean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthlyMeanValue)) return false;
        MonthlyMeanValue that = (MonthlyMeanValue) o;
        return mean.equals(that.mean) && month.equals(that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mean, month);
    }

    @Override
    public int compareTo(MonthlyMeanValue o) {
        return mean.compareTo(o.mean);
    }
}