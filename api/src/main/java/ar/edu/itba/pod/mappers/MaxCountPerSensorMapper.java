package ar.edu.itba.pod.mappers;

import ar.edu.itba.pod.constants.Month;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import com.hazelcast.mapreduce.Context;

import java.rmi.RemoteException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MaxCountPerSensorMapper/*<K> extends GenericReadingActiveMapper<K>*/ {
    @Override
    protected void emitter(Reading reading, Sensor sensor, Context<String, Long> context) {
        try {
            context.emit(sensor.sensor_description + ";" + parseDateTime(reading.getTime()),
                    Long.valueOf(reading.hourly_Counts));
        } catch (RemoteException e) {

        }
    }

    private String parseDateTime(String dateTime) throws ParseException {
        final String oldFormat = "MMMM d, yyyy hh:mm:ss a";
        final String newFormat = "dd/MM/yyyy HH:00";

        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        Date d = sdf.parse(dateTime);
        sdf.applyPattern(newFormat);
        return sdf.format(d);
    }
    public static void main(String[] args) {

        String string = "May 29, 2018 03:00:00 PM";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(string, formatter);
        System.out.println(date); // 2010-01-02
    }
}
