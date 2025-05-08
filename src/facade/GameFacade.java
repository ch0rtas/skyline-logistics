package facade;

import service.*;
import ui.*;
import java.util.Scanner;

public class GameFacade {
    private final GameService gameService;
    private final PlayerService playerService;
    private final UIManager uiManager;
    private final Scanner scanner;

    public GameFacade() {
        this.gameService = new GameService();
        this.playerService = new PlayerService();
        this.uiManager = new UIManager();
        this.scanner = new Scanner(System.in);
    }

    public void iniciarJuego() {
        boolean salir = false;

        while (!salir) {
            uiManager.mostrarMenuPrincipal();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "01", "1":
                    iniciarNuevaPartida();
                    break;
                case "02", "2":
                    mostrarHistoricoJugadores();
                    break;
                case "98":
                    mostrarCreditos();
                    break;
                case "99":
                    salir = true;
                    uiManager.mostrarMensajeDespedida();
                    break;
                default:
                    uiManager.mostrarError("Opción no válida");
            }
        }
        scanner.close();
    }

    private void iniciarNuevaPartida() {
        uiManager.mostrarBienvenida();
        String nombreJugador = uiManager.solicitarNombreJugador(scanner);
        String ciudad = uiManager.seleccionarCiudad(scanner);
        String dificultad = uiManager.seleccionarDificultad(scanner);
        String modoJuego = uiManager.seleccionarModoJuego(scanner);

        game.JuegoLogistica juego = gameService.crearNuevoJuego(ciudad, dificultad, nombreJugador, modoJuego);
        juego.iniciar();
    }

    private void mostrarHistoricoJugadores() {
        playerService.mostrarHistoricoJugadores();
    }

    private void mostrarCreditos() {
        uiManager.mostrarCreditos();
    }
}