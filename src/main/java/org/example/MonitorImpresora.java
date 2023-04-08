package org.example;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


public class MonitorImpresora {
    private final LinkedBlockingQueue<PrintJob> primaryStorage = new LinkedBlockingQueue<>(5);
    private final LinkedBlockingQueue<PrintJob> secondaryStorage = new LinkedBlockingQueue<>();
    private final ReentrantLock primaryStorageLock = new ReentrantLock();
    private final ReentrantLock secondaryStorageLock = new ReentrantLock();
    private static final int PRIMARY_STORAGE_DELAY = 100;
    private static final int SECONDARY_STORAGE_DELAY = 500;

    public void addPrintJob(PrintJob job) {
        primaryStorageLock.lock();
        try {
            if (!primaryStorage.offer(job)) {
                secondaryStorageLock.lock();
                try {
                    secondaryStorage.offer(job);
                } finally {
                    secondaryStorageLock.unlock();
                }
            }
        } finally {
            primaryStorageLock.unlock();
        }
    }

    public PrintJob getNextPrintJob() {
        PrintJob primaryJob = null;
        PrintJob secondaryJob = null;

        primaryStorageLock.lock();
        try {
            primaryJob = primaryStorage.poll();
        } finally {
            primaryStorageLock.unlock();
        }

        secondaryStorageLock.lock();
        try {
            secondaryJob = secondaryStorage.poll();
        } finally {
            secondaryStorageLock.unlock();
        }

        if (primaryJob != null) {
            if (secondaryJob == null || primaryJob.getArrivalOrder() < secondaryJob.getArrivalOrder()) {
                return primaryJob;
            }
            secondaryStorageLock.lock();
            try {
                secondaryStorage.offer(secondaryJob);
            } finally {
                secondaryStorageLock.unlock();
            }
        } else if (secondaryJob != null) {
            return secondaryJob;
        }

        return null;
    }


    public void simulatePrintJob(PrintJob job) throws InterruptedException {
        if (primaryStorage.contains(job)) {
            Thread.sleep(PRIMARY_STORAGE_DELAY);
        } else {
            Thread.sleep(SECONDARY_STORAGE_DELAY);
        }
    }
}
