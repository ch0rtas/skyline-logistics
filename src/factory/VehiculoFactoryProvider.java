package factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que actúa como proveedor de fábricas de vehículos
 */
public class VehiculoFactoryProvider {
    private static final Map<String, VehiculoFactory> factories = new HashMap<>();

    static {
        factories.put("Furgoneta", new FurgonetaFactory());
        factories.put("Camión", new CamionFactory());
        factories.put("Barco", new BarcoFactory());
        factories.put("Avión", new AvionFactory());
    }

    /**
     * Obtiene la fábrica correspondiente al tipo de vehículo
     * @param tipoVehiculo Tipo de vehículo
     * @return Fábrica de vehículos
     * @throws IllegalArgumentException si el tipo de vehículo no existe
     */
    public static VehiculoFactory getFactory(String tipoVehiculo) {
        VehiculoFactory factory = factories.get(tipoVehiculo);
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de vehículo no soportado: " + tipoVehiculo);
        }
        return factory;
    }
} 