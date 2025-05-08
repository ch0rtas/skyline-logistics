package ui;

import game.JuegoLogistica;

public class OpcionProcessor {
    public static void procesarOpcion(String opcion, JuegoLogistica juegoLogistica) {
        switch (opcion) {
            case "01":
            case "1":
                juegoLogistica.mostrarPedidosPendientes();
                break;
            case "02":
            case "2":
                juegoLogistica.mostrarPedidosEnCurso();
                break;
            case "03":
            case "3":
                juegoLogistica.gestionarPedido();
                break;
            case "04":
            case "4":
                juegoLogistica.mostrarFlota();
                break;
            case "05":
            case "5":
                juegoLogistica.mostrarEstadisticas();
                break;
            case "06":
            case "6":
                juegoLogistica.pasarDia();
                break;
            case "99":
                juegoLogistica.mostrarEstadisticas();
                juegoLogistica.guardarEstadisticas();
                System.exit(0);
                break;
            default:
                System.out.println("\n❌ Opción no válida");
                juegoLogistica.mostrarMenuPartida();
                procesarOpcion(juegoLogistica.getScanner().nextLine(), juegoLogistica);
        }
    }
}