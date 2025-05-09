package singleton;

public class GameRulesSingleton {
    private static GameRulesSingleton instance;
    private final int DIAS_TOTALES = 30;
    private final int DINERO_INICIAL = 10000;
    private final int SATISFACCION_MINIMA = 60;
    private final int DIAS_PARA_OBJETIVOS = 5;
    private final int MAX_VEHICULOS = 10;
    private final int MAX_PEDIDOS_ACTIVOS = 5;

    private GameRulesSingleton() {
        // Constructor privado para evitar instanciación directa
    }

    public static synchronized GameRulesSingleton getInstance() {
        if (instance == null) {
            instance = new GameRulesSingleton();
        }
        return instance;
    }

    public int getDiasTotales() {
        return DIAS_TOTALES;
    }

    public int getDineroInicial() {
        return DINERO_INICIAL;
    }

    public int getSatisfaccionMinima() {
        return SATISFACCION_MINIMA;
    }

    public int getDiasParaObjetivos() {
        return DIAS_PARA_OBJETIVOS;
    }

    public int getMaxVehiculos() {
        return MAX_VEHICULOS;
    }

    public int getMaxPedidosActivos() {
        return MAX_PEDIDOS_ACTIVOS;
    }
} 