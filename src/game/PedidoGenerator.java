package game;

import java.util.*;
import strategy.PedidoStrategy;
import strategy.PedidoFacilStrategy;
import strategy.PedidoDificilStrategy;
import decorator.IVehiculo;

public class PedidoGenerator {
    private Random random;
    private Calendar fechaActual;
    private List<IVehiculo> flota;
    private String almacenPrincipal;
    private String dificultad;
    private PedidoStrategy pedidoStrategy;

    public PedidoGenerator(Calendar fechaActual, List<IVehiculo> flota, String almacenPrincipal, String dificultad) {
        this.random = new Random();
        this.fechaActual = fechaActual;
        this.flota = flota;
        this.almacenPrincipal = almacenPrincipal;
        this.dificultad = dificultad;
        this.pedidoStrategy = crearEstrategia(dificultad);
    }

    private PedidoStrategy crearEstrategia(String dificultad) {
        if (dificultad.equalsIgnoreCase("facil")) {
            return new PedidoFacilStrategy(this);
        } else {
            return new PedidoDificilStrategy(this);
        }
    }

    public Pedido generarPedidoAleatorio() {
        String[] clientes = {
            "Banco Santander", "BBVA", "CaixaBank", "Iberdrola", "Telefónica", 
            "Repsol", "Inditex", "Mercadona", "El Corte Inglés", "AENA",
            "Renfe", "Seat", "Naturgy", "Endesa", "Mapfre"
        };
        
        String[] ciudades = CityConstants.CIUDADES;
        
        Map<String, String[]> cargasPorTipo = new HashMap<>();
        cargasPorTipo.put("NORMAL", new String[]{"Materiales Construcción", "Piezas Industriales", "Equipaje VIP", "Material Educativo"});
        cargasPorTipo.put("REFRIGERADO", new String[]{"Vacunas", "Medicamentos", "Alimentos Frescos", "Mariscos Frescos"});
        cargasPorTipo.put("CONGELADO", new String[]{"Productos Congelados", "Muestras Biológicas", "Alimentos Ultra Congelados"});
        cargasPorTipo.put("PELIGROSO", new String[]{"Productos Químicos", "Materiales Explosivos", "Residuos Tóxicos", "Combustibles"});
        cargasPorTipo.put("ESCOLTADO", new String[]{"Joyas Valiosas", "Obras de Arte", "Dinero", "Documentos Secretos"});
        cargasPorTipo.put("FRÁGIL", new String[]{"Equipos Médicos", "Electrónicos", "Flores Exóticas", "Instrumentos Musicales", "Antigüedades"});
        cargasPorTipo.put("PERECEDERO", new String[]{"Frutas Frescas", "Verduras", "Lácteos", "Carnes"});
        cargasPorTipo.put("ALTO_VALOR", new String[]{"Obras de Arte", "Metales Preciosos", "Electrónica de Alta Gama", "Documentos Confidenciales"});
        cargasPorTipo.put("SERES_VIVOS", new String[]{"Animales Domésticos", "Ganado", "Aves", "Peces"});
        
        String[] prioridades = {"URGENTE", "NORMAL", "BAJA"};
        String[] tiposPaquetes = {"NORMAL", "REFRIGERADO", "CONGELADO", "ESCOLTADO", "PELIGROSO", "FRÁGIL", "PERECEDERO", "ALTO_VALOR", "SERES_VIVOS"};

        String tipoPaquete = tiposPaquetes[random.nextInt(tiposPaquetes.length)];
        String[] cargasDisponibles = cargasPorTipo.get(tipoPaquete);
        String carga = cargasDisponibles[random.nextInt(cargasDisponibles.length)];
        
        Map<String, String[]> clientesPorCarga = new HashMap<>();
        clientesPorCarga.put("Vacunas", new String[]{"Hospital General", "Farmacéutica Pfizer", "Clínica Salud"});
        clientesPorCarga.put("Medicamentos", new String[]{"Farmacia Central", "Laboratorios Roche", "Distribuidora Médica"});
        clientesPorCarga.put("Alimentos Frescos", new String[]{"Supermercado Local", "Distribuidora de Alimentos", "Mercado Central"});
        clientesPorCarga.put("Joyas Valiosas", new String[]{"Joyería El Brillante", "Casa de Subastas", "Banco de Valores"});
        clientesPorCarga.put("Materiales Construcción", new String[]{"Constructora XYZ", "Ferretería Central", "Distribuidora de Materiales"});
        clientesPorCarga.put("Piezas Industriales", new String[]{"Fábrica de Motores", "Industria Mecánica", "Proveedor de Equipos"});
        clientesPorCarga.put("Equipaje VIP", new String[]{"Aeropuerto Internacional", "Agencia de Viajes", "Hotel de Lujo"});
        clientesPorCarga.put("Material Educativo", new String[]{"Escuela Primaria", "Universidad Nacional", "Editorial Académica"});
        clientesPorCarga.put("Mariscos Frescos", new String[]{"Restaurante Gourmet", "Mercado de Pescados", "Distribuidora de Mariscos"});
        clientesPorCarga.put("Electrónicos", new String[]{"Tienda de Electrónica", "Distribuidor Electrónico", "Fabricante de Componentes"});
        clientesPorCarga.put("Lácteos", new String[]{"Supermercado Local", "Distribuidora de Lácteos", "Fábrica de Quesos"});

        String cliente;
        if (clientesPorCarga.containsKey(carga)) {
            String[] clientesRelacionados = clientesPorCarga.get(carga);
            cliente = clientesRelacionados[random.nextInt(clientesRelacionados.length)];
        } else {
            cliente = clientes[random.nextInt(clientes.length)];
        }

        while (!clientesPorCarga.containsKey(carga) || !Arrays.asList(clientesPorCarga.get(carga)).contains(cliente)) {
            tipoPaquete = tiposPaquetes[random.nextInt(tiposPaquetes.length)];
            cargasDisponibles = cargasPorTipo.get(tipoPaquete);
            carga = cargasDisponibles[random.nextInt(cargasDisponibles.length)];

            if (clientesPorCarga.containsKey(carga)) {
                String[] clientesRelacionados = clientesPorCarga.get(carga);
                cliente = clientesRelacionados[random.nextInt(clientesRelacionados.length)];
            } else {
                cliente = clientes[random.nextInt(clientes.length)];
            }
        }
        
        String prioridad = prioridades[random.nextInt(prioridades.length)];
        String idPedido = "P" + (100 + random.nextInt(900));
        int peso = calcularPeso(tipoPaquete);

        String origen = almacenPrincipal;
        String destino;
        do {
            destino = ciudades[random.nextInt(ciudades.length)];
        } while (destino.equals(origen));

        int costeMinimo = calcularCosteMinimo(tipoPaquete, origen, destino);
        int pagoBase = calcularPagoBase(costeMinimo, prioridad);
        Calendar fechaEntrega = calcularFechaEntrega(tipoPaquete, prioridad);

        return new Pedido(idPedido, cliente, carga, prioridad, pagoBase, 1, destino, fechaEntrega, peso, tipoPaquete);
    }

    private int calcularPeso(String tipoPaquete) {
        switch (tipoPaquete) {
            case "NORMAL":
                return 1000 + random.nextInt(4000);
            case "REFRIGERADO":
                return 500 + random.nextInt(2000);
            case "CONGELADO":
                return 1000 + random.nextInt(3000);
            case "PELIGROSO":
                return 500 + random.nextInt(1500);
            case "ESCOLTADO":
                return 100 + random.nextInt(900);
            case "FRÁGIL":
                return 100 + random.nextInt(500);
            case "PERECEDERO":
                return 500 + random.nextInt(2000);
            case "ALTO_VALOR":
                return 100 + random.nextInt(400);
            case "SERES_VIVOS":
                return 100 + random.nextInt(900);
            default:
                return 1000 + random.nextInt(2000);
        }
    }

    private int calcularCosteMinimo(String tipoPaquete, String origen, String destino) {
        int costeMinimo = Integer.MAX_VALUE;
        for (IVehiculo v : flota) {
            if (v.estaDisponible() && v.puedeTransportarTipo(tipoPaquete)) {
                int distancia = 100; // Placeholder for distance calculation
                int costeViaje = distancia * v.getCostePorKm();
                costeMinimo = Math.min(costeMinimo, costeViaje);
            }
        }
        return costeMinimo == Integer.MAX_VALUE ? 1000 : costeMinimo;
    }

    private int calcularPagoBase(int costeMinimo, String prioridad) {
        int pagoBase = (int) (costeMinimo * 1.2 * 10);
        pagoBase += (int) (pagoBase * random.nextDouble() * 0.5);

        if (prioridad.equals("URGENTE")) {
            pagoBase *= 1.5;
        } else if (prioridad.equals("BAJA")) {
            pagoBase *= 0.8;
        }

        return pagoBase;
    }

    private Calendar calcularFechaEntrega(String tipoPaquete, String prioridad) {
        Calendar fechaEntrega = (Calendar) fechaActual.clone();
        int diasBase;
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
            case "PERECEDERO":
                diasBase = 1;
                break;
            case "ALTO_VALOR":
                diasBase = 3;
                break;
            case "SERES_VIVOS":
                diasBase = 2;
                break;
            default:
                diasBase = 3;
        }
        if (prioridad.equals("URGENTE")) {
            diasBase = Math.max(1, diasBase - 2);
        } else if (prioridad.equals("BAJA")) {
            diasBase += 2;
        }
        fechaEntrega.add(Calendar.DAY_OF_MONTH, diasBase);
        return fechaEntrega;
    }

    public void setPedidoStrategy(PedidoStrategy pedidoStrategy) {
        this.pedidoStrategy = pedidoStrategy;
    }

    public PedidoStrategy getPedidoStrategy() {
        return pedidoStrategy;
    }
}