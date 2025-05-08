package game;

public class ImpuestosProcessor {
    public static void procesarImpuestos(Jugador jugador, String dificultad, int diaActual) {
        int diasImpuestos = switch (dificultad.toLowerCase()) {
            case "hard" -> 2;
            case "medium" -> 4;
            case "easy" -> 6;
            default -> 6;
        };
        if (diaActual % diasImpuestos == 0) {
            int impuestos = (int) (jugador.getBalance() * 0.45);
            jugador.gastar(impuestos);
            System.out.println("\n💰 Se han aplicado impuestos del 45%: -" + impuestos + "€");
        }
    }
}