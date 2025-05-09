# 🚚 Skyline Logistics

## Sistema de Gestión Logística Inteligente

---

## 📝 Descripción General

Skyline Logistics es una simulación de una plataforma integral, **completamente operada por línea de comandos (CLI)**, que gestiona envíos de mercancías entre almacenes, transportistas y clientes finales. El usuario asume el rol de gestor logístico, enfrentándose a desafíos reales (retrasos, averías, huelgas) para garantizar entregas puntuales. La meta es maximizar ingresos y minimizar quejas de clientes, evitando acumular tres reclamaciones graves.

---

## 🧩 Patrones de Diseño Implementados

### 1️⃣ Abstract Factory

**Objetivo:** Crear familias de vehículos y almacenes de manera consistente y extensible.

**Implementación:**
```java
public interface VehiculoFactory {
    IVehiculo crearVehiculoBase(String id, String[] tiposPermitidos);
    IVehiculo crearVehiculoMejorado(String id, String[] tiposPermitidos);
    IVehiculo crearVehiculoResistente(String id, String[] tiposPermitidos);
    IVehiculo crearVehiculoEficiente(String id, String[] tiposPermitidos);
}
```

**Valor Añadido:** Permite crear diferentes tipos de vehículos (Furgoneta, Camión, Barco, Avión) con sus características específicas.

### 2️⃣ Decorator

**Objetivo:** Añadir capacidades a los vehículos de forma dinámica y flexible.

**Implementación:**
```java
public abstract class VehiculoDecorator implements IVehiculo {
    protected IVehiculo vehiculo;
    
    public VehiculoDecorator(IVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    // Implementación de métodos delegados al vehículo base
}
```

**Valor Añadido:** Permite mejorar vehículos con características adicionales sin modificar la clase base.

### 3️⃣ Strategy

**Objetivo:** Implementar diferentes estrategias de procesamiento de pedidos según la dificultad.

**Implementación:**
```java
public interface ProcesamientoPedidoStrategy {
    void procesarPedido(Pedido pedido, List<IVehiculo> flota, 
                       Calendar fechaActual, String almacenPrincipal, 
                       Jugador jugador, int[] estadisticas);
}
```

**Valor Añadido:** Facilita la implementación de diferentes niveles de dificultad en el juego.

### 4️⃣ State

**Objetivo:** Gestionar los estados de los pedidos y vehículos.

**Valor Añadido:** Manejo claro y extensible de los diferentes estados del sistema.

### 5️⃣ Singleton

**Objetivo:** Garantizar una única instancia de recursos globales.

**Valor Añadido:** Control centralizado de recursos compartidos.

### 6️⃣ Template Method

**Objetivo:** Estandarizar procesos comunes.

**Valor Añadido:** Reutilización de código y consistencia en operaciones similares.

### 7️⃣ Facade

**Objetivo:** Simplificar la interfaz del sistema.

**Valor Añadido:** Facilita el uso del sistema para los clientes.

---

## 📂 Estructura de Paquetes

```
src/
├── factory/                       // Patrón Abstract Factory
│   ├── AbstractVehiculoFactory.java
│   ├── VehiculoFactory.java
│   ├── VehiculoFactoryProvider.java
│   ├── FurgonetaFactory.java
│   ├── CamionFactory.java
│   ├── BarcoFactory.java
│   └── AvionFactory.java
├── game/                         // Lógica principal del juego
│   ├── PedidoGenerator.java
│   ├── Jugador.java
│   ├── Pedido.java
│   ├── Almacen.java
│   ├── Ruta.java
│   └── Evento.java
├── model/                        // Modelos de datos
│   ├── TipoCarga.java
│   ├── Ubicacion.java
│   └── Estadisticas.java
├── template/                     // Patrón Template Method
│   ├── ProcesadorPedido.java
│   ├── ProcesadorPedidoBase.java
│   └── ProcesadorPedidoUrgente.java
├── singleton/                    // Patrón Singleton
│   ├── GestorRecursos.java
│   └── ConfiguracionGlobal.java
├── util/                         // Utilidades
│   ├── CalculadoraCostos.java
│   ├── ValidadorPedidos.java
│   └── Logger.java
├── service/                      // Servicios
│   ├── PedidoService.java
│   ├── VehiculoService.java
│   └── AlmacenService.java
├── facade/                       // Patrón Facade
│   ├── GestorLogistica.java
│   └── InterfazUsuario.java
├── strategy/                     // Patrón Strategy
│   ├── ProcesamientoPedidoStrategy.java
│   ├── PedidoFacilStrategy.java
│   └── PedidoDificilStrategy.java
├── state/                        // Patrón State
│   ├── EstadoPedido.java
│   ├── EnProceso.java
│   ├── EnTransito.java
│   ├── Retrasado.java
│   └── Entregado.java
├── decorator/                    // Patrón Decorator
│   ├── IVehiculo.java
│   ├── VehiculoDecorator.java
│   ├── VehiculoMejorado.java
│   ├── VehiculoResistente.java
│   └── VehiculoEficiente.java
├── ui/                           // Interfaz de usuario
│   ├── MenuPrincipal.java
│   ├── MenuTurno.java
│   └── VisualizadorEstadisticas.java
└── Main.java                     // Punto de entrada
```

---

## 🔄 Dinámica del Sistema

### 1. **Inicialización** 🚀
   - Creación de la flota inicial
   - Configuración de parámetros del juego
   - Inicialización del generador de pedidos

### 2. **Ciclo de Operación** 📆
   - Generación de pedidos según la dificultad
   - Asignación de vehículos
   - Procesamiento de eventos
   - Actualización de estadísticas

### 3. **Comandos Disponibles** ⌨️
   - `enviar [origen] [destino] [prioridad]` - Crear nuevo envío
   - `decorar [id_envio] [servicio]` - Añadir servicio adicional
   - `resolver [id_incidente] [metodo]` - Gestionar incidencia
   - `rastrear [id_pedido]` - Consultar estado actual
   - `informe [diario|semanal]` - Generar reporte de operaciones

### 4. **Métricas de Rendimiento** 📊
   - Margen de beneficio por envío
   - Tiempo medio de entrega
   - Satisfacción de clientes
   - Incidencias resueltas vs. pendientes

---

## 📱 Ejemplo de Caso de Uso

**Escenario**: Envío urgente de vacunas durante una crisis meteorológica en Sudamérica.

**Línea de comandos**:
```bash
# Iniciar sistema y seleccionar región
> iniciar --region sudamerica

# Crear envío básico
> enviar --origen "Lima" --destino "Cusco" --carga "Vacunas" --peso 500

# Decorar con servicios adicionales
> decorar 1001 --servicio refrigeracion
> decorar 1001 --servicio seguro
> decorar 1001 --servicio urgente

# Se notifica incidente (generado automáticamente)
! ALERTA: Incidente #127 - Bloqueo de carreteras por deslizamiento

# Resolver incidente aplicando estrategia alternativa
> resolver 127 --aplicar-estrategia aereo
> estado 1001

# Resultado: Pedido en tránsito con nueva ruta aérea
```

---

## 💡 Ventajas Clave

- **Modularidad**: Cada patrón encapsula una parte específica del sistema
- **Reusabilidad**: Componentes como estrategias y decoradores son altamente reutilizables
- **Flexibilidad**: Fácil adaptación a nuevos requisitos
- **Escalabilidad**: Estructura preparada para crecer
- **Mantenibilidad**: Separación clara de responsabilidades

---

## 💻 Requisitos del Sistema

- Java 11 o superior
- Terminal con soporte para caracteres UTF-8
- 4GB RAM mínimo para simulaciones complejas

```bash
# Compilar
javac -d bin src/**/*.java

# Ejecutar
java -cp bin Main --difficulty medium
```
