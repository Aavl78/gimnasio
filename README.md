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
https://github.com/Aavl78/gimnasio
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


AVANCE II - PAGOS Y RESERVAS

Usuarios de prueba
Administrador: admin@gimnasio.com / admin123
Entrenador 2: entrenador2@gimnasio.com / entrenador123
Cliente al dia: cliente@gimnasio.com / cliente123
Cliente moroso: moroso@gimnasio.com / cliente123
Cliente al dia 2: cliente2@gimnasio.com / cliente123

Datos de prueba
Yoga (finalizada): clase de ayer
Pilates (llena): cupo 1, ya reservada por Ana Rojas
Spinning: manana 18:00, reservada por Cliente Prueba
Funcional: manana 18:30, se cruza con Spinning
Zumba: en 2 dias, libre

Nota: los datos se crean desde la fecha del primer arranque.
Para reiniciarlos, apagar la app y borrar la carpeta data.

Reglas de reserva (todas en ReservaService)
R1 cliente al dia, R2 cupo, R3 clase vigente, R4 sin traslape,
R5 sin duplicados, R6 estados ACTIVA/CANCELADA, R7 cancelar hasta 15 min
despues del inicio, R8 admin cancela cualquiera, R9 cancelar clase cancela sus reservas.
