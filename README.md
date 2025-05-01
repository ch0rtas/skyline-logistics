# 🚚 Skyline Logistics

## Sistema de Gestión Logística Inteligente

Skyline Logistics es una simulación de una plataforma integral de gestión logística, operada completamente por línea de comandos (CLI). El sistema permite gestionar envíos de mercancías entre almacenes, transportistas y clientes finales, enfrentando desafíos reales como retrasos, averías y huelgas.

## 📋 Requisitos

- Java 11 o superior
- Terminal con soporte para caracteres UTF-8
- 4GB RAM mínimo para simulaciones complejas

## 🚀 Compilación y Ejecución

```bash
# Compilar el proyecto
javac -d bin src/**/*.java

# Ejecutar la aplicación
java -cp bin App --difficulty medium
```

## 🎮 Uso de Menús

### Menú Principal
1. Iniciar simulación
2. Configurar región
3. Ver ayuda
4. Salir

### Menú de Turno
1. Crear nuevo envío
2. Decorar envío existente
3. Resolver incidente
4. Rastrear pedido
5. Generar informe
6. Finalizar turno

## 📁 Estructura del Proyecto

```
src/
├── controller/                    // Lógica de orquestación CLI
├── domain/                        // Lógica de negocio y entidades
├── strategy/                      // Patrón Strategy
├── decorator/                     // Patrón Decorator
├── state/                         // Patrón State
├── factory/                       // Patrón Abstract Factory
├── incidente/                     // Patrón Template Method
├── util/                          // Utilidades y Singleton
└── App.java                       // Clase principal
```

## 🔄 Flujo de Turnos

1. Al iniciar un turno, se muestra la fecha y número de turno actual
2. El usuario puede realizar múltiples acciones a través del menú
3. Al finalizar el turno, se procesan eventos aleatorios
4. Se muestra un resumen de los eventos ocurridos
5. El sistema avanza al siguiente turno

## 📊 Métricas de Rendimiento

- Margen de beneficio por envío
- Tiempo medio de entrega
- Satisfacción de clientes
- Incidencias resueltas vs. pendientes

## 🛠️ Patrones de Diseño Implementados

1. Strategy - Planificación de rutas y modos de transporte
2. Decorator - Servicios adicionales para envíos
3. State - Gestión del ciclo de vida de pedidos
4. Abstract Factory - Creación de objetos regionales
5. Singleton - Gestión de cálculos financieros
6. Template Method - Resolución de incidentes
7. Facade - Interfaz simplificada del sistema 