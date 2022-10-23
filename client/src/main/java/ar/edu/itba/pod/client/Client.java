package ar.edu.itba.pod.client;

import ar.edu.itba.pod.exceptions.InvalidArgumentsException;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.queries.GenericQuery;
import ar.edu.itba.pod.queries.Query2;
import ar.edu.itba.pod.queries.Query1;
import ar.edu.itba.pod.queries.Query5;
import ar.edu.itba.pod.utils.Arguments;
import ar.edu.itba.pod.utils.CsvParser;
import ar.edu.itba.pod.utils.ReadingsCsvParser;
import ar.edu.itba.pod.utils.SensorsCsvParser;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static final String GROUP_NAME = "g10";
    private static final String GROUP_PASS = "g10-pass";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("hz-config Client Starting ...");

        Arguments arguments = new Arguments();

        try {
            arguments.parse();
        } catch (InvalidArgumentsException e) {
            System.out.println(e.getMessage());
        }

        /* Create instance of Hazelcast Client */
        HazelcastInstance hazelcastInstance = getHazelcastInstance(arguments.getAddresses().toArray(new String[0]));

        /* Read sensors file */
        IList<Sensor> sensorIList = hazelcastInstance.getList("sensors");
        fillSensorsList(sensorIList, arguments.getInPath());
        IList<Sensor> sensorsTestList = hazelcastInstance.getList("sensors");
        logger.info("Total sensors: " + sensorsTestList.size());

        /* Read readings file */
        IList<Reading> readingIList = hazelcastInstance.getList("readings");
        fillReadingsList(readingIList, arguments.getInPath());
        IList<Sensor> readingsTestList = hazelcastInstance.getList("readings");
        logger.info("Total readings: " + readingsTestList.size());

        Optional<GenericQuery<?, ?>> optionalQuery = Optional.empty();

        switch (arguments.getQuery()) {
            case QUERY_1:
                optionalQuery = Optional.of(new Query1(sensorIList, hazelcastInstance, arguments));
                break;
            case QUERY_2:
                optionalQuery = Optional.of(new Query2(hazelcastInstance, arguments));
                break;
            case QUERY_3:
            case QUERY_4:
            case QUERY_5:
                optionalQuery = Optional.of(new Query5(sensorIList, hazelcastInstance, arguments));
                break;
        }

        optionalQuery.orElseThrow(() -> new IllegalStateException("Error: no query to run")).run();

        /* Close all */
       // sensorIList.destroy();
       // readingIList.destroy();

        /* Shutdown */
        HazelcastClient.shutdownAll();
    }

    private static HazelcastInstance getHazelcastInstance(String[] addresses) {
        /* Client Config */
        ClientConfig clientConfig = new ClientConfig();

        /* Group Config */
        GroupConfig groupConfig = new GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASS);
        clientConfig.setGroupConfig(groupConfig);

        /* Client Network Config */
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    private static void fillSensorsList(IList<Sensor> sensorIList, String inPath) {
        final CsvParser sensorsCsvParser = new SensorsCsvParser(sensorIList);
        Path sensorsPath = Paths.get(inPath + "sensors.csv");
        sensorsCsvParser.loadData(sensorsPath);
    }

    private static void fillReadingsList(IList<Reading> readingIList, String inPath) {
        final CsvParser readingsCsvParser = new ReadingsCsvParser(readingIList);
        Path readingsPath = Paths.get(inPath + "readings.csv");
        readingsCsvParser.loadData(readingsPath);
    }

}

