package factory;

/**
 * Fábrica concreta para la creación de camiones
 */
public class CamionFactory extends AbstractVehiculoFactory {
    public CamionFactory() {
        super("Camión");
    }
} 