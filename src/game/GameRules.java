package game;

import singleton.GameRulesSingleton;

public class GameRules {
    private static final GameRulesSingleton rules = GameRulesSingleton.getInstance();

    /**
     * Verifica si el jugador está derrotado según el modo de juego
     * @param modoJuego Modo de juego seleccionado
     * @param balance Balance actual del jugador
     * @return true si el jugador está derrotado, false si no
     */
    public static boolean jugadorDerrotado(String modoJuego, int balance) {
        if (modoJuego.equals("libre")) {
            return false; // En modo libre nunca se pierde
        }
        return balance < 0; // Cambiado de <= 0 a < 0 para que termine cuando sea negativo
    }

    // Métodos delegados al Singleton
    public static int getDiasTotales() {
        return rules.getDiasTotales();
    }

    public static int getDineroInicial() {
        return rules.getDineroInicial();
    }

    public static int getSatisfaccionMinima() {
        return rules.getSatisfaccionMinima();
    }

    public static int getDiasParaObjetivos() {
        return rules.getDiasParaObjetivos();
    }

    public static int getMaxVehiculos() {
        return rules.getMaxVehiculos();
    }

    public static int getMaxPedidosActivos() {
        return rules.getMaxPedidosActivos();
    }
}