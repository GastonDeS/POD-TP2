package ar.edu.itba.pod.utils;

import ar.edu.itba.pod.constants.SensorsHeaders;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import com.hazelcast.core.IList;

import java.util.HashMap;
import java.util.Map;

public class SensorsCsvParser extends CsvParserImpl implements CsvParser {

    Map<SensorsHeaders, Integer> headersMap;
    IList<Sensor> sensors;

    public SensorsCsvParser(IList<Sensor> sensors) {
        headersMap = new HashMap<>();
        if (sensors == null) throw new IllegalArgumentException("Readings list cannot be null");
        this.sensors = sensors;
    }

    @Override
    protected void assignIndexes(String headers) {
        String[] headersToken = headers.split(";");

        for (int i = 0; i < headersToken.length; i++) {
            for (SensorsHeaders header : SensorsHeaders.values()) {
                if (header.label.equals(headersToken[i])) {
                    headersMap.put(header, i);
                }
            }
        }
    }

    @Override
    protected void parseLine(String line) {
        String[] column = line.split(";");

        sensors.add(new Sensor(
                column[headersMap.get(SensorsHeaders.SENSOR_ID)],
                column[headersMap.get(SensorsHeaders.SENSOR_DESCRIPTION)],
                getStatus(column[headersMap.get(SensorsHeaders.STATUS)])
        ));
    }

    private SensorStatus getStatus(String column) {
        switch (column) {
            case "A": return SensorStatus.ACTIVE;
            case "I": return SensorStatus.INACTIVE;
            case "R": return SensorStatus.REMOVED;
        }
        throw new IllegalArgumentException("Invalid status");
    }
}
