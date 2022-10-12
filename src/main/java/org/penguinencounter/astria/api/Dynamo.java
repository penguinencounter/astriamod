package org.penguinencounter.astria.api;

import org.penguinencounter.astria.AstriaClient;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamo is a class that allows you to queue tasks for loading.
 */
public class Dynamo {
    private static final Logger LOGGER = AstriaClient.LOGGER;

    public static List<Runnable> tasks = new ArrayList<>();
    private static boolean running = true;

    /**
     * Queue a task for loading.
     */
    public static void enqueueTask(Runnable task) {
        queueTask(task, false);
    }

    /**
     * Queue a task for loading.
     * @param task The task to queue.
     * @param failSilently Don't throw an exception if the task fails to queue.
     */
    public static void queueTask(Runnable task, boolean failSilently) {
        if (!running) {
            if (!failSilently) throw new RuntimeException("Cannot queue task after loading has started.");
            return;
        }
        tasks.add(task);
    }

    private static class StupidWrapper {
        public boolean value = false;
    }
    public static void lock() {
        final StupidWrapper ref = new StupidWrapper();
        Thread.getAllStackTraces().forEach((thread, trace) -> {
            if (thread.getId() == AstriaClient.getInitializerThread().getId()) {
                ref.value = true;
                LOGGER.info("Dynamo: Detecting source...");
                /*
                    [08:22:15] [Render thread/INFO] (Astria Client) dynamo: dumping trace (first lines lower)
                    [08:22:15] [Render thread/INFO] (Astria Client) java.lang.Thread.dumpThreads:-2
                    [08:22:15] [Render thread/INFO] (Astria Client) java.lang.Thread.getAllStackTraces:1662
                    [08:22:15] [Render thread/INFO] (Astria Client) org.penguinencounter.astria.api.Dynamo.lock:43
                    [08:22:15] [Render thread/INFO] (Astria Client) org.penguinencounter.astria.AstriaClient.onInitializeClient:39  <- this one
                 */
                StackTraceElement element = null;
                for (int i = 0; i < trace.length; i++) {
                    element = trace[i];
                    LOGGER.info("Dynamo: [analyzing] " + element.toString());
                    if (element.getClassName().equals(Dynamo.class.getName()) && element.getMethodName().equals("lock")) {
                        LOGGER.info("Dynamo: lock() method call ...");
                        i ++;
                        element = trace[i];
                        break;
                    }
                }
                if (element == null) {
                    LOGGER.warn("Dynamo: bad stack trace");
                    return;
                }
                LOGGER.info("Dynamo: caller: " + element.getClassName() + "." + element.getMethodName() + ":" + element.getLineNumber());
                if (element.getClassName().equals(AstriaClient.class.getName())) {
                    running = false;
                    LOGGER.info("Dynamo: locked");
                    return;
                }
                LOGGER.warn("Dynamo: bad (untrusted) caller to lock()");
            }
        });
        if (!ref.value) {
            LOGGER.warn("Dynamo: no thread found???");
        }
    }

    public static boolean isRunning() {
        return running;
    }
}
