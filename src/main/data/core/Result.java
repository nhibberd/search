package main.data.core;

import main.data.error.ServerException;
public class Result<T>{
    private final Status status;
    private final String error;
    private final T value;

    public Result(Status status, T value, String error) {
        this.status = status;
        this.error = error;
        this.value = value;
    }

    public static <T> Result<T> ok(T t) { return new Result<T>(Status.OK, t, null); }
    public static <T> Result<T> notfound() { return new Result<T>(Status.NOT_FOUND, null, null); }
    public static <T> Result<T> notauth() { return new Result<T>(Status.NOT_AUTH, null, null); }
    public static <T> Result<T> error(String message) { return new Result<T>(Status.BAD_REQUEST, null, message); }

    public Status status() {
        return status;
    }

    public T value() {
        if (status()==Status.OK)
            return value;
        throw new ServerException();
    }

    public Boolean statusOK(){ //has value
        return status==Status.OK;
    }

    public String error() {
        if (status()!=Status.OK)
            return error;
        throw new ServerException();
    }

}