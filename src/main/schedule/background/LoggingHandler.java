package main.schedule.background;

public class LoggingHandler implements Handler {
    public void handle(Throwable t) {
        System.err.println(t.getClass().getSimpleName() + ": " + t.getMessage());
    }
}
