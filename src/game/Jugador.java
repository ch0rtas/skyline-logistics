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
    private int balance;
    private int puntos;
    private EstadoJugador estado;

    /**
     * Constructor de la clase Jugador
     * @param nombre Nombre del jugador
     * @param balanceInicial Balance inicial
     */
    public Jugador(String nombre, int balanceInicial) {
        this.nombre = nombre;
        this.balance = balanceInicial;
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
        balance -= danioReal;
        
        // Actualizar estado según el balance
        actualizarEstado();
    }

    /**
     * El jugador recupera balance
     * @param cantidad Cantidad a recuperar
     */
    public void recuperarBalance(int cantidad) {
        estado.recuperarBalance(cantidad);
        balance += cantidad;
        
        // Actualizar estado según el balance
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
     * Actualiza el estado del jugador según su balance
     */
    private void actualizarEstado() {
        if (balance <= 20000) {
            estado = new EstadoAgotado();
        } else if (balance >= 80000) {
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
     * Obtiene el balance actual
     * @return int con el balance
     */
    public int getBalance() {
        return balance;
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
        return balance <= 0;
    }

    /**
     * El jugador gasta una cantidad de dinero
     * @param cantidad Cantidad a gastar
     * @return true si se pudo gastar, false si no hay suficiente dinero
     */
    public boolean gastar(int cantidad) {
        balance -= cantidad;
        actualizarEstado();
        return true;
    }
}