package ar.edu.itba.pod.models;

import java.io.Serializable;

public class Sensor implements Serializable {
    public String sensor_ID;
    public String sensor_description;
    public SensorStatus status;

    public Sensor(String sensor_ID, String sensor_description, SensorStatus status) {
        this.sensor_ID = sensor_ID;
        this.sensor_description = sensor_description;
        this.status = status;
    }
}
