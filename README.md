Sistema de Reserva de Clases de Gimnasio
Proyecto final del curso Programación de Lenguajes de Cuarta Generación - Universidad Central.
Avance I: login, gestión de clientes y gestión de clases.
Tecnologías usadas
Java
Spring Boot
Spring Security
Spring Data JPA
Base de datos H2
Thymeleaf
Bootstrap
Maven
Cómo ejecutar el proyecto
Tener instalado Java 21 o superior.
Abrir la carpeta del proyecto en Visual Studio Code.
En la terminal escribir:
```
mvnw.cmd spring-boot:run
```
Cuando aparezca "Started GimnasioApplication", abrir en el navegador:
```
http://localhost:8080
```
Usuarios de prueba
Entrenador (administrador)
Correo: admin@gimnasio.com
Contraseña: admin123
Entrenador
Correo: entrenador2@gimnasio.com
Contraseña: entrenador123
Cliente
Correo: cliente@gimnasio.com
Contraseña: cliente123
Qué hace el sistema
Iniciar sesión, registrarse y recuperar la contraseña.
El cliente puede editar su perfil.
El administrador puede ver, editar y desactivar clientes.
El administrador puede crear, editar y cancelar clases.
Las clases duran 1 hora y un entrenador no puede tener dos clases a la misma hora.
El cliente puede ver las clases disponibles.
Integrantes
Andrés Vargas León
Jose Gabriel Castro Berrocal
Roberth Cordero Alvarez
