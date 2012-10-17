package main.data.core;

public interface ResultFunction<A, B> {
    Result<B> apply(A a);
}
