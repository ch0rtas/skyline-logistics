package factory;

import decorator.IVehiculo;

/**
 * Interfaz que define la fábrica abstracta para la creación de vehículos
 */
public interface VehiculoFactory {
    /**
     * Crea un vehículo base
     * @param id Identificador único del vehículo
     * @param tiposPermitidos Tipos de carga permitidos
     * @return Vehículo base
     */
    IVehiculo crearVehiculoBase(String id, String[] tiposPermitidos);

    /**
     * Crea un vehículo mejorado
     * @param id Identificador único del vehículo
     * @param tiposPermitidos Tipos de carga permitidos
     * @return Vehículo mejorado
     */
    IVehiculo crearVehiculoMejorado(String id, String[] tiposPermitidos);

    /**
     * Crea un vehículo resistente
     * @param id Identificador único del vehículo
     * @param tiposPermitidos Tipos de carga permitidos
     * @return Vehículo resistente
     */
    IVehiculo crearVehiculoResistente(String id, String[] tiposPermitidos);

    /**
     * Crea un vehículo eficiente
     * @param id Identificador único del vehículo
     * @param tiposPermitidos Tipos de carga permitidos
     * @return Vehículo eficiente
     */
    IVehiculo crearVehiculoEficiente(String id, String[] tiposPermitidos);
} 