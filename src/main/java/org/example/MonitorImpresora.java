package org.example;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorImpresora {
    private final LinkedList<PrintJob> primaryStorage = new LinkedList<>();
    private final LinkedList<PrintJob> secondaryStorage = new LinkedList<>();
    private final ReentrantLock primaryStorageLock = new ReentrantLock();
    private final ReentrantLock secondaryStorageLock = new ReentrantLock();
    private final Condition primaryStorageNotEmpty = primaryStorageLock.newCondition();
    private final Condition secondaryStorageNotEmpty = secondaryStorageLock.newCondition();
    private static final int PRIMARY_STORAGE_DELAY = 100;
    private static final int SECONDARY_STORAGE_DELAY = 500;
    private final CountDownLatch latch;
    private final AtomicInteger completedJobsCount = new AtomicInteger(0);

    public MonitorImpresora(int numPrintJobs) {
        this.latch = new CountDownLatch(numPrintJobs);
    }

    public void addPrintJob(PrintJob job) {
        if (primaryStorage.size() < 5) {
            primaryStorageLock.lock();
            try {
                primaryStorage.offer(job);
                primaryStorageNotEmpty.signal();
            } finally {
                primaryStorageLock.unlock();
            }
        } else {
            secondaryStorageLock.lock();
            try {
                secondaryStorage.offer(job);
                secondaryStorageNotEmpty.signal();
            } finally {
                secondaryStorageLock.unlock();
            }
        }
    }

    public PrintJob getNextPrintJob() {
        PrintJob job = null;

        primaryStorageLock.lock();
        try {
            if (!primaryStorage.isEmpty()) {
                job = primaryStorage.poll();
            }
        } finally {
            primaryStorageLock.unlock();
        }

        if (job == null) {
            secondaryStorageLock.lock();
            try {
                if (!secondaryStorage.isEmpty()) {
                    job = secondaryStorage.poll();
                }
            } finally {
                secondaryStorageLock.unlock();
            }
        }

        return job;
    }

    public void simulatePrintJob(PrintJob job) throws InterruptedException {
        if (primaryStorage.contains(job)) {
            Thread.sleep(PRIMARY_STORAGE_DELAY);
        } else if (secondaryStorage.contains(job)) {
            Thread.sleep(SECONDARY_STORAGE_DELAY);
        }
    }

    public void jobCompleted() {
        completedJobsCount.incrementAndGet();
        latch.countDown();
    }

    public void awaitCompletion() throws InterruptedException {
        latch.await();
    }

    public AtomicInteger getCompletedJobsCount() {
        return completedJobsCount;
    }
}
