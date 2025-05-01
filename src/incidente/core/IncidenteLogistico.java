package incidente.core;

import java.time.LocalDateTime;

/**
 * Clase base abstracta para los incidentes logísticos.
 * Implementa el patrón Template Method para definir el proceso de resolución.
 */
public abstract class IncidenteLogistico {
    protected final String id;
    protected final String descripcion;
    protected final LocalDateTime fechaCreacion;
    protected boolean resuelto;
    protected String solucion;

    /**
     * Constructor de la clase IncidenteLogistico
     * @param descripcion Descripción del incidente
     */
    public IncidenteLogistico(String descripcion) {
        this.id = java.util.UUID.randomUUID().toString();
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
        this.resuelto = false;
        this.solucion = null;
    }

    /**
     * Método plantilla que define el proceso de resolución
     */
    public final void resolver() {
        if (!resuelto) {
            identificarCausa();
            asignarRecursos();
            if (evaluarResolucion()) {
                aplicarSolucion();
                notificarResolucion();
                resuelto = true;
            } else {
                aplicarContingencia();
            }
        }
    }

    /**
     * Identifica la causa del incidente
     */
    protected abstract void identificarCausa();

    /**
     * Asigna los recursos necesarios para resolver el incidente
     */
    protected abstract void asignarRecursos();

    /**
     * Evalúa si el incidente ha sido resuelto
     * @return boolean indicando si está resuelto
     */
    protected abstract boolean evaluarResolucion();

    /**
     * Aplica la solución al incidente
     */
    protected abstract void aplicarSolucion();

    /**
     * Notifica la resolución del incidente
     */
    protected void notificarResolucion() {
        System.out.println("Incidente " + id + " resuelto: " + solucion);
    }

    /**
     * Aplica una contingencia si la solución principal falla
     */
    protected abstract void aplicarContingencia();

    /**
     * Obtiene el ID del incidente
     * @return String con el ID
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene la descripción del incidente
     * @return String con la descripción
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la fecha de creación
     * @return LocalDateTime con la fecha
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Indica si el incidente está resuelto
     * @return boolean indicando si está resuelto
     */
    public boolean isResuelto() {
        return resuelto;
    }

    /**
     * Obtiene la solución aplicada
     * @return String con la solución
     */
    public String getSolucion() {
        return solucion;
    }
} 