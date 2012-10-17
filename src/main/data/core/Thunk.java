package main.data.core;

public interface Thunk<A> {
    A apply();
}
