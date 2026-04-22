# Sistema de préstamos con MySQL

Segunda versión de un sistema de gestión de préstamos desarrollada en Java, incorporando persistencia relacional con MySQL, DAOs y una organización por capas más cercana a una aplicación real.

## Descripción

Este proyecto representa la segunda etapa en la evolución de un sistema de préstamos.

En la primera versión, el foco estuvo en el modelado del dominio y la lógica de negocio utilizando estructuras en memoria.  
En esta segunda versión, la fuente de verdad pasa a ser la base de datos, lo que obliga a reorganizar el sistema para trabajar con persistencia relacional, DAOs y consultas SQL.

La aplicación gestiona usuarios, recursos, préstamos y sanciones, aplicando reglas de negocio sobre préstamos activos, vencimientos, devoluciones y bloqueos.

## Funcionalidades

- Registrar usuarios
- Registrar recursos
- Registrar préstamos
- Consultar usuarios y recursos
- Listar préstamos
- Buscar préstamos por usuario, recurso o ID
- Registrar devoluciones
- Detectar préstamos vencidos
- Aplicar sanciones por atraso
- Consultar sanciones y usuarios bloqueados
- Persistir información en MySQL

## Estructura del proyecto

- `DOMINIO`: entidades y tipos del sistema
- `DATOS`: DAOs y acceso a datos
- `CONEXION`: conexión a base de datos
- `SISTEMA`: lógica general del sistema
- `Main.java`: punto de entrada
- `mysql.sql`: script de base de datos
- `pom.xml`: configuración Maven

## Conceptos aplicados

- Programación orientada a objetos
- Persistencia relacional con MySQL
- JDBC
- PreparedStatement y ResultSet
- Organización por capas
- Reconstrucción de objetos desde base de datos
- Manejo de fechas con `LocalDateTime` y `Timestamp`
- Inyección de dependencias por constructor
- Separación entre lógica de negocio y acceso a datos

## Cambios respecto a la versión inicial

En esta versión se introducen cambios importantes respecto al sistema sin MySQL:

- La base de datos pasa a ser la fuente de verdad
- Las estructuras en memoria dejan de ser el mecanismo principal de almacenamiento
- El acceso a los datos se concentra en clases DAO
- La lógica del sistema se reorganiza para trabajar con persistencia
- Se utiliza Maven para estructurar mejor el proyecto y manejar dependencias
- Se mejora la organización del código mediante carpetas separadas por responsabilidad

## Tecnologías utilizadas

- Java
- MySQL
- JDBC
- Maven

## Base de datos

El sistema utiliza MySQL para persistir usuarios, recursos, préstamos y sanciones.

Las consultas y actualizaciones se realizan mediante `PreparedStatement`, y los datos obtenidos se convierten nuevamente en objetos del dominio para que el sistema pueda seguir operando con lógica orientada a objetos.

El archivo `mysql.sql` contiene la estructura inicial de la base de datos necesaria para ejecutar el proyecto.

## Objetivo del proyecto

Esta versión fue desarrollada para evolucionar un sistema inicialmente basado en memoria hacia un diseño con persistencia real, aplicando DAOs, conexión a base de datos y una estructura más escalable.

## Evolución del proyecto

Este repositorio forma parte de una evolución en tres etapas:

1. **Sin MySQL:** modelado del dominio y lógica de negocio
2. **Con MySQL:** persistencia relacional mediante JDBC
3. **Con Spring:** reorganización del sistema usando framework

## Posibles mejoras

- Externalizar configuración de conexión
- Migrar a Spring con una arquitectura más robusta

## Estado

Proyecto funcional de práctica, conservado como segunda versión evolutiva del sistema.
