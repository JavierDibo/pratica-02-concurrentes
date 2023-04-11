package ssccdd.ujaen.practica2;

/**
 * @author Javier Francisco Dibo Gómez
 */
public class ImpresoraJob {
    private final String hoja;
    private final int jobId;
    private final int ordenLlegada;

    public ImpresoraJob(String hoja, int jobId, int ordenLlegada) {
        this.hoja = hoja;
        this.jobId = jobId;
        this.ordenLlegada = ordenLlegada;
    }

    public String getHoja() {
        return hoja;
    }

    public int getJobId() {
        return jobId;
    }

    public int getOrdenLlegada() {
        return ordenLlegada;
    }

    @Override
    public String toString() {
        return "ImpresoraJob{" +
                "nombre='" + hoja + '\'' +
                ", jobId=" + jobId +
                '}';
    }
}
