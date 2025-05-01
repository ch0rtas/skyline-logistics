package incidente.tipos;

import incidente.core.IncidenteLogistico;

/**
 * Implementación concreta de un incidente de huelga de transporte.
 * Extiende la clase base IncidenteLogistico.
 */
public class HuelgaTransporte extends IncidenteLogistico {
    private String sectorAfectado;
    private String alternativaPropuesta;
    private boolean negociacionExitosa;

    /**
     * Constructor de la clase HuelgaTransporte
     * @param descripcion Descripción de la huelga
     */
    public HuelgaTransporte(String descripcion) {
        super(descripcion);
    }

    @Override
    protected void identificarCausa() {
        // Simulación de identificación de causa
        sectorAfectado = "Transporte de mercancías por carretera";
        System.out.println("Identificado sector afectado: " + sectorAfectado);
    }

    @Override
    protected void asignarRecursos() {
        // Simulación de asignación de recursos
        alternativaPropuesta = "Uso de transporte ferroviario";
        System.out.println("Propuesta alternativa: " + alternativaPropuesta);
    }

    @Override
    protected boolean evaluarResolucion() {
        // Simulación de evaluación
        negociacionExitosa = Math.random() > 0.3; // 70% de probabilidad de éxito
        return negociacionExitosa;
    }

    @Override
    protected void aplicarSolucion() {
        if (negociacionExitosa) {
            solucion = "Acuerdo alcanzado con los trabajadores, reanudación del servicio";
        } else {
            solucion = "No se pudo alcanzar un acuerdo con los trabajadores";
        }
    }

    @Override
    protected void aplicarContingencia() {
        // Simulación de contingencia
        solucion = "Implementando plan de contingencia: " + alternativaPropuesta;
        System.out.println("Aplicando contingencia: " + solucion);
    }

    /**
     * Obtiene el sector afectado
     * @return String con el sector
     */
    public String getSectorAfectado() {
        return sectorAfectado;
    }

    /**
     * Obtiene la alternativa propuesta
     * @return String con la alternativa
     */
    public String getAlternativaPropuesta() {
        return alternativaPropuesta;
    }

    /**
     * Indica si la negociación fue exitosa
     * @return boolean indicando el éxito
     */
    public boolean isNegociacionExitosa() {
        return negociacionExitosa;
    }
} 