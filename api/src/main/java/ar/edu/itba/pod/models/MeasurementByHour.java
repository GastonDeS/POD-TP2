package ar.edu.itba.pod.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MeasurementByHour implements Serializable, Comparable<MeasurementByHour>{
    private final String inDateFormat = "d/MMMMM/yyyy HH:00";
    private final String outDateFormat = "dd/MM/yyyy HH:00";

    private final long measurement;
    private final Date date;

    public MeasurementByHour(long measurement, String year, String month, String mdate, String time) throws ParseException {
        this.measurement = measurement;
        this.date = parseDate(year, month, mdate, time);
    }

    @Override
    public int compareTo(MeasurementByHour o) {
        return Comparator.comparingLong(MeasurementByHour::getMeasurement)
                .thenComparing(MeasurementByHour::getDate)
                .compare(this, o);
    }

    public long getMeasurement() {
        return measurement;
    }

    public Date getDate() { return date; }

    public Date parseDate(String year, String month, String mdate, String time) throws ParseException {
        String formatDate = String.format("%s/%s/%s %s:00", mdate, month, year, time);
        SimpleDateFormat sdf = new SimpleDateFormat(inDateFormat, Locale.ENGLISH);
        return sdf.parse(formatDate);
    }

    public String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(outDateFormat, Locale.ENGLISH);
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

    @Override
    public String toString() {
        return "Measurement: " + measurement + "\nDate: " + date;
    }
}
