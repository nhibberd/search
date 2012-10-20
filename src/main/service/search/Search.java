package main.service.search;

import main.config.ConfigSetup;
import main.config.Schema;
import main.config.database.VersionOne;
import main.schedule.background.Threads;
import main.schedule.jobs.Crawler;
import main.schedule.jobs.Indexing;
import main.schedule.jobs.Links;
import main.schedule.jobs.Ranking;

import java.io.File;
import static main.data.state.Params.records;

public class Search {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    public static void main(String[] args){
        long start = System.currentTimeMillis();

        if (args.length>=1) {
            //program arguments
            ConfigSetup.set(new File(args[0]));
            records.put("dir", args[1]);
        }
        else
            ConfigSetup.set(new File("config.properties"));

        //db setup
        new VersionOne().setup();
        new Schema().apply();

        //threads
        Threads threads = new Threads();
        threads.add(new Crawler(), 10 * SECOND);
        threads.add(new Links(), 10 * SECOND);
        threads.add(new Indexing(), 10 * SECOND);
        threads.add(new Ranking(), 10 * SECOND);

        try{

            for (;;) {}


        } finally {
            long end = System.currentTimeMillis();
            System.out.println("Run time: " + (end-start));
            threads.stop();
        }
    }
}
