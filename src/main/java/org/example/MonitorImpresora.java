package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MonitorImpresora {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Condition primaryQueueNotEmpty = rwLock.writeLock().newCondition();
    private final Condition secondaryQueueNotEmpty = rwLock.writeLock().newCondition();

    private Queue<PrintJob> primaryStorage = new LinkedList<>();
    private Queue<PrintJob> secondaryStorage = new LinkedList<>();

    private static final int PRIMARY_STORAGE_CAPACITY = 5;
    private static final int PRIMARY_STORAGE_DELAY = 100;
    private static final int SECONDARY_STORAGE_DELAY = 500;

    public void addPrintJob(PrintJob job) {
        rwLock.writeLock().lock();
        try {
            if (primaryStorage.size() < PRIMARY_STORAGE_CAPACITY) {
                primaryStorage.add(job);
                primaryQueueNotEmpty.signal();
            } else {
                secondaryStorage.add(job);
                secondaryQueueNotEmpty.signal();
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public PrintJob getNextPrintJob() {
        rwLock.writeLock().lock();
        try {
            while (primaryStorage.isEmpty() && secondaryStorage.isEmpty()) {
                primaryQueueNotEmpty.await();
            }

            PrintJob primaryJob = primaryStorage.peek();
            PrintJob secondaryJob = secondaryStorage.peek();

            if (primaryJob != null && (secondaryJob == null || primaryJob.getArrivalOrder() < secondaryJob.getArrivalOrder())) {
                primaryStorage.poll();
                primaryQueueNotEmpty.signalAll();
                return primaryJob;
            } else if (secondaryJob != null) {
                secondaryStorage.poll();
                secondaryQueueNotEmpty.signalAll();
                return secondaryJob;
            } else {
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            rwLock.writeLock().unlock();
        }
    }


    public void simulatePrintJob(PrintJob job) throws InterruptedException {
        if (primaryStorage.contains(job)) {
            Thread.sleep(PRIMARY_STORAGE_DELAY);
        } else {
            Thread.sleep(SECONDARY_STORAGE_DELAY);
        }
    }
}
