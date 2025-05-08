package ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenuUI {
    public static void mostrarMenuPartida(int diaActual, Calendar fechaActual, String almacenPrincipal, SimpleDateFormat formatoFecha) {
        System.out.println("\n==================================================");
        System.out.println("📅 DÍA " + diaActual + " (" + formatoFecha.format(fechaActual.getTime()) + ") | ALMACÉN PRINCIPAL: " + almacenPrincipal);
        System.out.println("==================================================");
        System.out.println("\n01. Ver pedidos pendientes");
        System.out.println("02. Gestionar pedidos");
        System.out.println("03. Ver flota");
        System.out.println("04. Ver estadísticas");
        System.out.println("05. Pasar al siguiente día");
        System.out.println("99. Finalizar partida");
        System.out.print("\nSeleccione una opción: ");
    }
}