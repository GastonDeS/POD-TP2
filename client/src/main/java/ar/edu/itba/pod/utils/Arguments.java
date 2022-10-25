package ar.edu.itba.pod.utils;

import ar.edu.itba.pod.constants.Queries;
import ar.edu.itba.pod.exceptions.InvalidArgumentsException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class Arguments {
    private Queries query;
    private Collection<String> addresses;
    private String inPath;
    private String outPath;
    private int min;
    private int n;
    private int year;

    private static final String QUERY_KEY = "query";
    private static final String ADDRESSES_KEY = "addresses";
    private static final String IN_PATH_KEY = "inPath";
    private static final String OUT_PATH_KEY = "outPath";
    private static final String MIN_KEY = "min";
    private static final String N_KEY = "n";
    private static final String YEAR_KEY = "year";

    public void parse() throws InvalidArgumentsException {
        Properties props = System.getProperties();

        // Required arguments
        if (!props.containsKey(QUERY_KEY)) {
            throw new InvalidArgumentsException("Error: invalid query argument");
        }
        if (!props.containsKey(ADDRESSES_KEY)) {
            throw new InvalidArgumentsException("Error: invalid addresses argument");
        }
        if (!props.containsKey(IN_PATH_KEY)) {
            throw new InvalidArgumentsException("Error: invalid in path argument");
        }
        if (!props.containsKey(OUT_PATH_KEY)) {
            throw new InvalidArgumentsException("Error: invalid out path argument");
        }

        this.query = Queries.getFromId(Integer.parseInt(props.getProperty(QUERY_KEY)));
        this.addresses = Arrays.asList(props.getProperty(ADDRESSES_KEY).split(";"));
        this.inPath = props.getProperty(IN_PATH_KEY);
        this.outPath = props.getProperty(OUT_PATH_KEY);

        if (query.equals(Queries.QUERY_3)) {
            parseQuery3(props);
        }

        if (query.equals(Queries.QUERY_4)) {
            parseQuery4(props);
        }
    }

    private void parseQuery3(Properties props) throws InvalidArgumentsException {
        if (!props.containsKey(MIN_KEY)) {
            throw new InvalidArgumentsException("Error: invalid min argument");
        }

        this.min = Integer.parseInt(props.getProperty(MIN_KEY));
    }

    private void parseQuery4(Properties props) throws InvalidArgumentsException {
        if (!props.containsKey(N_KEY)) {
            throw new InvalidArgumentsException("Error: invalid n argument");
        }
        if (!props.containsKey(YEAR_KEY)) {
            throw new InvalidArgumentsException("Error: invalid year argument");
        }

        this.n = Integer.parseInt(props.getProperty(N_KEY));
        this.year = Integer.parseInt(props.getProperty(YEAR_KEY));
    }

    /*
        GETTERS
     */

    public Queries getQuery() {
        return query;
    }

    public Collection<String> getAddresses() {
        return addresses;
    }

    public String getInPath() {
        return inPath;
    }

    public String getOutPath() {
        return outPath;
    }

    public int getMin() {
        return min;
    }

    public int getN() {
        return n;
    }

    public int getYear() {
        return year;
    }

    /*
        SETTERS
     */

    public void setQuery(Queries query) {
        this.query = query;
    }
}
