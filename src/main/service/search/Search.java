package main.service.search;

import main.config.ConfigSetup;
import main.config.Schema;
import main.config.database.VersionOne;
import main.data.core.Action;
import main.data.error.ServerException;
import main.db.Connector;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;
import main.db.Statement;
import main.schedule.background.Threads;
import main.schedule.jobs.FileSystem;
import main.tool.CrapToMove;

import java.io.File;
import java.sql.*;

public class Search {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    public static void main(String[] args){
        long start = System.currentTimeMillis();

        if (args.length>=1)
            ConfigSetup.set(new File(args[0]));
        else
            ConfigSetup.set(new File("config.properties"));

        //db setup
        new VersionOne().setup();
        new Schema().apply();

        //threads
        Threads threads = new Threads();
        threads.add(new main.schedule.jobs.FileSystem(), 1 * MINUTE);

        try{
            for (;;) {}


        } finally {
            long end = System.currentTimeMillis();
            System.out.println("Run time: " + (end-start));
            threads.stop();
        }
    }
}
