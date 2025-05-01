package strategy.context;

import strategy.core.EstrategiaEnvio;
import decorator.base.Envio;

/**
 * Clase contexto que gestiona las estrategias de envío.
 * Implementa el patrón Strategy para permitir cambiar dinámicamente la estrategia.
 */
public class EnvioContext {
    private EstrategiaEnvio estrategia;

    /**
     * Constructor de la clase EnvioContext
     * @param estrategia Estrategia inicial
     */
    public EnvioContext(EstrategiaEnvio estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * Establece una nueva estrategia
     * @param estrategia Nueva estrategia a utilizar
     */
    public void setEstrategia(EstrategiaEnvio estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * Calcula el costo del envío usando la estrategia actual
     * @param envio Envío a calcular
     * @return double con el costo calculado
     */
    public double calcularCosto(Envio envio) {
        return estrategia.calcularCosto(envio);
    }

    /**
     * Calcula el tiempo estimado usando la estrategia actual
     * @param envio Envío a calcular
     * @return int con el tiempo estimado en días
     */
    public int calcularTiempoEstimado(Envio envio) {
        return estrategia.calcularTiempoEstimado(envio);
    }

    /**
     * Obtiene la descripción de la estrategia actual
     * @return String con la descripción
     */
    public String getDescripcionEstrategia() {
        return estrategia.getDescripcion();
    }
} 