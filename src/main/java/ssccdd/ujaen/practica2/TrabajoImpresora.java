package ssccdd.ujaen.practica2;

import java.util.Random;

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

    public static String nombresDeCuentos(int id) {
        String[] titulos = {
                "El increible libro de los chistes malos",
                "La historia jamas contada de la tortuga y la liebre",
                "Los secretos de la cocina de un unicornio",
                "Viaje al centro de la lavadora",
                "El ataque de los tomates mutantes",
                "Como domesticar a tu dragon de compañia",
                "El dia que los lapices de colores tomaron el control"
        };

        Random random = new Random();
        int indice = random.nextInt(titulos.length);
        return titulos[indice];
    }

    @Override
    public String toString() {
        return "TrabajoImpresora(" +
                "nombre='" + hoja + '\'' +
                ", id=" + id +
                ')';
    }
}
