package game.state;

/**
 * Implementación del estado agotado del jugador.
 * En este estado, el jugador hace menos daño pero tiene más defensa.
 */
public class EstadoAgotado implements EstadoJugador {
    @Override
    public void procesarTurno() {
        System.out.println("El jugador está agotado y actúa con menor intensidad");
    }

    @Override
    public void recibirDanio(int danio) {
        System.out.println("El jugador agotado recibe " + danio + " de daño (reducido)");
    }

    @Override
    public void recuperarPresupuesto(int presupuesto) {
        System.out.println("El jugador agotado recupera " + presupuesto + " de presupuesto");
    }

    @Override
    public String getNombreEstado() {
        return "Agotado";
    }

    @Override
    public double getMultiplicadorDanio() {
        return 0.7; // 30% menos de daño
    }

    @Override
    public double getMultiplicadorDefensa() {
        return 1.3; // 30% más de defensa
    }
} 