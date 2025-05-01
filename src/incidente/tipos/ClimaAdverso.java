package incidente.tipos;

import incidente.core.IncidenteLogistico;

/**
 * Implementación concreta de un incidente por condiciones climáticas adversas.
 * Extiende la clase base IncidenteLogistico.
 */
public class ClimaAdverso extends IncidenteLogistico {
    private String tipoClima;
    private String rutaAlternativa;
    private boolean condicionesMejoradas;

    /**
     * Constructor de la clase ClimaAdverso
     * @param descripcion Descripción de las condiciones climáticas
     */
    public ClimaAdverso(String descripcion) {
        super(descripcion);
    }

    @Override
    protected void identificarCausa() {
        // Simulación de identificación de causa
        tipoClima = "Tormenta severa";
        System.out.println("Identificadas condiciones climáticas: " + tipoClima);
    }

    @Override
    protected void asignarRecursos() {
        // Simulación de asignación de recursos
        rutaAlternativa = "Ruta costera alternativa";
        System.out.println("Asignada ruta alternativa: " + rutaAlternativa);
    }

    @Override
    protected boolean evaluarResolucion() {
        // Simulación de evaluación
        condicionesMejoradas = Math.random() > 0.4; // 60% de probabilidad de mejora
        return condicionesMejoradas;
    }

    @Override
    protected void aplicarSolucion() {
        if (condicionesMejoradas) {
            solucion = "Condiciones climáticas mejoradas, reanudando ruta original";
        } else {
            solucion = "Condiciones climáticas persisten, manteniendo ruta alternativa";
        }
    }

    @Override
    protected void aplicarContingencia() {
        // Simulación de contingencia
        solucion = "Activando plan de contingencia: " + rutaAlternativa;
        System.out.println("Aplicando contingencia: " + solucion);
    }

    /**
     * Obtiene el tipo de clima adverso
     * @return String con el tipo de clima
     */
    public String getTipoClima() {
        return tipoClima;
    }

    /**
     * Obtiene la ruta alternativa
     * @return String con la ruta
     */
    public String getRutaAlternativa() {
        return rutaAlternativa;
    }

    /**
     * Indica si las condiciones mejoraron
     * @return boolean indicando la mejora
     */
    public boolean isCondicionesMejoradas() {
        return condicionesMejoradas;
    }
} 