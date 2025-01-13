# Foro Web API

Foro Web API es una aplicación desarrollada en **Spring Boot** que permite la gestión de usuarios, publicaciones, comentarios y análisis de tendencias utilizando inteligencia artificial. Este proyecto incluye un sistema de autenticación, manejo de archivos y conexión con servicios externos.

## Tecnologías Utilizadas

- **Spring Boot 3.3.1**
- **Java 17**
- **JPA/Hibernate** para manejo de entidades y base de datos
- **RESTful API** para comunicación cliente-servidor
- **JWT** para autenticación y manejo de sesiones
- **WebSockets** para notificaciones en tiempo real
- **Testcontainers** para pruebas
- **Python** para análisis externo a través de servicios REST

## Funcionalidades

1. Gestión de usuarios (alumnos y profesores) con autenticación basada en JWT.
2. Creación y gestión de publicaciones y comentarios.
3. Subida y manejo de archivos (imágenes y documentos).
4. Notificaciones en tiempo real utilizando WebSockets.
5. Análisis de publicaciones y tendencias mediante integración con servicios de IA.
6. Gestión de departamentos, materias y relaciones entre ellos.

## Estructura del Proyecto

El proyecto sigue una arquitectura limpia, dividida en capas:

- **Controller**: Gestión de peticiones HTTP.
- **Service**: Lógica de negocio y procesamiento.
- **Model**: Representación de datos y relaciones.
- **Repository**: Acceso a la base de datos.

## Requisitos Previos

1. **Java 17** o superior.
2. **Maven 3.8.1** o superior.
3. **Base de datos MySQL**.
4. **Python 3.x** con los paquetes requeridos para análisis de IA.

## Configuración del Proyecto

1. Clona este repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd foroweb
   ```
## Configura el archivo application.properties en src/main/resources
   ```bash  
spring.datasource.url=jdbc:mysql://localhost:3306/foroweb
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
jwt.secret=TU_LLAVE_SECRETA
```
## Configura el entorno para ejecutar análisis de IA en Python
1. Ruta del script: /entrenamineto.py
2. Virtual environment

# Foro Web API
## Autenticación

    POST /api/usuarios/login: Inicio de sesión.
    GET /api/usuarios/token: Información del usuario autenticado.

## Gestión de Usuarios

    POST /api/usuarios/alumno: Crear alumno.
    POST /api/usuarios/profesor: Crear profesor.

## Publicaciones y Comentarios

    POST /api/post/post: Crear una publicación.
    GET /api/post/{id}: Obtener detalles de una publicación.
    POST /api/comentario/comentarios: Agregar un comentario.

## Análisis y Tendencias

    GET /api/analisis/analyze-posts: Análisis de publicaciones.
    GET /api/analisis/tendencias: Análisis de tendencias.

## WebSockets

    Notificaciones en tiempo real para usuarios.
    
# Backend 
https://github.com/edgar1223/foroWeb2.0.git


