package main.service.search;

import main.config.ConfigSetup;
import main.config.Schema;
import main.config.database.VersionOne;
import main.schedule.background.Threads;
import main.schedule.jobs.Crawler;
import main.schedule.jobs.Links;

import java.io.File;

import static main.data.state.Params.records;

public class Setup {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    public static void main(String[] args){
        long crawlPoll = 30 * SECOND;

        //program arguments
        if (args.length >= 0)
            ConfigSetup.set(new File(args[0]));
        else
            ConfigSetup.set(new File("config.properties"));

        if (args.length >= 1)
            records.put("dir", args[1]);
        if (args.length >= 2)
            crawlPoll = Long.valueOf(args[2]);


        //db setup
        new VersionOne().setup();
        new Schema().apply();


        //threads
        Threads threads = new Threads();
        threads.add(new Crawler(), crawlPoll);
        threads.add(new Links(), 60 * SECOND);

        try{
            for (;;) {}

        } finally {
            threads.stop();
        }
    }
}
