package it.unical.serialmente.TechnicalServices.Utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    public static ExecutorService get() {
        return executor;
    }

    public static void shutdown() {
        executor.shutdownNow();
    }
}