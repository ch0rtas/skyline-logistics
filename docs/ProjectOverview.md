# 🚚 Skyline Logistics

## Sistema de Gestión Logística Inteligente

---

## 📝 Descripción General

Skyline Logistics es una simulación de una plataforma integral, **completamente operada por línea de comandos (CLI)**, que gestiona envíos de mercancías entre almacenes, transportistas y clientes finales. El usuario asume el rol de gestor logístico, enfrentándose a desafíos reales (retrasos, averías, huelgas) para garantizar entregas puntuales. La meta es maximizar ingresos y minimizar quejas de clientes, evitando acumular tres reclamaciones graves.

---

## 🧩 Patrones de Diseño Implementados

A continuación se describen los siete patrones de diseño integrados en Skyline Logistics, cada uno con su objetivo, funcionamiento conceptual y valor añadido al sistema.

### 1️⃣ Strategy

**Objetivo:** Permitir la selección dinámica de diferentes métodos de planificación de rutas y modos de transporte según la urgencia, el coste y las condiciones externas.

**Explicación:** Se define una familia de algoritmos de envío (rápido, económico y alternativo en caso de bloqueo terrestre). El sistema decide en tiempo de ejecución cuál aplicar según la prioridad asignada al pedido y las restricciones actuales. Gracias a este patrón, agregar nuevos métodos de envío (por ejemplo, drones o ferrocarril) no requiere modificar la lógica central.

**Valor Añadido:** Flexible adaptabilidad a nuevas estrategias y simplificación de la lógica de planificación.

### 2️⃣ Decorator

**Objetivo:** Añadir servicios opcionales a los envíos (seguro, refrigeración, prioridad urgente) de modo transparente y combinable.

**Explicación:** Se parte de un envío base y, sin modificar su implementación original, se le «decoran» capas que ajustan el coste y el tiempo estimado de entrega. Cada servicio extra se encapsula en un componente independiente que se acopla al envío inicial, permitiendo combinaciones ilimitadas.

**Valor Añadido:** Composición dinámica de servicios, evitando proliferación de subclases y manteniendo un núcleo de envío limpio.

### 3️⃣ State

**Objetivo:** Gestionar el ciclo de vida de un pedido a través de estados (en proceso, en tránsito, retrasado, entregado) que determinan su comportamiento ante eventos.

**Explicación:** El pedido mantiene una referencia a su estado actual, delegando en él la manera de reaccionar a sucesos como averías, huelgas o confirmación de entrega. Cada estado encapsula las transiciones y las acciones permitidas, evitando condicionales dispersos.

**Valor Añadido:** Claridad en la gestión de estados y facilidad para incorporar nuevas fases o eventos en el flujo de pedidos.

### 4️⃣ Abstract Factory

**Objetivo:** Crear familias de objetos relacionados (vehículos y almacenes) específicas de cada región geográfica sin acoplar el código cliente a clases concretas.

**Explicación:** Se define una interfaz de fábrica genérica para producir los distintos tipos de vehículos y almacenes, y se implementan fábricas concretas para cada región con sus particularidades (normativa EURO-6, tracción 4x4, sistemas de seguridad, etc.).

**Valor Añadido:** Escalabilidad geográfica, permitiendo incorporar nuevos mercados sin alterar la estructura del sistema.

### 5️⃣ Singleton

**Objetivo:** Garantizar una única instancia del componente responsable de los cálculos financieros (`CalculadorCostos`), accesible globalmente.

**Explicación:** El patrón asegura que todas las partes de la aplicación usen la misma lógica y datos centralizados para computar costes y márgenes, evitando discrepancias y redundancias.

**Valor Añadido:** Consistencia en los resultados de cálculo y reducción de sobrecarga de instanciación.

### 6️⃣ Template Method

**Objetivo:** Estandarizar el proceso de resolución de incidentes logísticos (averías, huelgas) definiendo un esqueleto de pasos y permitiendo especializaciones en pasos concretos.

**Explicación:** El flujo general (identificar causa, asignar recursos, evaluar si está resuelto, aplicar contingencia, notificar) está fijado, pero cada tipo de incidente provee su propia lógica para los pasos específicos.

**Valor Añadido:** Coherencia en el tratamiento de incidentes y fácil incorporación de nuevos tipos con procesos personalizados.

### 7️⃣ Facade

**Objetivo:** Ofrecer una interfaz simplificada (`GestorCentralLogistica`) que unifica y oculta la complejidad de múltiples subsistemas.

**Explicación:** Mediante métodos de alto nivel, el cliente interactúa con las operaciones principales sin preocuparse por la inicialización de estrategias, decoración de envíos o cambio de estados. La fachada coordina internamente las llamadas a los demás componentes.

**Valor Añadido:** Reducción de dependencias directas y mejora de la experiencia de uso de la consola.

---

## 📂 Estructura de Paquetes

```plaintext
src/
├── controller/                    // Lógica de orquestación CLI
│   ├── facade/                    // Fachada principal del sistema
│   │   └── GestorCentralLogistica.java
│   └── commands/                  // Manejadores de comandos
│       ├── PlanificarEnvioCommand.java
│       ├── ResolverIncidenteCommand.java
│       ├── ConsultarInformeCommand.java
│       └── HelpCommand.java
│
├── domain/                        // Lógica de negocio y entidades
│   ├── model/                     // Entidades y objetos de valor
│   │   ├── pedido/                // Estado y comportamiento del pedido
│   │   │   ├── Pedido.java
│   │   │   ├── EstadoPedido.java
│   │   │   ├── estados/           // Implementaciones del patrón State
│   │   │   └── events/            // Definición de eventos
│   │   ├── envio/                 // Estructura de envíos y servicios
│   │   ├── flota/                 // Vehículos y características
│   │   ├── almacen/               // Almacenes y configuración
│   │   └── cliente/               // Información de clientes
│   ├── service/                   // Casos de uso y lógica intermedia
│   │   ├── EnvioService.java
│   │   ├── IncidenteService.java
│   │   └── ReporteService.java
│   └── repository/                // Persistencia simulada
│       ├── PedidoRepository.java
│       └── ConfiguracionRepository.java
│
├── strategy/                      // Pattern Strategy
│   ├── context/                   // Gestión de estrategia activa
│   │   └── EnvioContext.java
│   └── implementations/           // Algoritmos concretos
│       ├── rapido/                // Transporte aéreo prioritario
│       ├── economico/             // Transporte marítimo económico
│       └── bloqueo/               // Alternativas por bloqueo
│
├── decorator/                     // Pattern Decorator
│   ├── base/                      // Componente base
│   │   └── Envio.java
│   └── decorators/                // Servicios complementarios
│       ├── seguro/                // Seguro contra daños
│       ├── refrigeracion/         // Cadena de frío
│       └── urgente/               // Prioridad y despacho rápido
│
├── state/                         // Pattern State
│   ├── core/                      // Interfaz del estado
│   │   └── EstadoPedido.java
│   └── states/                    // Implementaciones
│       ├── EnProceso.java
│       ├── EnTransito.java
│       ├── Retrasado.java
│       └── Entregado.java
│
├── factory/                       // Pattern Abstract Factory
│   ├── abstract/                  // Interfaces genéricas
│   │   └── LogisticaFactory.java
│   └── concrete/                  // Fábricas por región
│       ├── europa/                // EuropaFactory + productos EURO6
│       └── sudamerica/            // SudamericaFactory + vehículos 4x4
│
├── incidente/                     // Pattern Template Method
│   ├── core/                      // Definición del método plantilla
│   │   └── IncidenteLogistico.java
│   └── tipos/                     // Clases de incidentes
│       ├── AveriaCamion.java
│       └── HuelgaTransporte.java
│
├── util/                          // Pattern Singleton y utilidades
│   ├── calculators/               // Cálculos de costes
│   │   └── CalculadorCostos.java
│   └── logging/                   // Registro de eventos
│       └── LogManager.java
│
├── config/                        // Configuración
│   └── application.properties     // Parámetros de simulación
│
└── App.java                       // Clase principal de arranque
```

---

## 🔄 Dinámica del Sistema

### 1. **Inicialización** 🚀
   - Selección de región (fábrica)
   - Creación de flota y almacenes
   - Configuración de parámetros iniciales (presupuesto, dificultad)

### 2. **Ciclo de Operación** 📆
   - Cada día laboral es un "turno" donde ocurren eventos aleatorios
   - Gestión de pedidos entrantes con diferentes prioridades
   - Asignación de recursos (vehículos, personal) a envíos
   - Resolución de incidentes que surgen durante las operaciones

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

- **Modularidad**: Cada patrón encapsula una parte específica del sistema, facilitando mantenimiento y extensión
- **Reusabilidad**: Componentes como estrategias y decoradores son altamente reutilizables
- **Flexibilidad**: Fácil adaptación a nuevos requisitos o escenarios logísticos
- **Escalabilidad**: Estructura preparada para crecer en complejidad sin refactorizaciones mayores
- **Mantenibilidad**: Separación clara de responsabilidades y bajo acoplamiento

---

## 💻 Requisitos del Sistema

- Java 11 o superior
- Terminal con soporte para caracteres UTF-8
- 4GB RAM mínimo para simulaciones complejas

```bash
# Compilar
javac -d bin src/**/*.java

# Ejecutar
java -cp bin App --difficulty medium
```
