package main.data.core;

public interface ResultFunction2<A, B, C> {
    Result<C> apply(A a, B b);
}
