package ar.edu.itba.pod.models;

import java.io.Serializable;

public class MeasurementByDayType implements Serializable {
    private final Long weekdaysCount;
    private final Long weekendsCount;
    private final Long totalCount;

    public MeasurementByDayType(Long weekdaysCount, Long weekendsCount, Long totalCount) {
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

    @Override
    public String toString() {
        return "Weekdays: " + weekdaysCount + "\nWeekends: " + weekendsCount + "\nTotal: " + totalCount;
    }
}
