package ar.edu.itba.pod.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        logger.info("hz-config Client Starting ...");

        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        // Logging Information
        clientConfig.setProperty("hazelcast.logging.type", "slf4j");

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g10").setPassword("g10-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        String[] addresses = {"192.168.1.51:5701"};
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        String mapName = "testMap";

        IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);
        testMapFromMember.set(1, "test1");
        IMap<Integer, String> testMap = hazelcastInstance.getMap(mapName);
        System.out.println(testMap.get(1));

        // Shutdown
        HazelcastClient.shutdownAll();
    }

}

