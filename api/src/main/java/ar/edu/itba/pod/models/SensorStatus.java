package ar.edu.itba.pod.models;

import java.io.Serializable;

public enum SensorStatus implements Serializable {
    ACTIVE("A"),
    REMOVED("R"),
    INACTIVE("I");

    public String id;

    SensorStatus(String id) {
        this.id = id;
    }
}
