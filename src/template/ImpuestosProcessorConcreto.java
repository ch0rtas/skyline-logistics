package template;

public class ImpuestosProcessorConcreto extends AbstractImpuestosProcessor {
    @Override
    protected boolean debeAplicarImpuestos(int diaActual, String dificultad) {
        int diasImpuestos = switch (dificultad.toLowerCase()) {
            case "hard" -> 2;
            case "medium" -> 4;
            case "easy" -> 6;
            default -> 6;
        };
        return diaActual % diasImpuestos == 0;
    }

    @Override
    protected double calcularPorcentajeImpuestos(String dificultad) {
        return 0.45; // 45% de impuestos
    }

    @Override
    protected void mostrarMensajeImpuestos(int impuestos) {
        System.out.println("\n💰 Se han aplicado impuestos del 45%: -" + impuestos + "€");
    }
} 