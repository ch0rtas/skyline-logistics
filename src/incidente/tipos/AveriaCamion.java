package incidente.tipos;

import incidente.core.IncidenteLogistico;

/**
 * Implementación concreta de un incidente de avería de camión.
 * Extiende la clase base IncidenteLogistico.
 */
public class AveriaCamion extends IncidenteLogistico {
    private String causa;
    private String vehiculoAsignado;
    private boolean reparacionExitosa;

    /**
     * Constructor de la clase AveriaCamion
     * @param descripcion Descripción de la avería
     */
    public AveriaCamion(String descripcion) {
        super(descripcion);
    }

    @Override
    protected void identificarCausa() {
        // Simulación de identificación de causa
        causa = "Fallo en el sistema de frenos";
        System.out.println("Identificada causa de la avería: " + causa);
    }

    @Override
    protected void asignarRecursos() {
        // Simulación de asignación de recursos
        vehiculoAsignado = "Taller Mecánico #123";
        System.out.println("Asignado taller: " + vehiculoAsignado);
    }

    @Override
    protected boolean evaluarResolucion() {
        // Simulación de evaluación
        reparacionExitosa = Math.random() > 0.2; // 80% de probabilidad de éxito
        return reparacionExitosa;
    }

    @Override
    protected void aplicarSolucion() {
        if (reparacionExitosa) {
            solucion = "Reparación exitosa del sistema de frenos en " + vehiculoAsignado;
        } else {
            solucion = "No se pudo reparar el sistema de frenos";
        }
    }

    @Override
    protected void aplicarContingencia() {
        // Simulación de contingencia
        solucion = "Se ha asignado un vehículo de reemplazo para continuar la ruta";
        System.out.println("Aplicando contingencia: " + solucion);
    }

    /**
     * Obtiene la causa identificada
     * @return String con la causa
     */
    public String getCausa() {
        return causa;
    }

    /**
     * Obtiene el vehículo asignado
     * @return String con el vehículo
     */
    public String getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    /**
     * Indica si la reparación fue exitosa
     * @return boolean indicando el éxito
     */
    public boolean isReparacionExitosa() {
        return reparacionExitosa;
    }
} 