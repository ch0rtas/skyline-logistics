package game;

import decorator.IVehiculo;
import decorator.VehiculoMejorado;
import decorator.VehiculoResistente;
import decorator.VehiculoEficiente;

public class MejorasVehiculo {
    /**
     * Aplica una mejora a un vehículo usando el patrón decorator
     * @param vehiculo Vehículo base a mejorar
     * @param tipoMejora Tipo de mejora a aplicar
     * @return Vehículo mejorado
     */
    public static IVehiculo aplicarMejora(IVehiculo vehiculo, String tipoMejora) {
        switch (tipoMejora.toLowerCase()) {
            case "mejorado":
                return new VehiculoMejorado(vehiculo);
            case "resistente":
                return new VehiculoResistente(vehiculo);
            case "eficiente":
                return new VehiculoEficiente(vehiculo);
            default:
                return vehiculo;
        }
    }

    /**
     * Obtiene el coste de una mejora específica
     * @param vehiculo Vehículo a mejorar
     * @param tipoMejora Tipo de mejora
     * @return Coste de la mejora
     */
    public static int getCosteMejora(IVehiculo vehiculo, String tipoMejora) {
        int costeBase = vehiculo.getPrecio() / 2; // 50% del precio del vehículo
        
        switch (tipoMejora.toLowerCase()) {
            case "mejorado":
                return (int) (costeBase * 0.3); // 30% del coste base
            case "resistente":
                return (int) (costeBase * 0.2); // 20% del coste base
            case "eficiente":
                return (int) (costeBase * 0.15); // 15% del coste base
            default:
                return 0;
        }
    }

    /**
     * Verifica si un vehículo ya tiene una mejora específica
     * @param vehiculo Vehículo a verificar
     * @param tipoMejora Tipo de mejora a verificar
     * @return true si ya tiene la mejora, false si no
     */
    public static boolean tieneMejora(IVehiculo vehiculo, String tipoMejora) {
        // Esta es una implementación simplificada
        // En una implementación real, necesitaríamos una forma de identificar
        // qué decoradores ya están aplicados al vehículo
        return false;
    }
} 