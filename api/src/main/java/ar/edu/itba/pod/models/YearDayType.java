package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.util.Objects;

public class YearDayType implements Serializable {
    private final String year;
    private final String typeOfDay;

    public YearDayType(String year, String day) {
        this.year = year;
        this.typeOfDay = day;
    }

    public String getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearDayType that = (YearDayType) o;
        return Objects.equals(year, that.year) && Objects.equals(typeOfDay, that.typeOfDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, typeOfDay);
    }

    public int compareTo(YearDayType key) {
        int yearCompare = year.compareTo(key.year);
        if(yearCompare != 0)
            return yearCompare;
        return typeOfDay.compareTo(key.typeOfDay);
    }
}
