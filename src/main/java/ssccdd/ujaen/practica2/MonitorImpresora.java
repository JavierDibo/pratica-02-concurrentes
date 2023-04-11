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
    private final LinkedList<ImpresoraJob> primario = new LinkedList<>();
    private final LinkedList<ImpresoraJob> secundario = new LinkedList<>();
    private final ReentrantLock lockPrimario = new ReentrantLock();
    private final ReentrantLock lockSecundario = new ReentrantLock();
    private final Condition primarioNoVacio = lockPrimario.newCondition();
    private final Condition secundarioNoVacio = lockSecundario.newCondition();
    private static final int DELAY_PRIMARIO = 100;
    private static final int DELAY_SECUNDARIO = 500;
    private final CountDownLatch latch;
    private final AtomicInteger completados = new AtomicInteger(0);

    public MonitorImpresora(int num) {
        this.latch = new CountDownLatch(num);
    }

    public void annadirTrabajo(ImpresoraJob job) {
        if (primario.size() < 5) {
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

    public ImpresoraJob siguienteTrabajo() {
        ImpresoraJob job = null;

        lockPrimario.lock();
        try {
            if (!primario.isEmpty()) {
                job = primario.poll();
            }
        } finally {
            lockPrimario.unlock();
        }

        if (job == null) {
            lockSecundario.lock();
            try {
                if (!secundario.isEmpty()) {
                    job = secundario.poll();
                }
            } finally {
                lockSecundario.unlock();
            }
        }

        return job;
    }

    public void imprimir(ImpresoraJob job) throws InterruptedException {
        if (primario.contains(job)) {
            Thread.sleep(DELAY_PRIMARIO);
        } else if (secundario.contains(job)) {
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
}
