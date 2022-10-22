package ar.edu.itba.pod.utils;

import ar.edu.itba.pod.constants.ReadingsHeaders;
import ar.edu.itba.pod.models.Reading;
import com.hazelcast.core.IList;

import java.util.*;

public class ReadingsCsvParser extends CsvParserImpl implements CsvParser {

    Map<ReadingsHeaders, Integer> headersMap;
    IList<Reading> readings;


    public ReadingsCsvParser(IList<Reading> readings) {
        headersMap = new HashMap<>();
        if (readings == null) throw new IllegalArgumentException("Readings list cannot be null");
        this.readings = readings;
    }

    @Override
    protected void assignIndexes(String headers) {
        String[] headersToken = headers.split(";");

        for (int i = 0; i < headersToken.length; i++) {
            for (ReadingsHeaders header : ReadingsHeaders.values()) {
                if (header.label.equals(headersToken[i])) {
                    headersMap.put(header, i);
                }
            }
        }
    }

    @Override
    protected void parseLine(String line) {
        String[] column = line.split(";");

        readings.add(new Reading(
                column[headersMap.get(ReadingsHeaders.SENSOR_ID)],
                column[headersMap.get(ReadingsHeaders.YEAR)],
                column[headersMap.get(ReadingsHeaders.MONTH)],
                column[headersMap.get(ReadingsHeaders.MDATE)],
                column[headersMap.get(ReadingsHeaders.DAY)],
                column[headersMap.get(ReadingsHeaders.TIME)],
                column[headersMap.get(ReadingsHeaders.HOURLY_COUNTS)]
        ));
    }
}
