package ssccdd.ujaen.practica2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Javier Francisco Dibo Gómez
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Comienza la ejecución del hilo (PRINCIPAL)");

        int numHojas = 20;
        int numImpresoras = 1;

        MonitorImpresora monitorImpresora = new MonitorImpresora(numHojas);

        for (int i = 0; i < numHojas; i++) {
            TrabajoImpresora job = new TrabajoImpresora("Hoja " + (i + 1), i + 1, i);
            monitorImpresora.annadirTrabajo(job);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numImpresoras);
        for (int i = 0; i < numImpresoras; i++) {
            executorService.execute(new Impresora(monitorImpresora, numHojas));
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
