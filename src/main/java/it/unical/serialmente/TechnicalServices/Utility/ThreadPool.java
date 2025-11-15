package it.unical.serialmente.TechnicalServices.Utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static ExecutorService executor;

    public static synchronized ExecutorService get() {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
        return executor;
    }

    public static synchronized void shutdown() {
        if(executor != null && !executor.isShutdown()) {
            executor.shutdown();
        };
        executor = null;
    }
}