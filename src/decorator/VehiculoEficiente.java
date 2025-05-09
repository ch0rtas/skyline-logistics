package decorator;

public class VehiculoEficiente extends VehiculoDecorator {
    private static final double REDUCCION_CONSUMO = 0.7; // 30% menos consumo
    private static final double REDUCCION_COSTE_KM = 0.8; // 20% menos coste por km

    public VehiculoEficiente(IVehiculo vehiculo) {
        super(vehiculo);
    }

    @Override
    public int getConsumo() {
        return (int) (vehiculo.getConsumo() * REDUCCION_CONSUMO);
    }

    @Override
    public int getCostePorKm() {
        return (int) (vehiculo.getCostePorKm() * REDUCCION_COSTE_KM);
    }

    @Override
    public int getPrecio() {
        return (int) (vehiculo.getPrecio() * 1.15); // 15% más caro
    }
} 