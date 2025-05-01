package game.state;

/**
 * Interfaz que define el contrato para los estados del jugador.
 * Implementa el patrón State para gestionar el ciclo de vida del jugador.
 */
public interface EstadoJugador {
    /**
     * Procesa el turno del jugador en su estado actual
     */
    void procesarTurno();

    /**
     * El jugador recibe daño a su presupuesto
     * @param danio Cantidad de daño recibido
     */
    void recibirDanio(int danio);

    /**
     * El jugador recupera presupuesto
     * @param presupuesto Cantidad de presupuesto recuperado
     */
    void recuperarPresupuesto(int presupuesto);

    /**
     * Obtiene el nombre del estado actual
     * @return String con el nombre del estado
     */
    String getNombreEstado();

    /**
     * Obtiene el multiplicador de daño en el estado actual
     * @return double con el multiplicador
     */
    double getMultiplicadorDanio();

    /**
     * Obtiene el multiplicador de defensa en el estado actual
     * @return double con el multiplicador
     */
    double getMultiplicadorDefensa();
} 