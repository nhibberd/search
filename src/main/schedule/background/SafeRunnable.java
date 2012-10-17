package main.schedule.background;

public class SafeRunnable implements Runnable {
    private final Runnable runnable;
    private final Handler handler;

    public SafeRunnable(Runnable runnable, Handler handler) {
        this.runnable = runnable;
        this.handler = handler;
    }

    public void run() {
        try {
          runnable.run();
        } catch (ThreadDeath ignore) {
        } catch (InterruptRuntimeException ignore) {
        } catch (Throwable t) {
          handler.handle(t);
        }
    }
}
