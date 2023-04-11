package ssccdd.ujaen.practica2;

public class Impresora implements Runnable {
    private final MonitorImpresora monitorImpresora;
    private final int numImpresiones;

    public Impresora(MonitorImpresora monitorImpresora, int numImpresiones) {
        this.monitorImpresora = monitorImpresora;
        this.numImpresiones = numImpresiones;
    }

    @Override
    public void run() {
        while (monitorImpresora.getCompletados().get() < numImpresiones) {
            Pair<String, TrabajoImpresora> jobPair = monitorImpresora.siguienteTrabajo();
            if (jobPair != null) {
                TrabajoImpresora job = jobPair.getValue();
                System.out.println("Imprimiendo: " + job );
                try {
                    monitorImpresora.imprimir(jobPair);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Fin: " + job);
                monitorImpresora.completado();
            }
        }
    }
}