package ui;

import game.Jugador;

public class BienvenidaUI {
    public static void mostrarBienvenida(String almacenPrincipal, Jugador jugador) {
        System.out.println("\n✅ Sistema iniciado en región: " + almacenPrincipal);
        System.out.println("💰 Balance inicial: $" + jugador.getBalance());
    }
}