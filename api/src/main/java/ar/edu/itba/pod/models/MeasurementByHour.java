package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class MeasurementByHour implements Serializable, Comparable<MeasurementByHour>{
    private final long measurement;

    private final Date date;

    public MeasurementByHour(long measurement, String time) throws ParseException {
        this.measurement = measurement;
        date = parseDateTime(time);
    }

    @Override
    public int compareTo(MeasurementByHour o) {
        return Comparator.comparingLong(MeasurementByHour::getMeasurement).compare(this, o);
    }


    public long getMeasurement() {
        return measurement;
    }

    public Date getDate() { return date; }

    private Date parseDateTime(String dateTime) throws ParseException {
        final String oldFormat = "MMMM d, yyyy hh:mm:ss a";

        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        Date d = sdf.parse(dateTime);
        return d;
    }

    public String formatDate() {
        final String newFormat = "dd/MM/yyyy HH:00";
        SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
        return sdf.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementByHour that = (MeasurementByHour) o;
        return measurement == that.measurement && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurement, date);
    }
}
