package game;

import java.util.Map;

public class CampanaUtils {

    public static boolean verificarObjetivosCampaña(String modoJuego, String dificultad, int diaActual, int enviosExitosos, int satisfaccionClientes, int beneficiosAcumulados, Map<String, Map<String, Integer>> objetivosCampana) {
        if (!modoJuego.equals("campaña")) {
            return false;
        }

        // Obtener objetivos según la dificultad
        Map<String, Integer> objetivos = objetivosCampana.get(dificultad);
        if (objetivos == null) {
            return false;
        }

        // Verificar si se ha alcanzado el día máximo
        if (diaActual >= objetivos.get("dias")) {
            // Verificar el resto de objetivos
            boolean objetivosAlcanzados = 
                enviosExitosos >= objetivos.get("enviosExitosos") &&
                satisfaccionClientes >= objetivos.get("satisfaccion") &&
                beneficiosAcumulados >= objetivos.get("beneficios");

            // Mostrar resultado final
            System.out.println("\n=== 🎯 RESULTADO DE LA CAMPAÑA 🎯 ===");
            System.out.println("Nivel: " + dificultad.toUpperCase());
            System.out.println("Días jugados: " + diaActual + "/" + objetivos.get("dias"));
            System.out.println("Envíos exitosos: " + enviosExitosos + "/" + objetivos.get("enviosExitosos"));
            System.out.println("Satisfacción: " + satisfaccionClientes + "%/" + objetivos.get("satisfaccion") + "%");
            System.out.println("Beneficios: " + beneficiosAcumulados + "€/" + objetivos.get("beneficios") + "€");
            System.out.println("\nResultado: " + (objetivosAlcanzados ? "✅ VICTORIA" : "❌ DERROTA"));

            return objetivosAlcanzados;
        }

        return false;
    }
}