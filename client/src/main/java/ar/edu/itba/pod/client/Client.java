package ar.edu.itba.pod.client;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.utils.CsvParser;
import ar.edu.itba.pod.utils.ReadingsCsvParser;
import ar.edu.itba.pod.utils.SensorsCsvParser;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("hz-config Client Starting ...");

        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g10").setPassword("g10-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        String[] addresses = {"192.168.0.10:5701"};
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        IList<Sensor> sensorIList = hazelcastInstance.getList("sensors");
        final CsvParser sensorsCsvParser = new SensorsCsvParser(sensorIList);
        Path sensorsPath = Paths.get("/Users/gastondeschant/Downloads/pod/sensors.csv");
        sensorsCsvParser.loadData(sensorsPath);
        System.out.println(sensorIList.size());

        IList<Reading> sensorsTestList = hazelcastInstance.getList("sensors");
        System.out.println("total sensors: "+sensorsTestList.size());

        IList<Reading> readingIList = hazelcastInstance.getList("readings");
        final CsvParser readingsCsvParser = new ReadingsCsvParser(readingIList);
        Path readingsPath = Paths.get("/Users/gastondeschant/Downloads/pod/readings.csv");
        readingsCsvParser.loadData(readingsPath);
        System.out.println(readingIList.size());

        IList<Reading> readingsTestList = hazelcastInstance.getList("readings");
        System.out.println("total readings: "+readingsTestList.size());

        // Shutdown
        HazelcastClient.shutdownAll();
    }

}

