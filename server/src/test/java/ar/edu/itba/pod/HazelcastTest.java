package ar.edu.itba.pod;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.test.TestHazelcastFactory;
import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Collections;

public class HazelcastTest {

    private TestHazelcastFactory hazelcastFactory;
    private HazelcastInstance member, client;

    @Before
    public void setUp() throws FileNotFoundException {
        hazelcastFactory = new TestHazelcastFactory();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g10").setPassword("g10-pass");

        // Config
        Config config = new Config();
        config.setGroupConfig(groupConfig);
        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Collections.singletonList("192.*.*.*")).setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig().setInterfaces(interfacesConfig).setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

        // Management Center Config
        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig()
                .setUrl("http://localhost:32768/mancenter/")
                .setEnabled(true);
        config.setManagementCenterConfig(managementCenterConfig);

        member = hazelcastFactory.newHazelcastInstance(config);

        // Client Config
        ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig);
        clientConfig.setNetworkConfig(new ClientNetworkConfig().addAddress("192.168.1.51:5701"));
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

    @After
    public void tearDown() {
        hazelcastFactory.shutdownAll();
    }

}
