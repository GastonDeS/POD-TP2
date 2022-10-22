package ar.edu.itba.pod.queries;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.JobTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public abstract class QueryImpl implements Query {
    protected final HazelcastInstance hazelcastInstance;
//    private final CommandLine arguments;
//

    // TODO enable CommandLine this
    public QueryImpl(final HazelcastInstance hazelcastInstance/*, final CommandLine arguments*/) {
        this.hazelcastInstance = hazelcastInstance;
//        this.arguments = arguments;
    }

    @Override
    public void writeResult(String outPath) throws IOException {
        final FileWriter fileWriter = new FileWriter(outPath);
        fileWriter.write(getResult());
        fileWriter.close();
    }

    protected abstract String getHeaders();

    // TODO check if we need to change it to make it more generic
    protected abstract String formatData(String key, Long value);

    public JobTracker getJobTracker(String queryName) {
        return hazelcastInstance.getJobTracker("g12_" + queryName);
    }
}
