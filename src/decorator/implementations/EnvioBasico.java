package decorator.implementations;

import decorator.base.Envio;

/**
 * Implementación concreta de un envío básico.
 * Representa el envío más simple sin servicios adicionales.
 */
public class EnvioBasico extends Envio {
    private static final double COSTO_POR_KG = 2.5;
    private static final int TIEMPO_BASE_DIAS = 3;

    /**
     * Constructor de la clase EnvioBasico
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @param peso Peso del envío en kg
     */
    public EnvioBasico(String origen, String destino, double peso) {
        super(origen, destino, peso, peso * COSTO_POR_KG);
    }

    @Override
    public double getCosto() {
        return costoBase;
    }

    @Override
    public int getTiempoEstimado() {
        return TIEMPO_BASE_DIAS;
    }

    @Override
    public String getDescripcion() {
        return "Envío básico de " + peso + " kg desde " + origen + " a " + destino;
    }
} 