package ssccdd.ujaen.practica2;

import java.util.concurrent.atomic.AtomicInteger;

public class Impresora implements Runnable {
    private final MonitorImpresora monitorImpresora;
    private final int numImpresiones;
    private final int id;
    private static AtomicInteger idGenerator = new AtomicInteger(0);

    public Impresora(MonitorImpresora monitorImpresora, int numImpresiones) {
        this.monitorImpresora = monitorImpresora;
        this.numImpresiones = numImpresiones;
        this.id = idGenerator.incrementAndGet();
    }
    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while (monitorImpresora.getCompletados().get() < numImpresiones) {
            Pair<String, TrabajoImpresora> trabajo = monitorImpresora.siguienteTrabajo();
            if (trabajo != null) {
                TrabajoImpresora job = trabajo.getValue();
                System.out.println("Impresora " + id + " - Imprimiendo: " + job);
                try {
                    monitorImpresora.imprimir(trabajo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Impresora " + id + " - Fin: " + job);
                monitorImpresora.completado();
            }
        }
    }
}