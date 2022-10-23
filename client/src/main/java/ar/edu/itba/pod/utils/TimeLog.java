package ar.edu.itba.pod.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeLog {
    private BufferedWriter bufferedWriter;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");

    public TimeLog(String outPath, String timeFile) {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(outPath + timeFile, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String data) {
        try {
            bufferedWriter.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void addLog(String method, String className, int line, String msg) {
        write(formatter.format(new Date()) + " INFO [" + method + "] " + className +
                " (" + className + ".java:" + line + ") - " + msg + "\n");
    }

    public void close() {
        if (bufferedWriter != null) try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
