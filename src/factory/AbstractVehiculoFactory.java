package factory;

import decorator.IVehiculo;
import game.Vehiculo;
import decorator.VehiculoMejorado;
import decorator.VehiculoResistente;
import decorator.VehiculoEficiente;

/**
 * Clase abstracta que implementa la lógica común para todas las fábricas de vehículos
 */
public abstract class AbstractVehiculoFactory implements VehiculoFactory {
    protected String tipoVehiculo;

    public AbstractVehiculoFactory(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    @Override
    public IVehiculo crearVehiculoBase(String id, String[] tiposPermitidos) {
        return new Vehiculo(tipoVehiculo, id, tiposPermitidos);
    }

    @Override
    public IVehiculo crearVehiculoMejorado(String id, String[] tiposPermitidos) {
        IVehiculo vehiculoBase = crearVehiculoBase(id, tiposPermitidos);
        return new VehiculoMejorado(vehiculoBase);
    }

    @Override
    public IVehiculo crearVehiculoResistente(String id, String[] tiposPermitidos) {
        IVehiculo vehiculoBase = crearVehiculoBase(id, tiposPermitidos);
        return new VehiculoResistente(vehiculoBase);
    }

    @Override
    public IVehiculo crearVehiculoEficiente(String id, String[] tiposPermitidos) {
        IVehiculo vehiculoBase = crearVehiculoBase(id, tiposPermitidos);
        return new VehiculoEficiente(vehiculoBase);
    }
} 