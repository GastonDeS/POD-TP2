package ar.edu.itba.pod.constants;

public enum ReadingsHeaders {
    SENSOR_ID("Sensor_ID"),
    YEAR("Year"),
    MONTH("Month"),
    MDATE("Mdate"),
    DAY("Day"),
    TIME("Time"),
    HOURLY_COUNTS("Hourly_Counts");

    private final String label;

    ReadingsHeaders(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
