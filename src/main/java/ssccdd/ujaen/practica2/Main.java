package ssccdd.ujaen.practica2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Javier Francisco Dibo Gómez
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Comienza la ejecución del hilo (PRINCIPAL)");

        int numImpresiones = 10;
        int numImpresoras = 2;

        MonitorImpresora monitorImpresora = new MonitorImpresora(numImpresiones);

        for (int i = 0; i < numImpresiones; i++) {
            ImpresoraJob job = new ImpresoraJob("Hoja " + (i + 1), i + 1, i);
            monitorImpresora.annadirTrabajo(job);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numImpresoras);
        for (int i = 0; i < numImpresoras; i++) {
            executorService.execute(new Impresora(monitorImpresora, numImpresiones));
        }

        try {
            monitorImpresora.esperar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

        System.out.println("Finaliza la ejecución del hilo (PRINCIPAL)");
    }
}
