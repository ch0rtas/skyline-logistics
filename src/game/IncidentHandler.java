package game;

import java.util.List;
import java.util.Random;

public class IncidentHandler {
    private List<Vehiculo> flota;
    private Random random;

    public IncidentHandler(List<Vehiculo> flota, Random random) {
        this.flota = flota;
        this.random = random;
    }

    /**
     * Resuelve un incidente para un pedido
     * @param pedido Pedido afectado
     */
    public void resolverIncidente(Pedido pedido) {
        IncidentResolver resolver = new IncidentResolver(flota, random);
        resolver.resolverIncidente(pedido);
    }
}