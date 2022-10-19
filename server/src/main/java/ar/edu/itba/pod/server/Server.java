package ar.edu.itba.pod.server;


import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("hz-config Server Starting ...");
        Config config = new Config();

        // Logging Information
        config.setProperty("hazelcast.logging.type", "slf4j");

        // Group Config

        GroupConfig groupConfig = new GroupConfig().setName("g10").setPassword("g10-pass");
        config.setGroupConfig(groupConfig);

        // Network Config

        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()

                .setInterfaces(Collections.singletonList("127.0.0.*"))
                .setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

//        // Management Center Config
//        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig()
//                .setUrl("http://localhost:32768/mancenter/")
//                .setEnabled(true);
//        config.setManagementCenterConfig(managementCenterConfig);

        Hazelcast.newHazelcastInstance(config);
        logger.info("Server started");
    }

}

