package ar.edu.itba.pod.constants;

import ar.edu.itba.pod.exceptions.InvalidArgumentsException;

public enum Query {
    QUERY_1(1),
    QUERY_2(2),
    QUERY_3(3),
    QUERY_4(4),
    QUERY_5(5);

    private final int id;

    Query(int id){
        this.id = id;
    }

    public static Query getFromId(int id) throws InvalidArgumentsException {
        for (Query q : Query.values()) {
            if (q.id == id) {
                return q;
            }
        }
        throw new InvalidArgumentsException("Error: invalid argument for query");
    }
}
