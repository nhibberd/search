package main.schedule.background;

import java.util.ArrayList;
import java.util.List;

public class Threads implements Stop {
    private final List<Stop> stoppers = new ArrayList<Stop>();
    private final List<Thread> threads = new ArrayList<Thread>();
    private final Handler logger = new LoggingHandler();

    public void stop() {
        for (Stop stopper : stoppers)
            stopper.stop();
        for (Thread thread : threads)
            thread.interrupt();
    }
    
    public void add(Runnable runnable, long delay) {
        SafeRunnable safe = new SafeRunnable(runnable, logger);
        RepeatableRunnable repeater = new RepeatableRunnable(safe, delay);
        Thread thread = new Thread(repeater);
        stoppers.add(repeater);
        threads.add(thread);
        thread.setDaemon(true);
        thread.start();
    }
}
