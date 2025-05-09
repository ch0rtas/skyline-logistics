package decorator;

public class VehiculoResistente extends VehiculoDecorator {
    private static final double REDUCCION_DESGASTE = 0.7; // 30% menos desgaste
    private static final double REDUCCION_COSTE_REPARACION = 0.8; // 20% menos coste de reparación

    public VehiculoResistente(IVehiculo vehiculo) {
        super(vehiculo);
    }

    @Override
    public int getDesgastePorViaje() {
        return (int) (vehiculo.getDesgastePorViaje() * REDUCCION_DESGASTE);
    }

    @Override
    public int calcularCosteReparacion() {
        return (int) (vehiculo.calcularCosteReparacion() * REDUCCION_COSTE_REPARACION);
    }

    @Override
    public void reducirSalud(int cantidad) {
        vehiculo.reducirSalud((int) (cantidad * REDUCCION_DESGASTE));
    }

    @Override
    public int getPrecio() {
        return (int) (vehiculo.getPrecio() * 1.2); // 20% más caro
    }
} 