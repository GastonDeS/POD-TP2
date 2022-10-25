package ar.edu.itba.pod;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.utils.CsvParser;
import ar.edu.itba.pod.utils.ReadingsCsvParser;
import ar.edu.itba.pod.utils.SensorsCsvParser;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.test.TestHazelcastFactory;
import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HazelcastTest {

    private TestHazelcastFactory hazelcastFactory;
    private HazelcastInstance member, client;

    @Before
    public void setUp() {
        hazelcastFactory = new TestHazelcastFactory();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g10").setPassword("g10-pass");

        // Config
        Config config = new Config().setGroupConfig(groupConfig);

        member = hazelcastFactory.newHazelcastInstance(config);

        // Client Config
        ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig);

        client = hazelcastFactory.newHazelcastClient(clientConfig);
    }

    @Test
    public void simpleTest() {
        String mapName = "testMap";

        IMap<Integer, String> testMapFromMember = member.getMap(mapName);
        testMapFromMember.set(1, "test1");

        IMap<Integer, String> testMap = client.getMap(mapName);
        String value = testMap.get(1);
        Assert.assertEquals("test1", value);
    }

    @Test
    public void fillSensorsListTest() {
        IList<Sensor> sensorIList = client.getList("sensors");
        sensorIList.clear();
        final CsvParser sensorsCsvParser = new SensorsCsvParser(sensorIList);
        Path sensorsPath = Paths.get("src/test/resources/sensors.csv");
        sensorsCsvParser.loadData(sensorsPath);

        Assert.assertEquals(6, client.getList("sensors").size());
    }

    @Test
    public void fillReadingsListTest() {
        IList<Reading> readingIList = client.getList("readings");
        readingIList.clear();
        final CsvParser readingsCsvParser = new ReadingsCsvParser(readingIList);
        Path sensorsPath = Paths.get("src/test/resources/readings.csv");
        readingsCsvParser.loadData(sensorsPath);

        Assert.assertEquals(27, client.getList("readings").size());
    }

    @After
    public void tearDown() {
        hazelcastFactory.shutdownAll();
    }


}
