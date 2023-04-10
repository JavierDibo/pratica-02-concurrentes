package org.example;
public class PrinterRunnable implements Runnable {
    private final MonitorImpresora monitorImpresora;
    private final int numPrintJobs;

    public PrinterRunnable(MonitorImpresora monitorImpresora, int numPrintJobs) {
        this.monitorImpresora = monitorImpresora;
        this.numPrintJobs = numPrintJobs;
    }

    @Override
    public void run() {
        while (monitorImpresora.getCompletedJobsCount().get() < numPrintJobs) {
            PrintJob job = monitorImpresora.getNextPrintJob();
            if (job != null) {
                System.out.println("Imprimiendo: " + job);
                try {
                    monitorImpresora.simulatePrintJob(job);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Impresión finalizada: " + job);
                monitorImpresora.jobCompleted();
            }
        }
    }
}