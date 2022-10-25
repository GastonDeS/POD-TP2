package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.constants.*;
import ar.edu.itba.pod.models.*;
import ar.edu.itba.pod.utils.*;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public abstract class GenericQuery<K, V> {
    protected final HazelcastInstance hazelcastInstance;
    private final String outPath;
    private final Queries query;
    private final TimeLog timeLog;
    private boolean generateOutputFile = true;

    public GenericQuery(final HazelcastInstance hazelcastInstance, final Arguments arguments, final TimeLog timeLog) {
        this.hazelcastInstance = hazelcastInstance;
        this.outPath = arguments.getOutPath();
        this.query = arguments.getQuery();
        this.timeLog = timeLog;
    }

    public List<Map.Entry<K, V>> run() throws ExecutionException, InterruptedException {
        /* Log start time */
        if (generateOutputFile) {
            timeLog.addLog(
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    "GenericQuery",
                    Thread.currentThread().getStackTrace()[1].getLineNumber(),
                    "Starting map reduce"
            );
        }

        /* Submit job */
        ICompletableFuture<List<Map.Entry<K, V>>> future = this.submit();

        /* Get results */
        List<Map.Entry<K, V>> results = future.get();

        /* Write results */
        if (generateOutputFile) {
            String info = getResult(results);
            writeResult(info);
        }

        /* Log finish time */
        if (generateOutputFile) {
            timeLog.addLog(
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    "GenericQuery",
                    Thread.currentThread().getStackTrace()[1].getLineNumber(),
                    "Ending map reduce"
            );
        }

        return results;
    }

    private void writeResult(String info) {
        try {
            final FileWriter fileWriter = new FileWriter(outPath + query.getQueryFile());
            fileWriter.write(info);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error: cannot write results to file " + outPath + query.getQueryFile());
        }
    }

    private String getResult(List<Map.Entry<K, V>> results) {
        final StringBuilder stringBuilder = new StringBuilder();
        // Add headers
        stringBuilder.append(this.getHeaders());
        // Add results
        results.stream().map(this::formatData).forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    /*
        ABSTRACT METHODS
    */

    protected abstract ICompletableFuture<List<Map.Entry<K, V>>> submit() throws ExecutionException, InterruptedException;

    protected abstract String getHeaders();

    protected abstract String formatData(Map.Entry<K, V> entry);

    protected Job<String, Reading> getJobFromReadingsList(String queryName) {
        final JobTracker jobTracker = hazelcastInstance.getJobTracker("g10_" + queryName);

        final IList<Reading> readingIList = hazelcastInstance.getList("readings");
        final KeyValueSource<String, Reading> source = KeyValueSource.fromList(readingIList);

        return jobTracker.newJob(source);
    }

    /*
        COMMON UTILS
    */

    protected List<Sensor> filterActiveSensors(List<Sensor> sensors) {
        return sensors.stream().filter(s -> s.getStatus() == SensorStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    public void setGenerateOutputFile(boolean generateOutputFile) {
        this.generateOutputFile = generateOutputFile;
    }
}
