package ar.edu.itba.pod.utils;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Triple{
    private Long weekdaysCount;
    private Long weekendsCount;
    private Long totalCount;

    public Triple(Long weekdaysCount, Long weekendsCount, Long totalCount) {
        this.weekdaysCount = weekdaysCount;
        this.weekendsCount = weekendsCount;
        this.totalCount = totalCount;
    }

    public Long getWeekdaysCount() {
        return weekdaysCount;
    }

    public void setWeekdaysCount(Long weekdaysCount) {
        this.weekdaysCount = weekdaysCount;
    }

    public Long getWeekendsCount() {
        return weekendsCount;
    }

    public void setWeekendsCount(Long weekendsCount) {
        this.weekendsCount = weekendsCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
