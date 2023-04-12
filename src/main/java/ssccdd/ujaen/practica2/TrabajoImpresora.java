package ssccdd.ujaen.practica2;

import java.util.Random;

/**
 * @author Javier Francisco Dibo Gómez
 */
public class TrabajoImpresora {
    private final String nombre;
    private final int id;
    private final int ordenLlegada;

    public TrabajoImpresora(String nombre, int id, int ordenLlegada) {
        this.nombre = nombre;
        this.id = id;
        this.ordenLlegada = ordenLlegada;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public int getOrdenLlegada() {
        return ordenLlegada;
    }

    public static String generaNombres() {
        String[] nombres = {
                "El increible libro de los chistes malos",
                "La historia jamas contada de la tortuga y la liebre",
                "Los secretos de la cocina de un unicornio",
                "Viaje al centro de la lavadora",
                "El ataque de los tomates mutantes",
                "Como domesticar a tu dragon de compañia",
                "El dia que los lapices de colores tomaron el control",
                "La odisea del calcetín perdido",
                "El gato con botas y pantalones",
                "El abecedario de las frutas ninja",
                "El club secreto de las plantas carnívoras",
                "La rebelión de las letras minúsculas",
                "La liga de los superheroes vegetarianos",
                "La vida secreta de los objetos inanimados",
                "El elefante que aprendió a bailar ballet",
                "La invasión de las hormigas gigantes",
                "El diario de un zombie optimista",
                "El misterioso caso del queso desaparecido"
        };

        Random random = new Random();
        int indice = random.nextInt(nombres.length);
        return nombres[indice];
    }


    @Override
    public String toString() {
        return "TrabajoImpresora(" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                ')';
    }
}
