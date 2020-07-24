package ir.ac.kntu.Technical.Other.CustomRunnable;

/**
 * used to  load "run" which gives multi objects as input
 *
 * @param <T> generic
 */
public interface Runnable_MultiArg<T> {
    void run(T... object);
}