package org.example;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorImpresora {
    private final LinkedList<PrintJob> primaryStorage = new LinkedList<>();
    private final LinkedList<PrintJob> secondaryStorage = new LinkedList<>();
    private final ReentrantReadWriteLock storageLock = new ReentrantReadWriteLock();
    private static final int PRIMARY_STORAGE_DELAY = 100;
    private static final int SECONDARY_STORAGE_DELAY = 500;
    private final CountDownLatch latch;

    public MonitorImpresora(int numPrintJobs) {
        this.latch = new CountDownLatch(numPrintJobs);
    }

    public synchronized void addPrintJob(PrintJob job) {
        if (primaryStorage.size() < 5) {
            primaryStorage.offer(job);
        } else {
            secondaryStorage.offer(job);
        }
    }

    public synchronized PrintJob getNextPrintJob() {
        PrintJob job = null;
        if (!primaryStorage.isEmpty()) {
            job = primaryStorage.poll();
        } else if (!secondaryStorage.isEmpty()) {
            job = secondaryStorage.poll();
        }
        return job;
    }

    public void simulatePrintJob(PrintJob job) throws InterruptedException {
        long waitTime = primaryStorage.contains(job) ? PRIMARY_STORAGE_DELAY : SECONDARY_STORAGE_DELAY;
        Thread.sleep(waitTime);
    }

    public void jobCompleted() {
        latch.countDown();
    }

    public void awaitCompletion() throws InterruptedException {
        latch.await();
    }
}
