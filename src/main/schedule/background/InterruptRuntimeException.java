package main.schedule.background;

public class InterruptRuntimeException extends RuntimeException {
    public InterruptRuntimeException() {
        super();
    }

    public InterruptRuntimeException(String message) {
        super(message);
    }

    public InterruptRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptRuntimeException(Throwable cause) {
        super(cause);
    }
}
