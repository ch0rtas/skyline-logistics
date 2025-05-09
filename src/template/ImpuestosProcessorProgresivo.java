package template;

public class ImpuestosProcessorProgresivo extends AbstractImpuestosProcessor {
    @Override
    protected boolean debeAplicarImpuestos(int diaActual, String dificultad) {
        int diasImpuestos = getDiasEntreImpuestos(dificultad);
        return diaActual % diasImpuestos == 0;
    }

    @Override
    protected double calcularPorcentajeImpuestos(String dificultad) {
        return switch (dificultad.toLowerCase()) {
            case "hard" -> 0.50; // 50% en modo difícil
            case "medium" -> 0.40; // 40% en modo medio
            case "easy" -> 0.30; // 30% en modo fácil
            default -> 0.35;
        };
    }

    @Override
    protected void mostrarMensajeImpuestos(int impuestos) {
        System.out.println("\n💰 Se han aplicado impuestos progresivos: -" + impuestos + "€");
    }

    @Override
    protected int getDiasEntreImpuestos(String dificultad) {
        return switch (dificultad.toLowerCase()) {
            case "hard" -> 3;
            case "medium" -> 5;
            case "easy" -> 7;
            default -> 7;
        };
    }
} 