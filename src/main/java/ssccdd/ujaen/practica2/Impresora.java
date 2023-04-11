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
            ImpresoraJob job = monitorImpresora.siguienteTrabajo();
            if (job != null) {
                System.out.println("Imprimiendo: " + job);
                try {
                    monitorImpresora.imprimir(job);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Impresión finalizada: " + job);
                monitorImpresora.completado();
            }
        }
    }
}