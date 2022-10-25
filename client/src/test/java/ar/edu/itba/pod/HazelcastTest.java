package ar.edu.itba.pod;

import ar.edu.itba.pod.constants.Queries;
import ar.edu.itba.pod.exceptions.InvalidArgumentsException;
import ar.edu.itba.pod.models.MeasurementByDayType;
import ar.edu.itba.pod.models.MeasurementByHour;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.queries.*;
import ar.edu.itba.pod.utils.*;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.test.TestHazelcastFactory;
import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HazelcastTest {

    private static final Double EPSILON = 0.00001;

    private TestHazelcastFactory hazelcastFactory;
    private HazelcastInstance member, client;
    private IList<Sensor> sensorIList;
    private IList<Reading> readingIList;
    private Arguments arguments;

    @Before
    public void setUp() {
        arguments = new Arguments();

        hazelcastFactory = new TestHazelcastFactory();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g10").setPassword("g10-pass");

        // Config
        Config config = new Config().setGroupConfig(groupConfig);
        member = hazelcastFactory.newHazelcastInstance(config);

        // Client Config
        ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig);
        client = hazelcastFactory.newHazelcastClient(clientConfig);

        sensorIList = client.getList("sensors");
        sensorIList.clear();
        fillSensorsList();

        readingIList = client.getList("readings");
        readingIList.clear();
        fillReadingsList();
    }

    private void fillSensorsList() {
        final CsvParser sensorsCsvParser = new SensorsCsvParser(sensorIList);
        Path sensorsPath = Paths.get("src/test/resources/sensors.csv");
        sensorsCsvParser.loadData(sensorsPath);
    }

    private void fillReadingsList() {
        final CsvParser readingsCsvParser = new ReadingsCsvParser(readingIList);
        Path readingsPath = Paths.get("src/test/resources/readings.csv");
        readingsCsvParser.loadData(readingsPath);
    }

    @Test
    public void fillSensorsListTest() {
        Assert.assertEquals(6, client.getList("sensors").size());
    }

    @Test
    public void fillReadingsListTest() {
        Assert.assertEquals(27, client.getList("readings").size());
    }

    @Test
    public void query1Test() throws ExecutionException, InterruptedException {
        GenericQuery<String, Long> query = new Query1(sensorIList, client, arguments, null);
        query.setGenerateOutputFile(false);
        List<Map.Entry<String, Long>> results = query.run();

        Assert.assertEquals("Sensor 1", results.get(0).getKey());
        Assert.assertEquals(Long.valueOf(1072418), results.get(0).getValue());
    }

    @Test
    public void query2Test() throws ExecutionException, InterruptedException {
        GenericQuery<String, MeasurementByDayType> query = new Query2(client, arguments, null);
        query.setGenerateOutputFile(false);
        List<Map.Entry<String, MeasurementByDayType>> results = query.run();

        Assert.assertEquals("2022", results.get(0).getKey());
        Assert.assertEquals(Long.valueOf(1276), results.get(0).getValue().getWeekdaysCount());
        Assert.assertEquals(Long.valueOf(334), results.get(0).getValue().getWeekendsCount());
        Assert.assertEquals(Long.valueOf(1610), results.get(0).getValue().getTotalCount());
    }

    @Test
    public void query3Test() throws ExecutionException, InterruptedException {
        arguments.setMin(2000);
        GenericQuery<String, MeasurementByHour> query = new Query3(sensorIList, client, arguments, null);
        query.setGenerateOutputFile(false);
        List<Map.Entry<String, MeasurementByHour>> results = query.run();

        Assert.assertEquals(3, results.size()); // Sensor 2 does not appear

        Assert.assertEquals("Sensor 1", results.get(0).getKey());
        Assert.assertEquals(Long.valueOf(1070000), Long.valueOf(results.get(0).getValue().getMeasurement()));
        Assert.assertEquals("Sun Sep 18 11:00:00 ART 2016", results.get(0).getValue().getDate().toString());
    }

    @Test
    public void query4Test() throws ExecutionException, InterruptedException {
        arguments.setN(2);
        arguments.setYear(2016);
        GenericQuery<String, Double> query = new Query4(sensorIList, client, arguments, null);
        query.setGenerateOutputFile(false);
        List<Map.Entry<String, Double>> results = query.run();

        Assert.assertEquals(2, results.size());

        String[] key = results.get(0).getKey().split(";");
        Assert.assertEquals("Sensor 1", key[0]);
        Assert.assertEquals("September", key[1]);
        Assert.assertEquals(1070000/30.0, results.get(0).getValue(), EPSILON);
    }

    @Test
    public void query5Test() throws ExecutionException, InterruptedException {
        GenericQuery<String, Long> query = new Query5(sensorIList, client, arguments, null);
        query.setGenerateOutputFile(false);
        List<Map.Entry<String, Long>> results = query.run();
        System.out.println(results);

        Assert.assertEquals(3, results.size());

        String[] sensors = results.get(0).getKey().split(";");
        Assert.assertEquals("Sensor 1", sensors[0]);
        Assert.assertEquals("Sensor 4", sensors[1]);
        Assert.assertEquals(Long.valueOf(1000000), results.get(0).getValue());
    }

    @After
    public void tearDown() {
        hazelcastFactory.shutdownAll();
    }

}
