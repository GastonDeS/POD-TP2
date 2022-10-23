package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Triple implements Serializable {
    private final Long weekdaysCount;
    private final Long weekendsCount;
    private final Long totalCount;

    public Triple(Long weekdaysCount, Long weekendsCount, Long totalCount) {
        this.weekdaysCount = weekdaysCount;
        this.weekendsCount = weekendsCount;
        this.totalCount = totalCount;
    }

    public Long getWeekdaysCount() {
        return weekdaysCount;
    }


    public Long getWeekendsCount() {
        return weekendsCount;
    }


    public Long getTotalCount() {
        return totalCount;
    }
}
