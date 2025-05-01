package game.state;

/**
 * Implementación del estado estresado del jugador.
 * En este estado, el jugador hace más daño pero recibe más daño también.
 */
public class EstadoEstresado implements EstadoJugador {
    @Override
    public void procesarTurno() {
        System.out.println("El jugador está estresado y actúa con mayor intensidad");
    }

    @Override
    public void recibirDanio(int danio) {
        System.out.println("El jugador estresado recibe " + danio + " de daño (aumentado)");
    }

    @Override
    public void recuperarPresupuesto(int presupuesto) {
        System.out.println("El jugador estresado recupera " + presupuesto + " de presupuesto");
    }

    @Override
    public String getNombreEstado() {
        return "Estresado";
    }

    @Override
    public double getMultiplicadorDanio() {
        return 1.5; // 50% más de daño
    }

    @Override
    public double getMultiplicadorDefensa() {
        return 0.8; // 20% menos de defensa
    }
} 