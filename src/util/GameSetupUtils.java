package util;

import decorator.IVehiculo;
import factory.VehiculoFactory;
import factory.VehiculoFactoryProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.Arrays;

/**
 * Utility class for setting up the game based on difficulty levels.
 */
public class GameSetupUtils {

    private static final String[] TIPOS_CARGA = {
        "NORMAL", "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL", "PERECEDERO", "ALTO_VALOR", "SERES_VIVOS"
    };

    /**
     * Initializes the fleet of vehicles based on the difficulty level.
     *
     * @param dificultad The difficulty level.
     * @return A list of vehicles.
     */
    public static List<IVehiculo> inicializarFlota(String dificultad) {
        List<IVehiculo> flota = new ArrayList<>();
        Random random = new Random();

        // Helper function to generate random cargo types
        Function<Integer, String[]> generarTiposCarga = (numTipos) -> {
            List<String> tiposDisponibles = new ArrayList<>(Arrays.asList(TIPOS_CARGA));
            List<String> tiposSeleccionados = new ArrayList<>();
            tiposSeleccionados.add("NORMAL"); // All vehicles can transport normal cargo

            for (int i = 1; i < numTipos; i++) {
                if (tiposDisponibles.isEmpty()) break;
                int index = random.nextInt(tiposDisponibles.size());
                tiposSeleccionados.add(tiposDisponibles.remove(index));
            }

            return tiposSeleccionados.toArray(new String[0]);
        };

        switch (dificultad.toLowerCase()) {
            case "easy":
                // 3 vans, 2 trucks, 2 ships, 2 planes
                VehiculoFactory furgonetaFactory = VehiculoFactoryProvider.getFactory("Furgoneta");
                VehiculoFactory camionFactory = VehiculoFactoryProvider.getFactory("Camión");
                VehiculoFactory barcoFactory = VehiculoFactoryProvider.getFactory("Barco");
                VehiculoFactory avionFactory = VehiculoFactoryProvider.getFactory("Avión");

                flota.add(furgonetaFactory.crearVehiculoBase("F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(furgonetaFactory.crearVehiculoBase("F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(furgonetaFactory.crearVehiculoBase("F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(camionFactory.crearVehiculoBase("C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(camionFactory.crearVehiculoBase("C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(barcoFactory.crearVehiculoBase("B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(barcoFactory.crearVehiculoBase("B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(avionFactory.crearVehiculoBase("A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(avionFactory.crearVehiculoBase("A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
            case "medium":
                // 2 vans, 2 trucks, 1 ship, 1 plane
                furgonetaFactory = VehiculoFactoryProvider.getFactory("Furgoneta");
                camionFactory = VehiculoFactoryProvider.getFactory("Camión");
                barcoFactory = VehiculoFactoryProvider.getFactory("Barco");
                avionFactory = VehiculoFactoryProvider.getFactory("Avión");

                flota.add(furgonetaFactory.crearVehiculoBase("F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(furgonetaFactory.crearVehiculoBase("F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(camionFactory.crearVehiculoBase("C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(camionFactory.crearVehiculoBase("C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(barcoFactory.crearVehiculoBase("B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(avionFactory.crearVehiculoBase("A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
            case "hard":
                // 1 van, 1 truck, 1 ship, 1 plane
                furgonetaFactory = VehiculoFactoryProvider.getFactory("Furgoneta");
                camionFactory = VehiculoFactoryProvider.getFactory("Camión");
                barcoFactory = VehiculoFactoryProvider.getFactory("Barco");
                avionFactory = VehiculoFactoryProvider.getFactory("Avión");

                flota.add(furgonetaFactory.crearVehiculoBase("F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(camionFactory.crearVehiculoBase("C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(barcoFactory.crearVehiculoBase("B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(avionFactory.crearVehiculoBase("A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
        }

        return flota;
    }
}