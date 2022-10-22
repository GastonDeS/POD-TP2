package ar.edu.itba.pod.constants;

public enum SensorsHeaders {
    SENSOR_ID("sensor_id"),
    SENSOR_DESCRIPTION("sensor_description"),
    STATUS("status");

    public final String label;

    SensorsHeaders(String label) {
        this.label = label;
    }
}
