package game;

import java.io.Serializable;

/**
 * Clase que maneja la salida del juego
 */
public class SalirJuego implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Ejecuta la acción de salir del juego
     */
    public static void ejecutar() {
        System.out.println("\n👋 ¡Hasta luego!");
        System.exit(0);
    }
}