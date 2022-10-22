package ar.edu.itba.pod.constants;

import ar.edu.itba.pod.exceptions.InvalidArgumentsException;

public enum Queries {
    QUERY_1(1),
    QUERY_2(2),
    QUERY_3(3),
    QUERY_4(4),
    QUERY_5(5);

    private final int id;
    private final String timeFile;
    private final String queryFile;

    Queries(int id){
        this.id = id;
        this.timeFile = "time" + id + ".txt";
        this.queryFile = "query" + id + ".csv";
    }

    public static Queries getFromId(int id) throws InvalidArgumentsException {
        for (Queries q : Queries.values()) {
            if (q.id == id) {
                return q;
            }
        }
        throw new InvalidArgumentsException("Error: invalid argument for query");
    }

    public int getId() {
        return id;
    }

    public String getTimeFile() {
        return timeFile;
    }

    public String getQueryFile() {
        return queryFile;
    }
}
