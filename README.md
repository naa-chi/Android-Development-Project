# Junta Vecinos

- Aplicación de Android para gestión de comunidades de vecinos.

----

## Descripción
- Junta Vecinos es una aplicación de Android que facilita la organización y comunicación dentro de comunidades de vecinos, permitiendo gestionar eventos, gastos y espacios comunes de manera colaborativa.

## Tecnologías
> Android (Kotlin/Java) - Aplicación móvil nativa
> Python - Ayuda a levantar el servidor
> Base de datos (Room + SQLite) - Persistencia de datos local y remota

## Estructura
```
├── Test/          # Aplicación Android (cliente móvil)  
│   ├── app/       # Código fuente principal
│   └── gradle/    # Configuración de build
├── Server/        # Backend en Python
│   ├── event/     # Gestión de eventos 
│   ├── expenses/  # Control de gastos
│   ├── neighborhood/ # Datos del vecindario
│   ├── users/     # Gestión de usuario
│   ├── venue/     # Espacios comunes
│   └── zGateway/  # API Gateway
└── External/      # Recursos externos
```

## Caracteristicas Principales
- Gestión de Eventos: Creación, edición y seguimiento de eventos comunitarios
- Control de Gastos: Registro y seguimiento de gastos del vecindario
- Gestión de Usuarios: Sistema de cuentas locales con persistencia
- Espacios Comunes: Administración de vecindades y áreas compartidas
- API REST: Comunicación cliente-servidor mediante microservicios

## Instalación
Requisitos Previos
- Android Studio (para la aplicación móvil)
- Python 3.x (para el servidor)
- JDK 21+

### Aplicación Android
1. Clonar el repositorio
2. Abrir la carpeta Test/ en Android Studio
3. Sincronizar dependencias Gradle
4. Ejecutar en emulador o dispositivo físico

## Servidor Backend
1. Navegar a la carpeta /Server/
2.Instalar dependencias de Python
3. Configurar variables de entorno en .env
4. Ejecutar python run_all.py para iniciar todos los microservicios


# JUNTA DE VECINOS 2026 TODOS LOS DERECHOS RESERVADOS
sup><sub>(No realmente)</sub></sup>
