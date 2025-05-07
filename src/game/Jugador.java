package game;

import game.state.EstadoJugador;
import game.state.EstadoNormal;
import game.state.EstadoEstresado;
import game.state.EstadoAgotado;

/**
 * Clase que representa al jugador en el juego.
 * Implementa el patrón State para gestionar sus diferentes estados.
 */
public class Jugador {
    private String nombre;
    private int presupuesto;
    private int puntos;
    private EstadoJugador estado;

    /**
     * Constructor de la clase Jugador
     * @param nombre Nombre del jugador
     * @param presupuestoInicial Presupuesto inicial
     */
    public Jugador(String nombre, int presupuestoInicial) {
        this.nombre = nombre;
        this.presupuesto = presupuestoInicial;
        this.puntos = 0;
        this.estado = new EstadoNormal();
    }

    /**
     * Procesa el turno del jugador
     */
    public void procesarTurno() {
        estado.procesarTurno();
    }

    /**
     * El jugador recibe daño
     * @param danio Cantidad de daño recibido
     */
    public void recibirDanio(int danio) {
        int danioReal = (int) (danio * estado.getMultiplicadorDefensa());
        estado.recibirDanio(danioReal);
        presupuesto -= danioReal;
        
        // Actualizar estado según el presupuesto
        actualizarEstado();
    }

    /**
     * El jugador recupera presupuesto
     * @param cantidad Cantidad a recuperar
     */
    public void recuperarPresupuesto(int cantidad) {
        estado.recuperarPresupuesto(cantidad);
        presupuesto += cantidad;
        
        // Actualizar estado según el presupuesto
        actualizarEstado();
    }

    /**
     * El jugador gana puntos
     * @param cantidad Cantidad de puntos ganados
     */
    public void ganarPuntos(int cantidad) {
        puntos += cantidad;
    }

    /**
     * Actualiza el estado del jugador según su presupuesto
     */
    private void actualizarEstado() {
        if (presupuesto <= 20000) {
            estado = new EstadoAgotado();
        } else if (presupuesto >= 80000) {
            estado = new EstadoEstresado();
        } else {
            estado = new EstadoNormal();
        }
    }

    /**
     * Obtiene el nombre del jugador
     * @return String con el nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el presupuesto actual
     * @return int con el presupuesto
     */
    public int getPresupuesto() {
        return presupuesto;
    }

    /**
     * Obtiene los puntos acumulados
     * @return int con los puntos
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Obtiene el estado actual
     * @return EstadoJugador actual
     */
    public EstadoJugador getEstado() {
        return estado;
    }

    /**
     * Verifica si el jugador está derrotado
     * @return boolean indicando si está derrotado
     */
    public boolean estaDerrotado() {
        return presupuesto <= 0;
    }

    /**
     * El jugador gasta una cantidad de dinero
     * @param cantidad Cantidad a gastar
     * @return true si se pudo gastar, false si no hay suficiente dinero
     */
    public boolean gastar(int cantidad) {
        presupuesto -= cantidad;
        actualizarEstado();
        return true;
    }
} 