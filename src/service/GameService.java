package service;

import game.JuegoLogistica;

public class GameService {
    public JuegoLogistica crearNuevoJuego(String ciudad, String dificultad, String nombreJugador, String modoJuego) {
        return new JuegoLogistica(ciudad, dificultad, nombreJugador, modoJuego);
    }
} 