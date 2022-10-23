package ar.edu.itba.pod.models;

import java.io.Serializable;

public class Reading implements Serializable {
    public String sensor_ID;
    public String year;
    public String month;
    public String mdate;
    public String day;
    public String time;
    public String hourly_Counts;

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
}
