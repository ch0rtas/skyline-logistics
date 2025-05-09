package template;

import game.Jugador;

public abstract class AbstractImpuestosProcessor {
    // Template method
    public final void procesarImpuestos(Jugador jugador, String dificultad, int diaActual) {
        if (debeAplicarImpuestos(diaActual, dificultad)) {
            double porcentaje = calcularPorcentajeImpuestos(dificultad);
            int impuestos = (int) (jugador.getBalance() * porcentaje);
            aplicarImpuestos(jugador, impuestos);
            mostrarMensajeImpuestos(impuestos);
        }
    }

    // Métodos primitivos que las subclases deben implementar
    protected abstract boolean debeAplicarImpuestos(int diaActual, String dificultad);
    protected abstract double calcularPorcentajeImpuestos(String dificultad);
    protected abstract void mostrarMensajeImpuestos(int impuestos);

    // Método hook que puede ser sobrescrito por las subclases
    protected void aplicarImpuestos(Jugador jugador, int impuestos) {
        jugador.gastar(impuestos);
    }
} 