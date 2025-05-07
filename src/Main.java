import facade.GameFacade;

/**
 * Clase principal que inicia el juego de logística
 */
public class Main {
    public static void main(String[] args) {
        GameFacade gameFacade = new GameFacade();
        gameFacade.iniciarJuego();
    }
}