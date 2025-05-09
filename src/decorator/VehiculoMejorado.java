package decorator;

public class VehiculoMejorado extends VehiculoDecorator {
    private static final double MEJORA_VELOCIDAD = 1.2; // 20% más rápido
    private static final double MEJORA_CAPACIDAD = 1.15; // 15% más capacidad
    private static final double MEJORA_CONSUMO = 0.9; // 10% menos consumo

    public VehiculoMejorado(IVehiculo vehiculo) {
        super(vehiculo);
    }

    @Override
    public int getVelocidad() {
        return (int) (vehiculo.getVelocidad() * MEJORA_VELOCIDAD);
    }

    @Override
    public int getCapacidad() {
        return (int) (vehiculo.getCapacidad() * MEJORA_CAPACIDAD);
    }

    @Override
    public int getConsumo() {
        return (int) (vehiculo.getConsumo() * MEJORA_CONSUMO);
    }

    @Override
    public int getPrecio() {
        return (int) (vehiculo.getPrecio() * 1.3); // 30% más caro
    }
} 