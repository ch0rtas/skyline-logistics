package game;

import decorator.IVehiculo;
import decorator.VehiculoMejorado;
import decorator.VehiculoResistente;
import decorator.VehiculoEficiente;
import java.util.*;

public class VehiculoGenerator {
    public static List<IVehiculo> generarVehiculosMercado(String[] tiposCarga) {
        List<IVehiculo> vehiculosMercado = new ArrayList<>();
        Random random = new Random();
        Set<String> idsUsados = new HashSet<>();

        // Generar 3 vehículos diferentes
        while (vehiculosMercado.size() < 3) {
            String tipo = random.nextBoolean() ? "Furgoneta" : "Camión";
            // Generar ID con formato 1Letra2Numeros
            String id = tipo.charAt(0) + String.format("%02d", random.nextInt(100));

            // Verificar que el ID no esté repetido
            if (idsUsados.contains(id)) {
                continue;
            }
            idsUsados.add(id);

            // Generar tipos de carga permitidos aleatorios (mínimo 1, máximo 3)
            int numTipos = 1 + random.nextInt(3);
            List<String> tiposPermitidos = new ArrayList<>();

            for (int j = 0; j < numTipos; j++) {
                String tipoCarga;
                do {
                    tipoCarga = tiposCarga[random.nextInt(tiposCarga.length)];
                } while (tiposPermitidos.contains(tipoCarga));
                tiposPermitidos.add(tipoCarga);
            }

            // Crear vehículo base
            IVehiculo vehiculoBase = new Vehiculo(tipo, id, tiposPermitidos.toArray(new String[0]));
            
            // Aplicar decoradores aleatorios
            if (random.nextBoolean()) {
                vehiculoBase = new VehiculoMejorado(vehiculoBase);
            }
            if (random.nextBoolean()) {
                vehiculoBase = new VehiculoResistente(vehiculoBase);
            }
            if (random.nextBoolean()) {
                vehiculoBase = new VehiculoEficiente(vehiculoBase);
            }

            vehiculosMercado.add(vehiculoBase);
        }

        return vehiculosMercado;
    }
}