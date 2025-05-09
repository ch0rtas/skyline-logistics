package factory;

/**
 * Fábrica concreta para la creación de aviones
 */
public class AvionFactory extends AbstractVehiculoFactory {
    public AvionFactory() {
        super("Avión");
    }
} 