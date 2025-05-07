package game.state;

import java.io.Serializable;

/**
 * Implementación del estado normal del jugador.
 * Representa el estado base sin modificadores.
 */
public class EstadoNormal implements EstadoJugador, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void procesarTurno() {
        System.out.println("El jugador está en estado normal");
    }

    @Override
    public void recibirDanio(int danio) {
        System.out.println("El jugador recibe " + danio + " de daño");
    }

    @Override
    public void recuperarPresupuesto(int presupuesto) {
        System.out.println("El jugador recupera " + presupuesto + " de presupuesto");
    }

    @Override
    public String getNombreEstado() {
        return "Normal";
    }

    @Override
    public double getMultiplicadorDanio() {
        return 1.0;
    }

    @Override
    public double getMultiplicadorDefensa() {
        return 1.0;
    }
}