# 🚚 Skyline Logistics

## Sistema de Gestión Logística Inteligente

Skyline Logistics es un juego de simulación logística que pone al jugador al frente de una empresa de transporte y distribución. El objetivo principal es gestionar eficientemente una flota de vehículos para realizar entregas y expandir el negocio, todo mientras se mantiene un balance entre la rentabilidad y la satisfacción del cliente.

## 📋 Características Principales

- Gestión de flota de vehículos (furgonetas, camiones, tráileres)
- Sistema de envíos con diferentes tipos de carga
- Múltiples modos de juego (Carrera, Libre, Desafío)
- Sistema económico detallado
- Gestión de infraestructura y almacenes
- Sistema de progresión y reputación
- Eventos aleatorios y crisis
- Diferentes niveles de dificultad

## 🎮 Modos de Juego

### Modo Carrera
- Progresión gradual de dificultad
- Sistema de reputación y clientes
- Desbloqueo de nuevas rutas y vehículos
- Eventos especiales
- Logros y recompensas

### Modo Libre
- Acceso a todos los vehículos
- Sin restricciones de tiempo
- Enfoque en la experimentación y optimización
- Personalización completa
- Modo sandbox
- Herramientas de prueba

### Modo Desafío
- Escenarios con condiciones específicas
- Objetivos a tiempo limitado
- Restricciones de recursos
- Desafíos diarios
- Competencias globales
- Rankings y premios

## 🚀 Requisitos del Sistema

- Java 11 o superior
- Terminal con soporte para caracteres UTF-8
- 4GB RAM mínimo para simulaciones complejas

## 📦 Compilación y Ejecución

```bash
# Compilar el proyecto
javac -d bin src/**/*.java

# Ejecutar la aplicación
java -cp bin Main --difficulty medium
```

## 🎯 Objetivos del Juego

El jugador debe convertirse en el líder del mercado logístico, gestionando una red de distribución que conecta diferentes ciudades y puntos de entrega. Para lograrlo, deberá:
- Realizar entregas a tiempo y en perfectas condiciones
- Gestionar eficientemente los recursos económicos
- Mantener y mejorar la flota de vehículos
- Expandir el negocio a nuevas rutas y territorios
- Mantener una buena reputación con los clientes
- Adaptarse a las condiciones del mercado y la competencia
- Gestionar crisis y eventos inesperados
- Optimizar rutas y recursos para maximizar beneficios

## 🛠️ Patrones de Diseño Implementados

### 1. Abstract Factory
- Creación de vehículos y almacenes
- `VehiculoFactory` y `AbstractVehiculoFactory`
- Fábricas concretas para cada tipo de vehículo

### 2. Decorator
- Mejoras de vehículos
- `IVehiculo` y `VehiculoDecorator`
- Decoradores para diferentes mejoras

### 3. Strategy
- Procesamiento de pedidos
- `ProcesamientoPedidoStrategy`
- Estrategias para diferentes niveles de dificultad

### 4. State
- Estados de pedidos y vehículos
- Manejo de diferentes estados del juego

### 5. Singleton
- Gestión de recursos globales
- Configuración centralizada

### 6. Template Method
- Procesos estandarizados
- Estructura base para diferentes tipos de pedidos

### 7. Facade
- Interfaz simplificada del sistema
- Gestión de la complejidad del sistema

## 📁 Estructura del Proyecto

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

## 📊 Sistema de Envíos

### Vehículos Disponibles

#### Furgonetas
- Furgonetas pequeñas (hasta 1.5 toneladas)
- Furgonetas medianas (hasta 3.5 toneladas)

#### Camiones
- Camiones rígidos (hasta 12 toneladas)
- Camiones articulados (hasta 26 toneladas)

#### Tráileres
- Tráileres estándar
- Tráileres especializados (refrigerados, cisternas, etc.)

## 📈 Sistema de Progresión

### Reputación
- Calificación por cliente
- Calificación global
- Beneficios de alta reputación

### Logros
- Logros por categoría
- Logros especiales
- Recompensas
- Desbloqueos
- Títulos y reconocimientos

## 👥 Autores

- Luis Marquina - [GitHub](https://github.com/Luiiss44/skyline-logistics)
- Manuel Martínez - [GitHub](https://github.com/ch0rtas/skyline-logistics)
- Miguel Toran

Estudiantes de la Universidad U-Tad, Grado en Ingeniería de Software  
Asignatura: Diseño de Software 