package ssccdd.ujaen.practica2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Javier Francisco Dibo Gómez
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Comienza la impresion");

        int numHojas = 40;
        int numImpresoras = 3;

        MonitorImpresora monitorImpresora = new MonitorImpresora(numHojas);

        for (int i = 0; i < numHojas; i++) {
            String nombre = TrabajoImpresora.generaNombres();
            TrabajoImpresora job = new TrabajoImpresora(nombre, i, i);
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

        System.out.println("Se ha impreso todo");
    }
}
