package ssccdd.ujaen.practica2;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Javier Francisco Dibo Gómez
 */
public class MonitorImpresora {
    private final LinkedList<TrabajoImpresora> primario = new LinkedList<>();
    private final LinkedList<TrabajoImpresora> secundario = new LinkedList<>();
    private final ReentrantLock lockPrimario = new ReentrantLock();
    private final ReentrantLock lockSecundario = new ReentrantLock();
    private final Condition primarioNoVacio = lockPrimario.newCondition();
    private final Condition secundarioNoVacio = lockSecundario.newCondition();
    private final CountDownLatch latch;
    private final AtomicInteger completados = new AtomicInteger(0);

    public MonitorImpresora(int num) {
        this.latch = new CountDownLatch(num);
    }

    public void annadirTrabajo(TrabajoImpresora job) {
        if (primario.size() < TAMANNO_PRIMARIO) {
            lockPrimario.lock();
            try {
                primario.offer(job);
                primarioNoVacio.signal();
            } finally {
                lockPrimario.unlock();
            }
        } else {
            lockSecundario.lock();
            try {
                secundario.offer(job);
                secundarioNoVacio.signal();
            } finally {
                lockSecundario.unlock();
            }
        }
    }

    public Pair<String, TrabajoImpresora> siguienteTrabajo() {
        Pair<String, TrabajoImpresora> result = null;

        lockPrimario.lock();
        try {
            if (!primario.isEmpty()) {
                TrabajoImpresora job = primario.poll();
                result = new Pair<>("primario", job);
            }
        } finally {
            lockPrimario.unlock();
        }

        if (result == null) {
            lockSecundario.lock();
            try {
                if (!secundario.isEmpty()) {
                    TrabajoImpresora job = secundario.poll();
                    result = new Pair<>("secundario", job);
                }
            } finally {
                lockSecundario.unlock();
            }
        }

        return result;
    }

    public void imprimir(Pair<String, TrabajoImpresora> jobPair) throws InterruptedException {
        String tipo = jobPair.getKey();
        if ("primario".equals(tipo)) {
            Thread.sleep(DELAY_PRIMARIO);
        } else if ("secundario".equals(tipo)) {
            Thread.sleep(DELAY_SECUNDARIO);
        }
    }

    public void completado() {
        completados.incrementAndGet();
        latch.countDown();
    }

    public void esperar() throws InterruptedException {
        latch.await();
    }

    public AtomicInteger getCompletados() {
        return completados;
    }

    private static final int DELAY_PRIMARIO = 1000;
    private static final int DELAY_SECUNDARIO = 5000;
    private static final int TAMANNO_PRIMARIO = 5;
}
