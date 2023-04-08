package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class PrinterRunnable implements Runnable {
    private MonitorImpresora monitorImpresora;
    private AtomicInteger remainingPrintJobs;

    public PrinterRunnable(MonitorImpresora monitorImpresora, AtomicInteger remainingPrintJobs) {
        this.monitorImpresora = monitorImpresora;
        this.remainingPrintJobs = remainingPrintJobs;
    }

    @Override
    public void run() {
        while (remainingPrintJobs.get() > 0) {
            PrintJob job = monitorImpresora.getNextPrintJob();
            if (job != null) {
                System.out.println("Imprimiendo: " + job);
                try {
                    monitorImpresora.simulatePrintJob(job);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Impresión finalizada: " + job);
                remainingPrintJobs.decrementAndGet();
            }
        }
    }
}
