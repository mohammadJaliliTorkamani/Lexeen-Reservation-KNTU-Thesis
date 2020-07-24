package ir.ac.kntu.Technical.Other.CustomRunnable;

/**
 * used to  load "run" which gives one object as input
 *
 * @param <T> generic
 */
public interface Runnable_SingleArg<T> {
    void run(T object);
}