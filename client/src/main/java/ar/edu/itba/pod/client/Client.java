package ar.edu.itba.pod.client;

import ar.edu.itba.pod.constants.Queries;
import ar.edu.itba.pod.exceptions.InvalidArgumentsException;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.queries.*;
import ar.edu.itba.pod.utils.*;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import jdk.jfr.StackTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static final String GROUP_NAME = "g10";
    private static final String GROUP_PASS = "g10-pass";

    public static void main(String[] args) throws ExecutionException, InterruptedException, InvalidArgumentsException {
        logger.info("hz-config Client Starting ...");

        /* Get arguments */
        final Arguments arguments = new Arguments();

        try {
            arguments.parse();
        } catch (InvalidArgumentsException e) {
            System.out.println(e.getMessage());
        }

        /* Create instance of Hazelcast Client */
        final HazelcastInstance hazelcastInstance = getHazelcastInstance(arguments.getAddresses().toArray(new String[0]));

        /* Log reading file start time */
        final TimeLog timeLog = new TimeLog(arguments.getOutPath(), arguments.getQuery().getTimeFile());
        timeLog.addLog(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "Client",
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                "Reading file starting"
        );

        /* Read sensors file */
        IList<Sensor> sensorIList = hazelcastInstance.getList("sensors");
        sensorIList.clear();
        fillSensorsList(sensorIList, arguments.getInPath());
        logger.info("Total sensors: " + hazelcastInstance.getList("sensors").size());

        /* Read readings file */
        IList<Reading> readingIList = hazelcastInstance.getList("readings");
        readingIList.clear();
        fillReadingsList(readingIList, arguments.getInPath());
        logger.info("Total readings: " + hazelcastInstance.getList("readings").size());

        /* Log reading file finish time */
        timeLog.addLog(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "Client",
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                "Finished reading file"
        );

        Optional<GenericQuery<?, ?>> optionalQuery = Optional.empty();

        switch (arguments.getQuery()) {
            case QUERY_1:
                optionalQuery = Optional.of(new Query1(sensorIList, hazelcastInstance, arguments, timeLog));
                break;
            case QUERY_2:
                optionalQuery = Optional.of(new Query2(hazelcastInstance, arguments, timeLog));
                break;
            case QUERY_3:
                optionalQuery = Optional.of(new Query3(sensorIList, hazelcastInstance, arguments, timeLog));
                break;
            case QUERY_4:
                optionalQuery = Optional.of(new Query4(sensorIList, hazelcastInstance, arguments, timeLog));
                break;
            case QUERY_5:
                optionalQuery = Optional.of(new Query5(sensorIList, hazelcastInstance, arguments, timeLog));
                break;
        }

        optionalQuery.orElseThrow(() -> new IllegalStateException("Error: no query to run")).run();

        /* Shutdown */
        timeLog.close();
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

