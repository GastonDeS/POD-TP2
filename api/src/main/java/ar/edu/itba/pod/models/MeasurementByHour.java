package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.util.Comparator;

public class MeasurementByHour implements Serializable, Comparable<MeasurementByHour>{
    private int year;
    private int month;
    private int day;
    private int time;
    private long measurement;

    public MeasurementByHour withYear(int year) {
        this.year = year;
        return this;
    }

    public MeasurementByHour withMonth(int month) {
        this.month = month;
        return this;
    }

    public MeasurementByHour withDay(int day) {
        this.day = day;
        return this;
    }

    public MeasurementByHour withTime(int time) {
        this.time = time;
        return this;
    }

    public MeasurementByHour withMeasurement(long measurement) {
        this.measurement = measurement;
        return this;
    }


    @Override
    public int compareTo(MeasurementByHour o) {
        return Comparator.comparingLong(MeasurementByHour::getMeasurement).compare(this, o);
    }

    /*
        GETTERS
     */

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    public long getMeasurement() {
        return measurement;
    }
}
