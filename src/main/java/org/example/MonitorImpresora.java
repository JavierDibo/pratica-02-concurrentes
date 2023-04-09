package org.example;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorImpresora {
    private final LinkedBlockingQueue<PrintJob> primaryStorage = new LinkedBlockingQueue<>(5);
    private final LinkedBlockingQueue<PrintJob> secondaryStorage = new LinkedBlockingQueue<>();
    private final ReadWriteLock storageLock = new ReentrantReadWriteLock();
    private static final int PRIMARY_STORAGE_DELAY = 100;
    private static final int SECONDARY_STORAGE_DELAY = 500;

    public void addPrintJob(PrintJob job) {
        storageLock.writeLock().lock();
        try {
            if (!primaryStorage.offer(job)) {
                secondaryStorage.offer(job);
            }
        } finally {
            storageLock.writeLock().unlock();
        }
    }

    public PrintJob getNextPrintJob() {
        PrintJob primaryJob = null;
        PrintJob secondaryJob = null;

        storageLock.readLock().lock();
        try {
            primaryJob = primaryStorage.poll();
            if (secondaryStorage.size() > 0) {
                secondaryJob = secondaryStorage.poll();
            }

            if (primaryJob != null) {
                if (secondaryJob == null || primaryJob.getArrivalOrder() < secondaryJob.getArrivalOrder()) {
                    if (secondaryJob != null) {
                        secondaryStorage.offer(secondaryJob);
                    }
                    return primaryJob;
                }
                primaryStorage.offer(primaryJob);
            } else if (secondaryJob != null) {
                return secondaryJob;
            }
        } finally {
            storageLock.readLock().unlock();
        }

        return null;
    }

    public void simulatePrintJob(PrintJob job) throws InterruptedException {
        long waitTime = primaryStorage.contains(job) ? PRIMARY_STORAGE_DELAY : SECONDARY_STORAGE_DELAY;
        Thread.sleep(waitTime);
    }
}
