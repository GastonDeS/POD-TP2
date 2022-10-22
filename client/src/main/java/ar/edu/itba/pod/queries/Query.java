package ar.edu.itba.pod.queries;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface Query<K, V> {
    void run() throws ExecutionException, InterruptedException, IOException;

    void writeResult() throws IOException;

    String getResult(List<Map.Entry<K, V>> results);
}
