package main.schedule.background;

public class RepeatableRunnable implements Runnable, Stop {
    private final Runnable runnable;
    private final long delay;
    private volatile boolean run = true;

    public RepeatableRunnable(Runnable runnable, long delay) {
        this.runnable = runnable;
        this.delay = delay;
    }

    public void stop() {
        run = false;
    }

    public void run() {
        while (run) {
            runnable.run();
            rest();
        }
    }

    private void rest() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new InterruptRuntimeException(e);
        }
    }
}
