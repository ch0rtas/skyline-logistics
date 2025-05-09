package game;

/**
 * Clase que representa al jugador en el juego.
 */
public class Jugador {
    private String nombre;
    private int balance;
    private int puntos;

    /**
     * Constructor que inicializa el jugador con un nombre y un balance inicial.
     * @param nombre Nombre del jugador
     * @param balanceInicial Balance inicial del jugador
     */
    public Jugador(String nombre, int balanceInicial) {
        this.nombre = nombre;
        this.balance = balanceInicial;
        this.puntos = 0; // Inicializamos los puntos en 0
    }

    /**
     * El jugador gana puntos
     * @param cantidad Cantidad de puntos ganados
     */
    public void ganarPuntos(int cantidad) {
        puntos += cantidad;
    }

    /**
     * Recupera una cantidad de balance para el jugador.
     * @param cantidad Cantidad a recuperar
     */
    public void recuperarBalance(int cantidad) {
        balance += cantidad;
    }

    /**
     * Incrementa el balance del jugador.
     * @param cantidad Cantidad a incrementar
     */
    public void incrementarBalance(int cantidad) {
        this.balance += cantidad;
    }

    /**
     * Obtiene el nombre del jugador
     * @return String con el nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el balance actual
     * @return int con el balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Obtiene los puntos acumulados
     * @return int con los puntos
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Verifica si el jugador está derrotado
     * @return boolean indicando si está derrotado
     */
    public boolean estaDerrotado() {
        return balance <= 0;
    }

    /**
     * El jugador gasta una cantidad de dinero
     * @param cantidad Cantidad a gastar
     * @return true si se pudo gastar, false si no hay suficiente dinero
     */
    public boolean gastar(int cantidad) {
        balance -= cantidad;
        return true;
    }
}