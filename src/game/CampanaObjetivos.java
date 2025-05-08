// Extracted OBJETIVOS_CAMPANA to a new class
package game;

import java.util.HashMap;
import java.util.Map;

public class CampanaObjetivos {
    public static final Map<String, Map<String, Integer>> OBJETIVOS_CAMPANA = new HashMap<>();

    static {
        // Objetivos nivel fácil
        Map<String, Integer> objetivosFacil = new HashMap<>();
        objetivosFacil.put("dias", 30);
        objetivosFacil.put("enviosExitosos", 100);
        objetivosFacil.put("satisfaccion", 80);
        objetivosFacil.put("beneficios", 100000);
        OBJETIVOS_CAMPANA.put("facil", objetivosFacil);

        // Objetivos nivel medio
        Map<String, Integer> objetivosMedio = new HashMap<>();
        objetivosMedio.put("dias", 60);
        objetivosMedio.put("enviosExitosos", 350);
        objetivosMedio.put("satisfaccion", 90);
        objetivosMedio.put("beneficios", 250000);
        OBJETIVOS_CAMPANA.put("medio", objetivosMedio);

        // Objetivos nivel difícil
        Map<String, Integer> objetivosDificil = new HashMap<>();
        objetivosDificil.put("dias", 100);
        objetivosDificil.put("enviosExitosos", 920);
        objetivosDificil.put("satisfaccion", 95);
        objetivosDificil.put("beneficios", 500000);
        OBJETIVOS_CAMPANA.put("dificil", objetivosDificil);
    }
}