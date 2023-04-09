package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Comienza la ejecución del hilo (PRINCIPAL)");

        final int NUM_PRINT_JOBS = 10;
        final int NUM_PRINTERS = 2;

        MonitorImpresora monitorImpresora = new MonitorImpresora(NUM_PRINT_JOBS);

        // Enviar trabajos de impresión
        for (int i = 0; i < NUM_PRINT_JOBS; i++) {
            PrintJob job = new PrintJob("Documento " + (i + 1), i + 1, i);
            monitorImpresora.addPrintJob(job);
        }

        // Asignar trabajos de impresión a las impresoras
        Thread[] printerThreads = new Thread[NUM_PRINTERS];
        for (int i = 0; i < NUM_PRINTERS; i++) {
            printerThreads[i] = new Thread(new PrinterRunnable(monitorImpresora));
            printerThreads[i].start();
        }

        // Esperar la finalización de todos los trabajos de impresión
        try {
            monitorImpresora.awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finaliza la ejecución del hilo (PRINCIPAL)");
    }


    private static class PrinterRunnable implements Runnable {
        private final MonitorImpresora monitorImpresora;

        public PrinterRunnable(MonitorImpresora monitorImpresora) {
            this.monitorImpresora = monitorImpresora;
        }

        @Override
        public void run() {
            while (true) {
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
                } else {
                    break;
                }
            }
        }
    }
}
