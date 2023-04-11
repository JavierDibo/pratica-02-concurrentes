package ssccdd.ujaen.practica2;

/**
 * @author Javier Francisco Dibo Gómez
 */
public class TrabajoImpresora {
    private final String hoja;
    private final int id;
    private final int ordenLlegada;

    public TrabajoImpresora(String hoja, int id, int ordenLlegada) {
        this.hoja = hoja;
        this.id = id;
        this.ordenLlegada = ordenLlegada;
    }

    public String getHoja() {
        return hoja;
    }

    public int getId() {
        return id;
    }

    public int getOrdenLlegada() {
        return ordenLlegada;
    }

    @Override
    public String toString() {
        return "TrabajoImpresora(" +
                "nombre='" + hoja + '\'' +
                ", jobId=" + id +
                ')';
    }
}
