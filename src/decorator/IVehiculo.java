package decorator;

import game.Pedido;
import java.util.Calendar;
import java.util.List;

public interface IVehiculo {
    String getTipo();
    String getId();
    Pedido getPedidoAsignado();
    void asignarPedido(Pedido pedido);
    boolean estaDisponible();
    int getCapacidad();
    int getVelocidad();
    int getCostePorKm();
    int calcularTiempoEntrega(int distancia);
    boolean puedeTransportarTipo(String tipoPaquete);
    List<String> getTiposPaquetesPermitidos();
    int getSalud();
    int getDesgastePorViaje();
    void aplicarDesgaste();
    int calcularCosteReparacion();
    int reparar();
    String getNombre();
    int getCosteReparacion();
    int getConsumo();
    int getPrecio();
    void reducirSalud(int cantidad);
    Calendar getFechaEstimadaLlegada();
    Calendar getFechaDisponibilidad();
    void setFechaDisponibilidad(Calendar fechaDisponibilidad);
} 