package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Comienza la ejecución del hilo (PRINCIPAL)");

        final int NUM_PRINT_JOBS = 10;
        final int NUM_PRINTERS = 2;

        MonitorImpresora monitorImpresora = new MonitorImpresora(NUM_PRINT_JOBS);

        for (int i = 0; i < NUM_PRINT_JOBS; i++) {
            PrintJob job = new PrintJob("Documento " + (i + 1), i + 1, i);
            monitorImpresora.addPrintJob(job);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_PRINTERS);
        for (int i = 0; i < NUM_PRINTERS; i++) {
            executorService.execute(new PrinterRunnable(monitorImpresora, NUM_PRINT_JOBS));
        }

        try {
            monitorImpresora.awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

        System.out.println("Finaliza la ejecución del hilo (PRINCIPAL)");
    }
}
