package game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import strategy.core.ModoJuegoStrategy;


/**
 * Clase principal que gestiona el juego de logística
 */
public class JuegoLogistica {
    private Jugador jugador;
    private Scanner scanner;
    private Random random;
    private Map<String, Pedido> pedidos;
    private List<Pedido> pedidosPendientes;
    private List<Pedido> pedidosEnCurso;
    private List<Vehiculo> flota;
    private int diaActual;
    private Calendar fechaActual;
    private String almacenPrincipal;
    private String dificultad;
    private int satisfaccionClientes;
    private int enviosExitosos;
    private int enviosTotales;
    private int beneficiosAcumulados;
    private ModoJuegoStrategy modoJuego;
    private static final double TASA_IMPUESTOS = 0.45;
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
    private static final String[] PROVINCIAS = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza",
        "Málaga", "Murcia", "Palma de Mallorca", "Las Palmas", "Bilbao",
        "Alicante", "Córdoba", "Valladolid", "Vigo", "Gijón"
    };

    // Provincias que son islas
    private static final String[] ISLAS = {
        "Palma de Mallorca", "Las Palmas"
    };

    // Provincias con acceso marítimo (puertos)
    private static final String[] PROVINCIAS_MARITIMAS = {
        "Barcelona", "Valencia", "Málaga", "Bilbao", "Alicante", "Vigo", "Gijón",
        "Palma de Mallorca", "Las Palmas"
    };

    // Matriz de distancias entre provincias (en km)
    private static final int[][] DISTANCIAS = {
        // Madrid, Barcelona, Valencia, Sevilla, Zaragoza, Málaga, Murcia, Palma, Las Palmas, Bilbao, Alicante, Córdoba, Valladolid, Vigo, Gijón
        {0, 621, 352, 538, 325, 530, 400, 800, 1800, 395, 420, 400, 193, 599, 450}, // Madrid
        {621, 0, 349, 1000, 296, 1000, 600, 200, 1800, 610, 500, 900, 800, 1000, 800}, // Barcelona
        {352, 349, 0, 650, 300, 600, 250, 300, 1800, 600, 166, 500, 500, 800, 700}, // Valencia
        {538, 1000, 650, 0, 800, 200, 400, 600, 1800, 800, 500, 140, 600, 900, 800}, // Sevilla
        {325, 296, 300, 800, 0, 700, 500, 500, 1800, 300, 400, 600, 300, 700, 600}, // Zaragoza
        {530, 1000, 600, 200, 700, 0, 300, 400, 1800, 800, 400, 200, 700, 1000, 900}, // Málaga
        {400, 600, 250, 400, 500, 300, 0, 300, 1800, 700, 100, 300, 600, 900, 800}, // Murcia
        {800, 200, 300, 600, 500, 400, 300, 0, 1800, 800, 300, 600, 800, 1000, 900}, // Palma de Mallorca
        {1800, 1800, 1800, 1800, 1800, 1800, 1800, 1800, 0, 1800, 1800, 1800, 1800, 1800, 1800}, // Las Palmas
        {395, 610, 600, 800, 300, 800, 700, 800, 1800, 0, 600, 700, 280, 400, 300}, // Bilbao
        {420, 500, 166, 500, 400, 400, 100, 300, 1800, 600, 0, 400, 500, 800, 700}, // Alicante
        {400, 900, 500, 140, 600, 200, 300, 600, 1800, 700, 400, 0, 500, 800, 700}, // Córdoba
        {193, 800, 500, 600, 300, 700, 600, 800, 1800, 280, 500, 500, 0, 400, 300}, // Valladolid
        {599, 1000, 800, 900, 700, 1000, 900, 1000, 1800, 400, 800, 800, 400, 0, 200}, // Vigo
        {450, 800, 700, 800, 600, 900, 800, 900, 1800, 300, 700, 700, 300, 200, 0}  // Gijón
    };

    private List<Vehiculo> vehiculosMercado;
    private static final String[] TIPOS_CARGA = {"NORMAL", "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL"};

    /**
     * Constructor del juego
     * @param almacenPrincipal Provincia seleccionada como almacén principal
     * @param dificultad Nivel de dificultad
     * @param nombreJugador Nombre del jugador
     */
    public JuegoLogistica(String almacenPrincipal, String dificultad, String nombreJugador) {
        // Normalizar el nombre del almacén principal
        this.almacenPrincipal = normalizarNombreProvincia(almacenPrincipal);
        this.dificultad = dificultad.toLowerCase();
        this.jugador = new Jugador(nombreJugador, calcularBalanceInicial());
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.pedidos = new HashMap<>();
        this.pedidosPendientes = new ArrayList<>();
        this.pedidosEnCurso = new ArrayList<>();
        this.diaActual = 1;
        this.fechaActual = Calendar.getInstance();
        this.satisfaccionClientes = 100;
        this.enviosExitosos = 0;
        this.enviosTotales = 0;
        this.beneficiosAcumulados = 0;
    }

    /**
    * Asigna el modo de juego a utilizar (Strategy Pattern).
    */
    public void setModoJuego(ModoJuegoStrategy modo) {
        this.modoJuego = modo;
    }      

    /**
     * Calcula el balance inicial según la dificultad
     * @return int con el balance inicial
     */
    private int calcularBalanceInicial() {
        switch (dificultad) {
            case "easy":
                return 50000;
            case "medium":
                return 25000;
            case "hard":
                return 10000;
            default:
                return 25000;
        }
    }

    /**
     * Calcula la multa por rechazar un pedido
     * @param pedido Pedido a rechazar
     * @return int con el monto de la multa
     */
    private int calcularMultaRechazo(Pedido pedido) {
        int base = 5000;
        int multa = base;
        
        // Aumentar según la dificultad
        switch (dificultad) {
            case "easy":
                multa *= 1;
                break;
            case "medium":
                multa *= 1.5;
                break;
            case "hard":
                multa *= 2;
                break;
        }
        
        // Aumentar según el día actual
        multa *= (1 + (diaActual * 0.1)); // 10% más por cada día
        
        // Aumentar según la prioridad
        if (pedido.getPrioridad().equals("URGENTE")) {
            multa *= 2;
        } else if (pedido.getPrioridad().equals("NORMAL")) {
            multa *= 1.5;
        }
        
        return (int) multa;
    }

    /**
     * Calcula los días entre pagos de impuestos según la dificultad
     * @return int con los días entre pagos
     */
    private int calcularDiasImpuestos() {
        switch (dificultad) {
            case "easy":
                return 8;
            case "medium":
                return 5;
            case "hard":
                return 3;
            default:
                return 5;
        }
    }

    /**
     * Inicia el juego
     */
    public void iniciar() {
        mostrarBienvenida();
        modoJuego.iniciarJuego(jugador);
        modoJuego.mostrarObjetivos();
        inicializarFlota();
        generarVehiculosMercado();
        generarPedidosDia();
        
        while (!jugador.estaDerrotado() && !modoJuego.verificarCondicionesFin(jugador, diaActual)) {
            mostrarMenuPrincipal();
            procesarOpcion(scanner.nextLine());
        }
        
        mostrarGameOver();
    }

    /**
     * Muestra la pantalla de bienvenida
     */
    private void mostrarBienvenida() {
        System.out.println("\n✅ Sistema iniciado en región: " + almacenPrincipal);
        System.out.println("💰 Balance inicial: $" + jugador.getPresupuesto());
    }

    /**
     * Inicializa la flota de vehículos según la dificultad
     */
    private void inicializarFlota() {
        flota = new ArrayList<>();
        Random random = new Random();
        
        // Función auxiliar para generar tipos de carga aleatorios
        Function<Integer, String[]> generarTiposCarga = (numTipos) -> {
            List<String> tiposDisponibles = new ArrayList<>(Arrays.asList(TIPOS_CARGA));
            List<String> tiposSeleccionados = new ArrayList<>();
            tiposSeleccionados.add("NORMAL"); // Todos los vehículos pueden transportar carga normal
            
            for (int i = 1; i < numTipos; i++) {
                if (tiposDisponibles.isEmpty()) break;
                int index = random.nextInt(tiposDisponibles.size());
                tiposSeleccionados.add(tiposDisponibles.remove(index));
            }
            
            return tiposSeleccionados.toArray(new String[0]);
        };
        
        switch (dificultad) {
            case "easy":
                // 3 furgonetas, 2 camiones, 2 barcos, 2 aviones
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
            case "medium":
                // 2 furgonetas, 2 camiones, 1 barco, 1 avión
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
            case "hard":
                // 1 furgoneta, 1 camión, 1 barco, 1 avión
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
        }

        System.out.println("\n🛠 Empresa creada:");
        System.out.println("   - Almacén: " + almacenPrincipal);
        System.out.print("   - Vehículos: ");
        for (int i = 0; i < flota.size(); i++) {
            Vehiculo v = flota.get(i);
            System.out.print(v.getTipo() + " " + v.getId());
            if (i < flota.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    /**
     * Muestra la flota de vehículos
     */
    private void mostrarFlota() {
        System.out.println("\n🚗 FLOTA DE VEHÍCULOS:");
        
        // Calcular anchos máximos para cada columna
        String[] encabezados = {"TIPO", "ID", "CAPACIDAD", "VELOCIDAD", "COSTE/KM", "ESTADO", "CARGAS PERMITIDAS"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Vehiculo vehiculo : flota) {
            String estado;
            if (vehiculo.getPedidoAsignado() != null) {
                Calendar fechaEntrega = Calendar.getInstance();
                fechaEntrega.setTime(fechaActual.getTime());
                fechaEntrega.add(Calendar.DAY_OF_MONTH, vehiculo.getPedidoAsignado().getDiasRestantes());
                estado = "Ocupado (" + vehiculo.getPedidoAsignado().getId() + ") hasta " + formatoFecha.format(fechaEntrega.getTime());
            } else {
                estado = "Disponible";
            }
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                estado,
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Vehiculo vehiculo : flota) {
            String estado;
            if (vehiculo.getPedidoAsignado() != null) {
                Calendar fechaEntrega = Calendar.getInstance();
                fechaEntrega.setTime(fechaActual.getTime());
                fechaEntrega.add(Calendar.DAY_OF_MONTH, vehiculo.getPedidoAsignado().getDiasRestantes());
                estado = "Ocupado (" + vehiculo.getPedidoAsignado().getId() + ") hasta " + formatoFecha.format(fechaEntrega.getTime());
            } else {
                estado = "Disponible";
            }
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                estado,
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }

        System.out.println("\n¿Desea ver el mercado de vehículos? (S/N)");
        String opcion = scanner.nextLine().toUpperCase();
        
        if (opcion.equals("S")) {
            mostrarMercadoVehiculos();
        }
    }

    /**
     * Muestra el mercado de vehículos
     */
    private void mostrarMercadoVehiculos() {
        if (vehiculosMercado.isEmpty()) {
            System.out.println("\n❌ No hay vehículos disponibles en el mercado hoy");
            return;
        }

        System.out.println("\n🏪 MERCADO DE VEHÍCULOS:");
        
        // Calcular anchos máximos para cada columna
        String[] encabezados = {"TIPO", "ID", "CAPACIDAD", "VELOCIDAD", "COSTE/KM", "PRECIO", "CARGAS PERMITIDAS"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Vehiculo vehiculo : vehiculosMercado) {
            int precio = calcularPrecioVehiculo(vehiculo);
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                "$" + precio,
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Vehiculo vehiculo : vehiculosMercado) {
            int precio = calcularPrecioVehiculo(vehiculo);
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                "$" + precio,
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }

        System.out.println("\n¿Desea comprar algún vehículo? (S/N)");
        String opcion = scanner.nextLine().toUpperCase();
        
        if (opcion.equals("S")) {
            System.out.println("\nIngrese el ID del vehículo que desea comprar:");
            String idVehiculo = scanner.nextLine().toUpperCase();
            
            Vehiculo vehiculoSeleccionado = null;
            for (Vehiculo v : vehiculosMercado) {
                if (v.getId().equals(idVehiculo)) {
                    vehiculoSeleccionado = v;
                    break;
                }
            }
            
            if (vehiculoSeleccionado != null) {
                int precio = calcularPrecioVehiculo(vehiculoSeleccionado);
                if (jugador.getPresupuesto() >= precio) {
                    jugador.recibirDanio(precio);
                    flota.add(vehiculoSeleccionado);
                    vehiculosMercado.remove(vehiculoSeleccionado);
                    System.out.println("\n✅ Vehículo comprado exitosamente");
                    System.out.println("   - Precio pagado: $" + precio);
                    System.out.println("   - Presupuesto restante: $" + jugador.getPresupuesto());
                    System.out.println("   - El vehículo estará disponible a partir del próximo día");
                } else {
                    System.out.println("\n❌ No tienes suficiente dinero para comprar este vehículo");
                    System.out.println("   - Precio del vehículo: $" + precio);
                    System.out.println("   - Tu presupuesto: $" + jugador.getPresupuesto());
                }
            } else {
                System.out.println("❌ ID de vehículo no válido");
            }
        }
    }

    /**
     * Calcula el precio de un vehículo según sus características
     * @param vehiculo Vehículo a calcular precio
     * @return int con el precio calculado
     */
    private int calcularPrecioVehiculo(Vehiculo vehiculo) {
        int precioBase = vehiculo.getCapacidad() * 2 + vehiculo.getVelocidad() * 10 + vehiculo.getCostePorKm() * 100;
        precioBase += vehiculo.getTiposPaquetesPermitidos().size() * 1000; // Cada tipo adicional suma 1000
        
        // Ajuste según dificultad
        switch (dificultad) {
            case "easy":
                precioBase *= 0.8; // 20% más barato
                break;
            case "hard":
                precioBase *= 1.2; // 20% más caro
                break;
        }
        
        return precioBase;
    }

    /**
     * Muestra el menú principal
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n==================================================");
        System.out.println("📅 DÍA " + diaActual + " (" + formatoFecha.format(fechaActual.getTime()) + ") | ALMACÉN PRINCIPAL: " + almacenPrincipal);
        System.out.println("==================================================");
        System.out.println("\n01. Ver pedidos pendientes");
        System.out.println("02. Ver pedidos en curso");
        System.out.println("03. Gestionar pedidos");
        System.out.println("04. Ver flota");
        System.out.println("05. Ver estadísticas");
        System.out.println("06. Pasar al siguiente día");
        System.out.println("99. Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    /**
     * Procesa la opción seleccionada
     * @param opcion Opción elegida por el usuario
     */
    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "01":
            case "1":
                mostrarPedidosPendientes();
                break;
            case "02":
            case "2":
                mostrarPedidosEnCurso();
                break;
            case "03":
            case "3":
                gestionarPedido();
                break;
            case "04":
            case "4":
                mostrarFlota();
                break;
            case "05":
            case "5":
                mostrarEstadisticas();
                break;
            case "06":
            case "6":
                pasarDia();
                break;
            case "99":
                SalirJuego.ejecutar();
                break;
            default:
                System.out.println("\n❌ Opción no válida");
                mostrarMenuPrincipal();
                procesarOpcion(scanner.nextLine());
        }
    }

    /**
     * Genera un pedido aleatorio
     * @return Pedido generado
     */
    private Pedido generarPedidoAleatorio() {
        String[] clientes = {
            "Hospital Regional", 
            "Farmacia Central", 
            "Laboratorio Médico",
            "Supermercado Local",
            "Restaurante VIP",
            "Tienda Electrónica",
            "Floristería Central",
            "Joyería Premium",
            "Galería de Arte",
            "Constructora",
            "Fábrica Industrial",
            "Aeropuerto Local",
            "Puerto Marítimo",
            "Centro Científico",
            "Universidad"
        };
        
        // Cargas específicas por tipo de paquete
        Map<String, String[]> cargasPorTipo = new HashMap<>();
        cargasPorTipo.put("NORMAL", new String[]{"Materiales Construcción", "Piezas Industriales", "Equipaje VIP", "Material Educativo"});
        cargasPorTipo.put("REFRIGERADO", new String[]{"Vacunas", "Medicamentos", "Alimentos Frescos", "Mariscos Frescos"});
        cargasPorTipo.put("CONGELADO", new String[]{"Productos Congelados", "Muestras Biológicas", "Alimentos Ultra Congelados"});
        cargasPorTipo.put("PELIGROSO", new String[]{"Productos Químicos", "Materiales Explosivos", "Residuos Tóxicos", "Combustibles"});
        cargasPorTipo.put("ESCOLTADO", new String[]{"Joyas Valiosas", "Obras de Arte", "Dinero", "Documentos Secretos"});
        cargasPorTipo.put("FRÁGIL", new String[]{"Equipos Médicos", "Electrónicos", "Flores Exóticas", "Instrumentos Musicales", "Antigüedades"});
        
        String[] prioridades = {"URGENTE", "NORMAL", "BAJA"};
        String[] tiposPaquetes = {"NORMAL", "REFRIGERADO", "CONGELADO", "ESCOLTADO", "PELIGROSO", "FRÁGIL"};

        // Seleccionar tipo de paquete
        String tipoPaquete = tiposPaquetes[random.nextInt(tiposPaquetes.length)];
        
        // Seleccionar carga según el tipo
        String[] cargasDisponibles = cargasPorTipo.get(tipoPaquete);
        String carga = cargasDisponibles[random.nextInt(cargasDisponibles.length)];
        
        // Seleccionar cliente
        String cliente = clientes[random.nextInt(clientes.length)];
        
        // Seleccionar prioridad
        String prioridad = prioridades[random.nextInt(prioridades.length)];
        
        // Generar ID y peso según el tipo de paquete
        String idPedido = "P" + (100 + random.nextInt(900));
        int peso;
        
        // Ajustar peso según el tipo de paquete
        switch (tipoPaquete) {
            case "NORMAL":
                peso = 100 + random.nextInt(900); // 100-1000 kg
                break;
            case "REFRIGERADO":
                peso = 200 + random.nextInt(800); // 200-1000 kg
                break;
            case "CONGELADO":
                peso = 500 + random.nextInt(1500); // 500-2000 kg
                break;
            case "PELIGROSO":
                peso = 1000 + random.nextInt(4000); // 1000-5000 kg
                break;
            case "ESCOLTADO":
                peso = 50 + random.nextInt(450); // 50-500 kg
                break;
            case "FRÁGIL":
                peso = 10 + random.nextInt(490); // 10-500 kg
                break;
            default:
                peso = 100 + random.nextInt(900);
        }

        // Seleccionar un destino aleatorio que no sea el almacén principal
        String destino;
        do {
            destino = PROVINCIAS[random.nextInt(PROVINCIAS.length)];
        } while (destino.equalsIgnoreCase(almacenPrincipal));

        // Calcular distancia base
        int distancia = obtenerDistancia(almacenPrincipal, destino);
        
        // Calcular coste base del envío (usando el vehículo más económico)
        int costeBaseEnvio = distancia * 2; // 2€/km como mínimo (furgoneta)
        
        // Si es ruta marítima, usar barco como base
        if (esRutaMaritima(almacenPrincipal, destino)) {
            costeBaseEnvio = (int)(distancia * 3 * 1.5); // 3€/km * 1.5 por ser marítimo
        }
        
        // Si es ruta aérea, usar avión como base
        if (esIsla(almacenPrincipal) || esIsla(destino)) {
            costeBaseEnvio = (int)(distancia * 10 * 2.0); // 10€/km * 2.0 por ser aéreo
        }

        // Calcular pago base según peso, tipo y distancia
        int pagoBase = peso * 2; // 2€ por kg base
        
        // Ajustar pago según tipo de paquete
        switch (tipoPaquete) {
            case "REFRIGERADO":
                pagoBase *= 1.3; // 30% más
                break;
            case "CONGELADO":
                pagoBase *= 1.5; // 50% más
                break;
            case "ESCOLTADO":
                pagoBase *= 1.8; // 80% más
                break;
            case "PELIGROSO":
                pagoBase *= 1.6; // 60% más
                break;
            case "FRÁGIL":
                pagoBase *= 1.4; // 40% más
                break;
        }
        
        // Ajustar pago según distancia
        pagoBase = (int)(pagoBase * (1 + (distancia / 2000.0))); // Aumenta 1% por cada 20km
        
        // Ajustar pago según prioridad
        if (prioridad.equals("URGENTE")) {
            pagoBase *= 1.5; // 50% más
        } else if (prioridad.equals("BAJA")) {
            pagoBase *= 0.8; // 20% menos
        }
        
        // Asegurar que el pago sea al menos 1.5 veces el coste base del envío
        pagoBase = Math.max(pagoBase, (int)(costeBaseEnvio * 1.5));
        
        // Asegurar un pago mínimo para pedidos pequeños
        pagoBase = Math.max(pagoBase, 2000); // Mínimo 2000€ para cualquier pedido

        // Calcular fecha de entrega según la prioridad y el tipo de paquete
        Calendar fechaEntrega = (Calendar) fechaActual.clone();
        int diasBase;
        
        // Días base según tipo de paquete
        switch (tipoPaquete) {
            case "NORMAL":
                diasBase = 3;
                break;
            case "REFRIGERADO":
                diasBase = 2;
                break;
            case "CONGELADO":
                diasBase = 4;
                break;
            case "PELIGROSO":
                diasBase = 5;
                break;
            case "ESCOLTADO":
                diasBase = 2;
                break;
            case "FRÁGIL":
                diasBase = 2;
                break;
            default:
                diasBase = 3;
        }
        
        // Ajustar días según prioridad
        if (prioridad.equals("URGENTE")) {
            diasBase = Math.max(1, diasBase - 2);
        } else if (prioridad.equals("BAJA")) {
            diasBase += 2;
        }
        
        fechaEntrega.add(Calendar.DAY_OF_MONTH, diasBase);

        // Determinar si es un pedido de varios días según la dificultad
        int diasEntrega = 1;
        if (random.nextDouble() < calcularProbabilidadMultiDia()) {
            diasEntrega = 2 + random.nextInt(3);
            pagoBase *= diasEntrega;
        }

        return new Pedido(idPedido, cliente, carga, prioridad, pagoBase, diasEntrega, destino, fechaEntrega, peso, tipoPaquete);
    }

    /**
     * Calcula la probabilidad de que un pedido sea de varios días según la dificultad
     * @return double con la probabilidad
     */
    private double calcularProbabilidadMultiDia() {
        switch (dificultad) {
            case "easy":
                return 0.2; // 20% de probabilidad
            case "medium":
                return 0.4; // 40% de probabilidad
            case "hard":
                return 0.6; // 60% de probabilidad
            default:
                return 0.3;
        }
    }

    /**
     * Genera los pedidos del día según la dificultad
     */
    private void generarPedidosDia() {
        int cantidadPedidos = calcularCantidadPedidos();
        pedidosPendientes.clear();
        
        for (int i = 0; i < cantidadPedidos; i++) {
            Pedido pedido = generarPedidoAleatorio();
            pedidosPendientes.add(pedido);
            pedidos.put(pedido.getId(), pedido);
        }
        
        System.out.println("\n📦 Han entrado " + cantidadPedidos + " paquetes nuevos!");
    }

    /**
     * Calcula la cantidad de pedidos según la dificultad
     * @return int con la cantidad de pedidos
     */
    private int calcularCantidadPedidos() {
        int base = 2; // Pedidos base por día
        switch (dificultad) {
            case "easy":
                return base + (diaActual / 3); // Aumenta 1 cada 3 días
            case "medium":
                return base + (diaActual / 2); // Aumenta 1 cada 2 días
            case "hard":
                return base + diaActual; // Aumenta 1 cada día
            default:
                return base;
        }
    }

    /**
     * Calcula el ancho máximo necesario para una columna basado en su contenido
     * @param contenido Array de strings con el contenido de la columna
     * @param encabezado String con el encabezado de la columna
     * @return int con el ancho máximo necesario
     */
    private int calcularAnchoColumna(String[] contenido, String encabezado) {
        int anchoMaximo = encabezado.length();
        for (String item : contenido) {
            anchoMaximo = Math.max(anchoMaximo, item.length());
        }
        return anchoMaximo;
    }

    /**
     * Genera una línea separadora para una tabla
     * @param anchos Array con los anchos de cada columna
     * @return String con la línea separadora
     */
    private String generarLineaSeparadora(int[] anchos) {
        StringBuilder linea = new StringBuilder();
        for (int ancho : anchos) {
            linea.append("-".repeat(ancho + 2)).append("+");
        }
        return linea.toString();
    }

    /**
     * Genera una fila de tabla con el formato correcto
     * @param valores Array con los valores de cada columna
     * @param anchos Array con los anchos de cada columna
     * @return String con la fila formateada
     */
    private String generarFilaTabla(String[] valores, int[] anchos) {
        StringBuilder fila = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            fila.append(String.format(" %-" + anchos[i] + "s |", valores[i]));
        }
        return fila.toString();
    }

    /**
     * Muestra los pedidos pendientes
     */
    private void mostrarPedidosPendientes() {
        if (pedidosPendientes.isEmpty()) {
            System.out.println("\n📭 No hay pedidos pendientes");
            return;
        }

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"ID", "CLIENTE", "CARGA", "PRIORIDAD", "PESO", "DESTINO", "TIPO", "PAGO", "ENTREGA"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Pedido pedido : pedidosPendientes) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega()
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println("\n📦 PEDIDOS PENDIENTES:");
        
        // Mostrar encabezados
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Pedido pedido : pedidosPendientes) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega()
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }
    }

    /**
     * Muestra los pedidos en curso
     */
    private void mostrarPedidosEnCurso() {
        if (pedidosEnCurso.isEmpty()) {
            System.out.println("\n📦 No hay pedidos en curso");
            return;
        }

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"ID", "CLIENTE", "CARGA", "PRIORIDAD", "PESO", "DESTINO", "TIPO", "PAGO", "ENTREGA MÁXIMA", "ENTREGA PREVISTA"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Pedido pedido : pedidosEnCurso) {
            Calendar fechaPrevia = Calendar.getInstance();
            fechaPrevia.setTime(fechaActual.getTime());
            fechaPrevia.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
            
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega(),
                formatoFecha.format(fechaPrevia.getTime())
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println("\n📦 PEDIDOS EN CURSO:");
        
        // Mostrar encabezados
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Pedido pedido : pedidosEnCurso) {
            Calendar fechaPrevia = Calendar.getInstance();
            fechaPrevia.setTime(fechaActual.getTime());
            fechaPrevia.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
            
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega(),
                formatoFecha.format(fechaPrevia.getTime())
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }
    }

    /**
     * Obtiene la distancia entre dos provincias
     * @param origen Provincia de origen
     * @param destino Provincia de destino
     * @return int con la distancia en km
     */
    private int obtenerDistancia(String origen, String destino) {
        int indiceOrigen = -1;
        int indiceDestino = -1;
        
        for (int i = 0; i < PROVINCIAS.length; i++) {
            if (PROVINCIAS[i].equalsIgnoreCase(origen)) {
                indiceOrigen = i;
            }
            if (PROVINCIAS[i].equalsIgnoreCase(destino)) {
                indiceDestino = i;
            }
        }
        
        if (indiceOrigen == -1 || indiceDestino == -1) {
            return 0;
        }
        
        return DISTANCIAS[indiceOrigen][indiceDestino];
    }

    /**
     * Verifica si una ruta es marítima
     * @param origen Provincia de origen
     * @param destino Provincia de destino
     * @return true si es una ruta marítima, false si no
     */
    private boolean esRutaMaritima(String origen, String destino) {
        // Normalizar nombres de provincias
        String origenNormalizado = normalizarNombreProvincia(origen);
        String destinoNormalizado = normalizarNombreProvincia(destino);
        
        boolean origenEsIsla = esIsla(origenNormalizado);
        boolean destinoEsIsla = esIsla(destinoNormalizado);
        boolean origenEsCostero = esProvinciaCostera(origenNormalizado);
        boolean destinoEsCostero = esProvinciaCostera(destinoNormalizado);
        
        // Es ruta marítima si:
        // 1. El origen es una isla y el destino es costero o isla
        // 2. El origen es costero y el destino es una isla
        // 3. Ambos son costeros
        return (origenEsIsla && (destinoEsCostero || destinoEsIsla)) ||
               (origenEsCostero && destinoEsIsla) ||
               (origenEsCostero && destinoEsCostero);
    }

    /**
     * Verifica si una provincia es costera
     * @param provincia Nombre de la provincia
     * @return true si es costera, false si no
     */
    private boolean esProvinciaCostera(String provincia) {
        // Normalizar el nombre de la provincia
        String provinciaNormalizada = normalizarNombreProvincia(provincia);
        
        for (String puerto : PROVINCIAS_MARITIMAS) {
            if (puerto.equalsIgnoreCase(provinciaNormalizada)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si una provincia es una isla
     * @param provincia Nombre de la provincia
     * @return true si es una isla, false si no
     */
    private boolean esIsla(String provincia) {
        // Normalizar el nombre de la provincia
        String provinciaNormalizada = normalizarNombreProvincia(provincia);
        
        return provinciaNormalizada.equalsIgnoreCase("Palma de Mallorca") || 
               provinciaNormalizada.equalsIgnoreCase("Las Palmas");
    }

    /**
     * Normaliza el nombre de una provincia
     * @param provincia Nombre de la provincia a normalizar
     * @return String con el nombre normalizado
     */
    private String normalizarNombreProvincia(String provincia) {
        // Reemplazar guiones bajos por espacios
        String nombre = provincia.replace("_", " ");
        
        // Capitalizar cada palabra
        String[] palabras = nombre.split(" ");
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                resultado.append(Character.toUpperCase(palabras[i].charAt(0)));
                resultado.append(palabras[i].substring(1).toLowerCase());
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }
        
        return resultado.toString();
    }

    /**
     * Calcula el coste de envío para un vehículo
     * @param vehiculo Vehículo que realizará el envío
     * @param origen Provincia de origen
     * @param destino Provincia de destino
     * @return int con el coste total
     */
    private int calcularCosteEnvio(Vehiculo vehiculo, String origen, String destino) {
        int distancia = obtenerDistancia(origen, destino);
        int costeBase = vehiculo.getCostePorKm() * distancia;
        
        // Ajustes específicos para barcos
        if (vehiculo.getTipo().equals("Barco")) {
            // Coste adicional por ser ruta marítima
            costeBase *= 1.5; // 50% más caro que la ruta terrestre equivalente
            
            // Coste adicional por tipo de ruta marítima
            if (esIsla(origen) && esIsla(destino)) {
                costeBase *= 1.3; // 30% más caro entre islas
            } else if (esIsla(origen) || esIsla(destino)) {
                costeBase *= 1.2; // 20% más caro entre isla y costa
            }
        }
        
        // Ajustes específicos para aviones
        if (vehiculo.getTipo().equals("Avión")) {
            // Coste adicional por ser ruta aérea
            costeBase *= 2.0; // 100% más caro que la ruta terrestre equivalente
            
            // Coste adicional por tipo de ruta aérea
            if (esIsla(origen) && esIsla(destino)) {
                costeBase *= 1.5; // 50% más caro entre islas
            } else if (esIsla(origen) || esIsla(destino)) {
                costeBase *= 1.3; // 30% más caro entre isla y costa
            }
        }
        
        return costeBase;
    }

    /**
     * Verifica si existe una ruta terrestre válida entre dos provincias
     * @param origen Provincia de origen
     * @param destino Provincia de destino
     * @return true si existe una ruta terrestre válida, false si no
     */
    private boolean existeRutaTerrestre(String origen, String destino) {
        // Normalizar nombres de provincias
        String origenNormalizado = normalizarNombreProvincia(origen);
        String destinoNormalizado = normalizarNombreProvincia(destino);
        
        // Si alguna de las provincias es una isla, no hay ruta terrestre
        if (esIsla(origenNormalizado) || esIsla(destinoNormalizado)) {
            return false;
        }
        
        // Obtener índices de las provincias
        int indiceOrigen = -1;
        int indiceDestino = -1;
        
        for (int i = 0; i < PROVINCIAS.length; i++) {
            if (PROVINCIAS[i].equalsIgnoreCase(origenNormalizado)) {
                indiceOrigen = i;
            }
            if (PROVINCIAS[i].equalsIgnoreCase(destinoNormalizado)) {
                indiceDestino = i;
            }
        }
        
        if (indiceOrigen == -1 || indiceDestino == -1) {
            return false;
        }
        
        // Verificar si hay una distancia terrestre válida
        int distancia = DISTANCIAS[indiceOrigen][indiceDestino];
        return distancia > 0;
    }

    /**
     * Verifica si un vehículo puede realizar una ruta específica
     * @param vehiculo Vehículo a verificar
     * @param origen Provincia de origen
     * @param destino Provincia de destino
     * @return true si el vehículo puede realizar la ruta, false si no
     */
    private boolean vehiculoPuedeRealizarRuta(Vehiculo vehiculo, String origen, String destino) {
        switch (vehiculo.getTipo()) {
            case "Furgoneta":
            case "Camión":
            case "Camion":
                return existeRutaTerrestre(origen, destino);
            case "Barco":
                return esRutaMaritima(origen, destino);
            case "Avión":
                return true; // Los aviones pueden ir a cualquier parte
            default:
                return false;
        }
    }

    /**
     * Muestra los vehículos disponibles para un pedido
     * @param pedido Pedido a transportar
     */
    private void mostrarVehiculosDisponibles(Pedido pedido) {
        System.out.println("\n🚗 VEHÍCULOS DISPONIBLES:");
        
        // Calcular anchos máximos para cada columna
        String[] encabezados = {"TIPO", "ID", "CAPACIDAD", "VELOCIDAD", "COSTE/KM", "COSTE TOTAL", "DÍA LLEGADA", "PENALIZACIÓN", "CARGAS PERMITIDAS"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Vehiculo vehiculo : flota) {
            if (!vehiculo.estaDisponible() || !vehiculo.puedeTransportarTipo(pedido.getTipoPaquete())) {
                continue;
            }
            
            // Verificar si el vehículo puede realizar la ruta
            if (!vehiculoPuedeRealizarRuta(vehiculo, almacenPrincipal, pedido.getDestino())) {
                continue;
            }
            
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());
            int horasViaje = vehiculo.calcularTiempoEntrega(obtenerDistancia(almacenPrincipal, pedido.getDestino()));
            int diasViaje = (int) Math.ceil(horasViaje / 8.0);
            
            Calendar fechaLlegada = Calendar.getInstance();
            fechaLlegada.setTime(fechaActual.getTime());
            fechaLlegada.add(Calendar.DAY_OF_MONTH, diasViaje);
            
            Calendar fechaEntrega = Calendar.getInstance();
            fechaEntrega.setTime(fechaActual.getTime());
            fechaEntrega.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
            
            int diasRetraso = (int) ((fechaLlegada.getTimeInMillis() - fechaEntrega.getTimeInMillis()) / (1000 * 60 * 60 * 24));
            
            String penalizacion;
            if (diasRetraso <= 0) {
                penalizacion = "Sin penalización";
            } else if (diasRetraso == 1) {
                penalizacion = "50% del pago";
            } else if (diasRetraso == 2) {
                penalizacion = "10% del pago";
            } else {
                penalizacion = "Multa 65%";
            }
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                "$" + costeTotal,
                formatoFecha.format(fechaLlegada.getTime()),
                penalizacion,
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Vehiculo vehiculo : flota) {
            if (!vehiculo.estaDisponible() || !vehiculo.puedeTransportarTipo(pedido.getTipoPaquete())) {
                continue;
            }
            
            if (!vehiculoPuedeRealizarRuta(vehiculo, almacenPrincipal, pedido.getDestino())) {
                continue;
            }
            
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());
            int horasViaje = vehiculo.calcularTiempoEntrega(obtenerDistancia(almacenPrincipal, pedido.getDestino()));
            int diasViaje = (int) Math.ceil(horasViaje / 8.0);
            
            Calendar fechaLlegada = Calendar.getInstance();
            fechaLlegada.setTime(fechaActual.getTime());
            fechaLlegada.add(Calendar.DAY_OF_MONTH, diasViaje);
            
            Calendar fechaEntrega = Calendar.getInstance();
            fechaEntrega.setTime(fechaActual.getTime());
            fechaEntrega.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
            
            int diasRetraso = (int) ((fechaLlegada.getTimeInMillis() - fechaEntrega.getTimeInMillis()) / (1000 * 60 * 60 * 24));
            
            String penalizacion;
            if (diasRetraso <= 0) {
                penalizacion = "Sin penalización";
            } else if (diasRetraso == 1) {
                penalizacion = "50% del pago";
            } else if (diasRetraso == 2) {
                penalizacion = "10% del pago";
            } else {
                penalizacion = "Multa 65%";
            }
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                "$" + costeTotal,
                formatoFecha.format(fechaLlegada.getTime()),
                penalizacion,
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }
    }

    /**
     * Permite gestionar un pedido
     */
    private void gestionarPedido() {
        if (pedidosPendientes.isEmpty()) {
            System.out.println("\n📭 No hay pedidos pendientes para gestionar");
            return;
        }

        mostrarPedidosPendientes();
        System.out.print("\nIngrese ID del pedido a gestionar: ");
        String idPedido = scanner.nextLine();

        Pedido pedido = pedidos.get(idPedido);
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado");
            return;
        }

        // Verificar si hay vehículos disponibles para esta ruta
        boolean hayVehiculosDisponibles = false;
        for (Vehiculo v : flota) {
            if (v.estaDisponible() && 
                v.puedeTransportarTipo(pedido.getTipoPaquete()) &&
                vehiculoPuedeRealizarRuta(v, almacenPrincipal, pedido.getDestino())) {
                hayVehiculosDisponibles = true;
                break;
            }
        }

        if (!hayVehiculosDisponibles) {
            System.out.println("\n❌ No hay vehículos disponibles para realizar esta ruta");
            System.out.println("   - Origen: " + almacenPrincipal);
            System.out.println("   - Destino: " + pedido.getDestino());
            System.out.println("   - Tipo de carga: " + pedido.getTipoPaquete());
            return;
        }

        System.out.println("\n¿Qué desea hacer con el pedido #" + idPedido + "?");
        System.out.println("01. Enviar");
        System.out.println("02. Rechazar (Multa: $" + calcularMultaRechazo(pedido) + ")");
        System.out.print("\nOpción: ");
        String opcion = scanner.nextLine();

        if (opcion.equals("02") || opcion.equals("2")) {
            System.out.println("\n⚠️ ¿Está seguro de rechazar el pedido #" + idPedido + "?");
            System.out.println("   - Multa por rechazo: $" + calcularMultaRechazo(pedido));
            System.out.print("   - Confirmar (S/N): ");
            
            String confirmacion = scanner.nextLine().toUpperCase();
            if (confirmacion.equals("S")) {
                pedidosPendientes.remove(pedido);
                System.out.println("❌ Pedido #" + idPedido + " rechazado");
                System.out.println("💰 Multa aplicada: $" + calcularMultaRechazo(pedido));
            }
            return;
        } else if (!opcion.equals("01") && !opcion.equals("1")) {
            System.out.println("\n❌ Opción no válida");
            gestionarPedido();
            return;
        }

        // Mostrar vehículos disponibles
        mostrarVehiculosDisponibles(pedido);
        if (pedidosPendientes.isEmpty()) {
            return;
        }

        System.out.print("\nIngrese ID del vehículo a utilizar: ");
        String idVehiculo = scanner.nextLine().toUpperCase();
        
        Vehiculo vehiculoSeleccionado = null;
        for (Vehiculo v : flota) {
            if (v.getId().equals(idVehiculo)) {
                vehiculoSeleccionado = v;
                break;
            }
        }

        if (vehiculoSeleccionado == null) {
            System.out.println("❌ ID de vehículo no válido");
            return;
        }

        // Calcular costo total
        int costoTotal = calcularCosteEnvio(vehiculoSeleccionado, almacenPrincipal, pedido.getDestino());
        
        // Verificar balance
        if (jugador.getPresupuesto() < costoTotal) {
            System.out.println("❌ Balance insuficiente para realizar el envío");
            return;
        }

        // Asignar vehículo al pedido
        vehiculoSeleccionado.asignarPedido(pedido);
        pedido.setTransporteAsignado(vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());

        // Resolver incidente si ocurre
        if (random.nextBoolean()) {
            resolverIncidente(pedido);
        }

        pedidosPendientes.remove(pedido);
        pedidosEnCurso.add(pedido);
        System.out.println("\n✅ Pedido #" + idPedido + " gestionado exitosamente");
        System.out.println("   - Vehículo asignado: " + vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
        System.out.println("   - Costo total: $" + costoTotal);
    }

    /**
     * Resuelve un incidente para un pedido
     * @param pedido Pedido afectado
     */
    private void resolverIncidente(Pedido pedido) {
        String tipoTransporte = pedido.getTransporteAsignado().split(" ")[0];
        String idVehiculo = pedido.getTransporteAsignado().split(" ")[1];
        String[] incidentesTerrestres = {
            "Caída de árbol en la carretera",
            "Accidente de tráfico",
            "Obras en la vía",
            "Protesta de agricultores",
            "Control policial",
            "Avería mecánica",
            "Desprendimiento de rocas",
            "Nieve en la carretera",
            "Niebla densa"
        };

        String[] incidentesAereos = {
            "Turbulencias severas",
            "Retraso en el despegue",
            "Problemas técnicos en el avión",
            "Mal tiempo en el aeropuerto",
            "Huelga de controladores",
            "Restricciones de espacio aéreo",
            "Problemas de navegación",
            "Viento fuerte en pista"
        };

        String[] incidentesMaritimos = {
            "Tormenta en el mar",
            "Niebla en la costa",
            "Problemas en el puerto",
            "Avería en el motor",
            "Oleaje fuerte",
            "Retraso en la descarga",
            "Problemas de navegación",
            "Control de aduanas"
        };

        String incidente;
        int idIncidente = 100 + random.nextInt(900);

        // Seleccionar incidente según el tipo de transporte
        switch (tipoTransporte) {
            case "Furgoneta":
            case "Camión":
                incidente = incidentesTerrestres[random.nextInt(incidentesTerrestres.length)];
                break;
            case "Avión":
                incidente = incidentesAereos[random.nextInt(incidentesAereos.length)];
                break;
            case "Barco":
                incidente = incidentesMaritimos[random.nextInt(incidentesMaritimos.length)];
                break;
            default:
                incidente = "Incidente desconocido";
        }

        // Obtener la fecha límite de entrega del pedido
        Calendar fechaLimite = Calendar.getInstance();
        fechaLimite.setTime(fechaActual.getTime());
        fechaLimite.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
        
        // Calcular fechas de llegada para cada opción
        Calendar fechaEspera = (Calendar) fechaActual.clone();
        fechaEspera.add(Calendar.DAY_OF_MONTH, 3); // 3 días de retraso por esperar
        
        Calendar fechaDesvio = (Calendar) fechaActual.clone();
        fechaDesvio.add(Calendar.DAY_OF_MONTH, 1); // 1 día de retraso por desviar

        // Calcular los días de retraso para cada opción
        int diasRetrasoEspera = calcularDiasRetraso(fechaEspera, fechaLimite);
        int diasRetrasoDesvio = calcularDiasRetraso(fechaDesvio, fechaLimite);

        System.out.println("\n❗ ALERTA: Incidente #" + idIncidente + " - " + incidente);
        System.out.println("   - Riesgo: Retraso en entrega");
        System.out.println("   - Fecha límite de entrega: " + formatoFecha.format(fechaLimite.getTime()));
        System.out.println("   - Soluciones posibles:");
        System.out.println("     01. Esperar");
        System.out.println("         • Nueva fecha de entrega: " + formatoFecha.format(fechaEspera.getTime()));
        if (diasRetrasoEspera > 0) {
            if (diasRetrasoEspera == 1) {
                System.out.println("         • Penalización: 50% del pago");
            } else if (diasRetrasoEspera == 2) {
                System.out.println("         • Penalización: 10% del pago");
            } else {
                System.out.println("         • Penalización: 65% de multa");
            }
        } else {
            System.out.println("         • Sin penalización");
        }
        
        System.out.println("     02. Desviar ruta (Coste adicional: $1,000)");
        System.out.println("         • Nueva fecha de entrega: " + formatoFecha.format(fechaDesvio.getTime()));
        if (diasRetrasoDesvio > 0) {
            if (diasRetrasoDesvio == 1) {
                System.out.println("         • Penalización: 50% del pago");
            } else if (diasRetrasoDesvio == 2) {
                System.out.println("         • Penalización: 10% del pago");
            } else {
                System.out.println("         • Penalización: 65% de multa");
            }
        } else {
            System.out.println("         • Sin penalización");
        }
        
        System.out.print("\nSeleccione solución (01-02): ");
        String solucion = scanner.nextLine();

        System.out.println("\n🛠 Aplicando patrón *Template Method*:");
        System.out.println("   1. Identificando causa: " + incidente);
        System.out.println("   2. Asignando recursos...");
        
        if (solucion.equals("02") || solucion.equals("2")) {
            System.out.println("   3. Desviando ruta...");
            System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaDesvio.getTime()));
            if (diasRetrasoDesvio > 0) {
                if (diasRetrasoDesvio == 1) {
                    System.out.println("   - Penalización: 50% del pago");
                } else if (diasRetrasoDesvio == 2) {
                    System.out.println("   - Penalización: 10% del pago");
                } else {
                    System.out.println("   - Penalización: 65% de multa");
                }
                aplicarPenalizacion(pedido, diasRetrasoDesvio);
            }
            pedido.setDiasRestantes(pedido.getDiasRestantes() + 1);
            
            // Actualizar el coste total del envío
            int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
            int costeAdicional = 1000; // Coste por desviar ruta
            
            // Buscar el vehículo en la flota
            Vehiculo vehiculo = null;
            for (Vehiculo v : flota) {
                if (v.getId().equals(idVehiculo)) {
                    vehiculo = v;
                    break;
                }
            }
            
            if (vehiculo != null) {
                int costeTotal = distancia * vehiculo.getCostePorKm() + costeAdicional;
                pedido.setPago(costeTotal);
                System.out.println("💰 Coste total actualizado: $" + costeTotal);
            }
        } else if (solucion.equals("01") || solucion.equals("1")) {
            System.out.println("   3. Esperando condiciones...");
            System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaEspera.getTime()));
            if (diasRetrasoEspera > 0) {
                if (diasRetrasoEspera == 1) {
                    System.out.println("   - Penalización: 50% del pago");
                } else if (diasRetrasoEspera == 2) {
                    System.out.println("   - Penalización: 10% del pago");
                } else {
                    System.out.println("   - Penalización: 65% de multa");
                }
                aplicarPenalizacion(pedido, diasRetrasoEspera);
            }
            pedido.setDiasRestantes(pedido.getDiasRestantes() + 3);
        } else {
            System.out.println("\n❌ Opción no válida, seleccionando esperar por defecto");
            System.out.println("   3. Esperando condiciones...");
            System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaEspera.getTime()));
            if (diasRetrasoEspera > 0) {
                if (diasRetrasoEspera == 1) {
                    System.out.println("   - Penalización: 50% del pago");
                } else if (diasRetrasoEspera == 2) {
                    System.out.println("   - Penalización: 10% del pago");
                } else {
                    System.out.println("   - Penalización: 65% de multa");
                }
                aplicarPenalizacion(pedido, diasRetrasoEspera);
            }
            pedido.setDiasRestantes(pedido.getDiasRestantes() + 3);
        }
    }
    
    private int calcularDiasRetraso(Calendar fechaLlegada, Calendar fechaLimite) {
        long diffMillis = fechaLlegada.getTimeInMillis() - fechaLimite.getTimeInMillis();
        int dias = (int) (diffMillis / (1000 * 60 * 60 * 24));
        return Math.max(0, dias); // Retornar 0 si la fecha es anterior o igual
    }
    
    private void aplicarPenalizacion(Pedido pedido, int diasRetraso) {
        double porcentajePenalizacion;
        if (diasRetraso == 1) {
            porcentajePenalizacion = 0.50; // 50% de penalización
        } else if (diasRetraso == 2) {
            porcentajePenalizacion = 0.10; // 10% de penalización
        } else {
            porcentajePenalizacion = 0.65; // 65% de penalización
        }
        
        double penalizacion = pedido.getPago() * porcentajePenalizacion;
        pedido.setPago((int)(pedido.getPago() - penalizacion));
    }

    /**
     * Muestra las estadísticas del juego
     */
    private void mostrarEstadisticas() {
        System.out.println("\n📊 MÉTRICAS ACTUALES:");
        System.out.println("   - 👤 " + jugador.getNombre() + ", aquí están tus métricas:");
        System.out.println("   - 🎮 Dificultad: " + dificultad.toUpperCase());
        System.out.println("   - 💰 Balance: $" + jugador.getPresupuesto());
        System.out.println("   - 😊 Satisfacción clientes: " + satisfaccionClientes + "%");
        System.out.println("   - 🚚 Envíos exitosos: " + enviosExitosos + "/" + enviosTotales);
        System.out.println("   - 📦 Pedidos pendientes: " + pedidosPendientes.size());
        System.out.println("   - 📦 Pedidos en curso: " + pedidosEnCurso.size());
        System.out.println("   - 🚗 Flota de vehículos: " + flota.size());
        System.out.println("\n   🚗 VEHÍCULOS DISPONIBLES:");
        
        // Contar vehículos por tipo
        Map<String, Integer> conteoVehiculos = new HashMap<>();
        for (Vehiculo v : flota) {
            conteoVehiculos.merge(v.getTipo(), 1, Integer::sum);
        }
        
        // Mostrar total por tipo
        for (Map.Entry<String, Integer> entry : conteoVehiculos.entrySet()) {
            String unidad = entry.getValue() == 1 ? "unidad" : "unidades";
            System.out.println("      • " + entry.getKey() + ": " + entry.getValue() + " " + unidad);
        }
    }

    /**
     * Procesa los accidentes aleatorios
     */
    private void procesarAccidentes() {
        for (Pedido pedido : new ArrayList<>(pedidosEnCurso)) {
            if (random.nextDouble() < 0.15) { // 15% de probabilidad de accidente
                String tipoTransporte = pedido.getTransporteAsignado().split(" ")[0];
                String incidente;
                int costeAdicional = 0;

                switch (tipoTransporte) {
                    case "Furgoneta":
                    case "Camión":
                        incidente = "Accidente en carretera";
                        costeAdicional = 2000;
                        break;
                    case "Avión":
                        incidente = "Turbulencias severas";
                        costeAdicional = 5000;
                        break;
                    case "Barco":
                        incidente = "Tormenta en el mar";
                        costeAdicional = 3000;
                        break;
                    default:
                        incidente = "Incidente desconocido";
                        costeAdicional = 1000;
                }

                System.out.println("\n⚠️ ¡INCIDENTE! El paquete #" + pedido.getId() + " ha sufrido " + incidente);
                System.out.println("   - Cliente: " + pedido.getCliente());
                System.out.println("   - Carga: " + pedido.getCarga());
                System.out.println("   - Coste adicional: $" + costeAdicional);
                
                pedido.setDiasRestantes(pedido.getDiasRestantes() + 1);
                satisfaccionClientes -= 5;
            }
        }
    }

    /**
     * Procesa el pago de impuestos
     */
    private void procesarImpuestos() {
        if (diaActual % calcularDiasImpuestos() == 0) {
            int impuestos = (int)(beneficiosAcumulados * TASA_IMPUESTOS);
            System.out.println("\n💰 " + jugador.getNombre() + ", es hora de pagar impuestos");
            System.out.println("   - Debes pagar el " + (TASA_IMPUESTOS * 100) + "% de tus beneficios");
            System.out.println("   - Beneficios acumulados: $" + beneficiosAcumulados);
            System.out.println("   - Impuestos a pagar: $" + impuestos);
            
            beneficiosAcumulados = 0;
        }
    }

    /**
     * Genera vehículos aleatorios para el mercado
     */
    private void generarVehiculosMercado() {
        vehiculosMercado = new ArrayList<>();
        Random random = new Random();
        Set<String> idsUsados = new HashSet<>();
        
        // Generar 3 vehículos diferentes
        while (vehiculosMercado.size() < 3) {
            String tipo = random.nextBoolean() ? "Furgoneta" : "Camión";
            // Generar ID con formato 1Letra2Numeros
            String id = tipo.charAt(0) + String.format("%02d", random.nextInt(100));
            
            // Verificar que el ID no esté repetido
            if (idsUsados.contains(id)) {
                continue;
            }
            idsUsados.add(id);
            
            // Generar tipos de carga permitidos aleatorios (mínimo 1, máximo 3)
            int numTipos = 1 + random.nextInt(3);
            List<String> tiposPermitidos = new ArrayList<>();
            
            for (int j = 0; j < numTipos; j++) {
                String tipoCarga;
                do {
                    tipoCarga = TIPOS_CARGA[random.nextInt(TIPOS_CARGA.length)];
                } while (tiposPermitidos.contains(tipoCarga));
                tiposPermitidos.add(tipoCarga);
            }
            
            vehiculosMercado.add(new Vehiculo(tipo, id, tiposPermitidos.toArray(new String[0])));
        }
    }

    /**
     * Avanza al siguiente día
     */
    private void pasarDia() {
        if (!pedidosPendientes.isEmpty()) {
            System.out.println("\n❌ " + jugador.getNombre() + ", no puedes pasar al siguiente día con pedidos pendientes");
            return;
        }

        diaActual++;
        fechaActual.add(Calendar.DAY_OF_MONTH, 1); // Añadir un día a la fecha actual
        
        System.out.println("\n==================================================");
        System.out.println("📅 DÍA " + diaActual + " (" + formatoFecha.format(fechaActual.getTime()) + ") | ENTREGA FINAL");
        System.out.println("==================================================");
        
        // Procesar accidentes
        procesarAccidentes();
        
        // Procesar envíos
        for (Pedido pedido : new ArrayList<>(pedidosEnCurso)) {
            pedido.reducirDiasRestantes();
            
            if (pedido.getDiasRestantes() <= 0) {
                int diasRetraso = Math.abs(pedido.getDiasRestantes());
                int pagoOriginal = pedido.getPago();
                int multa = 0;
                boolean exito = true;
                String mensaje = "";

                if (diasRetraso == 0) {
                    // Entrega a tiempo
                    mensaje = "✅ " + jugador.getNombre() + ", el envío #" + pedido.getId() + " se completó exitosamente";
                    multa = 0;
                } else if (diasRetraso == 1) {
                    // 1 día de retraso: 35% de multa
                    multa = (int)(pagoOriginal * 0.35);
                    mensaje = "⚠️ " + jugador.getNombre() + ", el envío #" + pedido.getId() + " se completó con 1 día de retraso";
                } else if (diasRetraso == 2) {
                    // 2 días de retraso: 90% de multa
                    multa = (int)(pagoOriginal * 0.90);
                    mensaje = "⚠️ " + jugador.getNombre() + ", el envío #" + pedido.getId() + " se completó con 2 días de retraso";
                } else {
                    // Más de 2 días: fallo y 150% de multa
                    multa = (int)(pagoOriginal * 1.50);
                    exito = false;
                    mensaje = "❌ " + jugador.getNombre() + ", el envío #" + pedido.getId() + " falló por exceso de retraso";
                }

                int ganancia = pagoOriginal - multa;
                if (exito) {
                    enviosExitosos++;
                    jugador.recuperarPresupuesto(ganancia);
                    beneficiosAcumulados += ganancia;
                } else {
                    satisfaccionClientes -= 10;
                }

                System.out.println(mensaje);
                System.out.println("💰 Pago original: $" + pagoOriginal);
                System.out.println("💰 Multa por retraso: $" + multa);
                System.out.println("💰 Ganancia final: $" + ganancia);
                
                enviosTotales++;
                pedidosEnCurso.remove(pedido);
            }
        }
        
        // Procesar impuestos
        procesarImpuestos();
        
        // Generar nuevos vehículos en el mercado
        generarVehiculosMercado();
        
        pedidos.clear();
        generarPedidosDia();
        mostrarEstadisticas();
    }

    /**
     * Muestra la pantalla de fin de juego
     */
    private void mostrarGameOver() {
        System.out.println("\n==============================================");
        System.out.println("🎮 GAME OVER");
        System.out.println("==============================================");
        System.out.println("💰 Balance final: $" + jugador.getPresupuesto());
        System.out.println("😊 Satisfacción final: " + satisfaccionClientes + "%");
        System.out.println("🚚 Envíos totales: " + enviosTotales);
        System.out.println("✅ Envíos exitosos: " + enviosExitosos);
    }
} 