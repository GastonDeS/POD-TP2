package ar.edu.itba.pod.models;

import java.io.Serializable;

public class Reading implements Serializable {
    private final String sensor_ID;
    private final String year;
    private final String month;
    private final String mdate;
    private final String day;
    private final String time;
    private final String hourly_Counts;

    public Reading(String sensor_ID, String year, String month, String mdate, String day, String time, String hourly_Counts) {
        this.sensor_ID = sensor_ID;
        this.year = year;
        this.month = month;
        this.mdate = mdate;
        this.day = day;
        this.time = time;
        this.hourly_Counts = hourly_Counts;
    }

    @Override
    public String toString() {
        return "Reading{" +
                "sensor_ID='" + sensor_ID + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", mdate='" + mdate + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", hourly_Counts='" + hourly_Counts + '\'' +
                '}';
    }

    public String getSensor_ID() {
        return sensor_ID;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getMdate() {
        return mdate;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getHourly_Counts() {
        return hourly_Counts;
    }
}
