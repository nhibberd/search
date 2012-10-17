package main.service.search;

import main.schedule.background.Threads;
import main.schedule.jobs.FileSystem;

public class Search {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    public static void main(String[] args){
        long start = System.currentTimeMillis();
        
        //new VersionOne().seutp();
        
        Threads threads = new Threads();
        //threads.add(new main.schedule.jobs.FileSystem(), 1 * SECOND);


        /*

        try{
            //something


        } finally {
            threads.stop();
        }             */
        FileSystem z = new FileSystem();
        z.run();

        long end = System.currentTimeMillis();
        System.out.println("Run time: " + (end-start));
    }
}
