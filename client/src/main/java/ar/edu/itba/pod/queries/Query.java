package ar.edu.itba.pod.queries;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface Query {
    void run() throws ExecutionException, InterruptedException, IOException;
    void writeResult(String outPath) throws IOException;
    String getResult();
}
