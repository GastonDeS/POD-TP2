package ar.edu.itba.pod.models;

import java.io.Serializable;

public class Sensor implements Serializable {
    private final String sensor_ID;
    private final String sensor_description;
    private final SensorStatus status;

    public Sensor(String sensor_ID, String sensor_description, SensorStatus status) {
        this.sensor_ID = sensor_ID;
        this.sensor_description = sensor_description;
        this.status = status;
    }

    public String getSensor_ID() {
        return sensor_ID;
    }

    public String getSensor_description() {
        return sensor_description;
    }

    public SensorStatus getStatus() {
        return status;
    }
}
