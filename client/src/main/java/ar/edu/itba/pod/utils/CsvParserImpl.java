package ar.edu.itba.pod.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public abstract class CsvParserImpl implements CsvParser {

    @Override
    public void loadData(Path path) {
        try (Stream<String> stream = Files.lines(path)) {
            Iterator<String> iterator = stream.iterator();
            String line = iterator.next();
            assignIndexes(line);
            iterator.forEachRemaining(this::parseLine);
            processLastChunk();
        } catch (IOException e) {
            System.out.println("Error reading file " + path);
        }
    }

    protected abstract void assignIndexes(String headers);

    protected abstract void parseLine(String line);

    protected abstract void processLastChunk();
}
