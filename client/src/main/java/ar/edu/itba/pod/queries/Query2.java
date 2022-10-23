package ar.edu.itba.pod.queries;

import ar.edu.itba.pod.collators.YearlyCountCollator;
import ar.edu.itba.pod.mappers.TypeOfDayMapper;
import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.reducers.SumReducerFactory;
import ar.edu.itba.pod.utils.Arguments;
import ar.edu.itba.pod.models.Triple;
import ar.edu.itba.pod.utils.TimeLog;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;

import java.util.List;
import java.util.Map;
public class Query2 extends GenericQuery<String, Triple>{

    public Query2(
            final HazelcastInstance hazelcastInstance,
            final Arguments arguments,
            final TimeLog timeLog
            ) {
        super(hazelcastInstance, arguments, timeLog);
    }

    @Override
    protected ICompletableFuture<List<Map.Entry<String, Triple>>> submit() {
        final Job<String, Reading> job = getJobFromReadingsList("q2");
        return job
                .mapper(new TypeOfDayMapper())
                .reducer(new SumReducerFactory())
                .submit(new YearlyCountCollator());
    }

    @Override
    protected String getHeaders() {
        return "Year;Weekdays_Count;Weekends_Count;Total_Count\n";
    }

    @Override
    protected String formatData(Map.Entry<String, Triple> entry) {
        Triple data = entry.getValue();
        return entry.getKey() + ";" + data.getWeekdaysCount() + ";" + data.getWeekendsCount() + ";" + data.getTotalCount() + "\n";
    }
}
