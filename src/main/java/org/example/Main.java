package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        System.out.println("Comienza la ejecución del hilo (PRINCIPAL)");

        final int NUM_PRINT_JOBS = 10;
        final int NUM_PRINTERS = 2;

        MonitorImpresora monitorImpresora = new MonitorImpresora();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_PRINTERS);
        AtomicInteger remainingPrintJobs = new AtomicInteger(NUM_PRINT_JOBS);

        // Enviar trabajos de impresión
        // Enviar trabajos de impresión
        for (int i = 0; i < NUM_PRINT_JOBS; i++) {
            PrintJob job = new PrintJob("Documento " + (i + 1), i + 1, i);
            monitorImpresora.addPrintJob(job);
        }


        // Asignar trabajos de impresión a las impresoras
        for (int i = 0; i < NUM_PRINTERS; i++) {
            executorService.execute(new PrinterRunnable(monitorImpresora, remainingPrintJobs));
        }

        // Cerrar el ExecutorService cuando todos los trabajos de impresión hayan sido procesados
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        System.out.println("Finaliza la ejecución del hilo (PRINCIPAL)");
    }
}
