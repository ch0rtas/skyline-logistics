package decorator;

import game.Pedido;
import java.util.Calendar;
import java.util.List;

public abstract class VehiculoDecorator implements IVehiculo {
    protected IVehiculo vehiculo;

    public VehiculoDecorator(IVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String getTipo() {
        return vehiculo.getTipo();
    }

    @Override
    public String getId() {
        return vehiculo.getId();
    }

    @Override
    public Pedido getPedidoAsignado() {
        return vehiculo.getPedidoAsignado();
    }

    @Override
    public void asignarPedido(Pedido pedido) {
        vehiculo.asignarPedido(pedido);
    }

    @Override
    public boolean estaDisponible() {
        return vehiculo.estaDisponible();
    }

    @Override
    public int getCapacidad() {
        return vehiculo.getCapacidad();
    }

    @Override
    public int getVelocidad() {
        return vehiculo.getVelocidad();
    }

    @Override
    public int getCostePorKm() {
        return vehiculo.getCostePorKm();
    }

    @Override
    public int calcularTiempoEntrega(int distancia) {
        return vehiculo.calcularTiempoEntrega(distancia);
    }

    @Override
    public boolean puedeTransportarTipo(String tipoPaquete) {
        return vehiculo.puedeTransportarTipo(tipoPaquete);
    }

    @Override
    public List<String> getTiposPaquetesPermitidos() {
        return vehiculo.getTiposPaquetesPermitidos();
    }

    @Override
    public int getSalud() {
        return vehiculo.getSalud();
    }

    @Override
    public int getDesgastePorViaje() {
        return vehiculo.getDesgastePorViaje();
    }

    @Override
    public void aplicarDesgaste() {
        vehiculo.aplicarDesgaste();
    }

    @Override
    public int calcularCosteReparacion() {
        return vehiculo.calcularCosteReparacion();
    }

    @Override
    public int reparar() {
        return vehiculo.reparar();
    }

    @Override
    public String getNombre() {
        return vehiculo.getNombre();
    }

    @Override
    public int getCosteReparacion() {
        return vehiculo.getCosteReparacion();
    }

    @Override
    public int getConsumo() {
        return vehiculo.getConsumo();
    }

    @Override
    public int getPrecio() {
        return vehiculo.getPrecio();
    }

    @Override
    public void reducirSalud(int cantidad) {
        vehiculo.reducirSalud(cantidad);
    }

    @Override
    public Calendar getFechaEstimadaLlegada() {
        return vehiculo.getFechaEstimadaLlegada();
    }

    @Override
    public Calendar getFechaDisponibilidad() {
        return vehiculo.getFechaDisponibilidad();
    }

    @Override
    public void setFechaDisponibilidad(Calendar fechaDisponibilidad) {
        vehiculo.setFechaDisponibilidad(fechaDisponibilidad);
    }
} 