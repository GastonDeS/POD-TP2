package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.util.Objects;

public class MonthlyMeanKey implements Serializable {
    private String sensor_ID;
    private String month;

    public MonthlyMeanKey(String sensor_ID, String month) {
        this.sensor_ID = sensor_ID;
        this.month = month;
    }

    public String getSensor_ID() {
        return sensor_ID;
    }

    public String getMonth() {
        return month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthlyMeanKey)) return false;
        MonthlyMeanKey that = (MonthlyMeanKey) o;
        return sensor_ID.equals(that.sensor_ID) && month.equals(that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensor_ID, month);
    }
}
