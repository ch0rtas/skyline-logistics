package game;

import java.util.List;
import java.util.Random;
import decorator.IVehiculo;

public class IncidentResolver {
    private List<IVehiculo> flota;
    private Random random;

    public IncidentResolver(List<IVehiculo> flota, Random random) {
        this.flota = flota;
        this.random = random;
    }

    public void resolverIncidente(Pedido pedido) {
        System.out.println("⚠️ Se ha producido un incidente con el pedido " + pedido.getId());

        IVehiculo vehiculo = flota.stream()
            .filter(v -> v.getId().equals(pedido.getTransporteAsignado().split(" ")[1]))
            .findFirst()
            .orElse(null);

        if (vehiculo == null) {
            System.out.println("❌ No se encontró el vehículo asignado al pedido");
            return;
        }

        int opcion = random.nextInt(3);
        switch (opcion) {
            case 0:
                System.out.println("✅ El incidente se resolvió automáticamente");
                break;
            case 1:
                System.out.println("❌ El vehículo sufrió daños y el pedido se retrasará");
                vehiculo.reducirSalud(10);
                pedido.incrementarDiasRestantes(1);
                break;
            case 2:
                System.out.println("❌ El pedido se perdió debido al incidente");
                flota.remove(vehiculo);
                break;
        }
    }
}