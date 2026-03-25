
CREATE DATABASE SISTEMAPRESTAMO;
USE SISTEMAPRESTAMO;

CREATE TABLE Usuarios (
  U_Legajo VARCHAR(50) NOT NULL PRIMARY KEY,
  U_Nombre VARCHAR(200) NOT NULL,
  U_Correo VARCHAR(200) NOT NULL UNIQUE,
  U_TipoUsuario ENUM('ALUMNO','DOCENTE') NOT NULL
);

CREATE TABLE Recursos (
  R_Codigo VARCHAR(50) NOT NULL PRIMARY KEY,
  R_Nombre VARCHAR(200) NOT NULL,
  R_Tipo ENUM('NOTEBOOK','LIBRO','PROYECTOR','KITLABORATORIO') NOT NULL,
  R_Estado ENUM('DISPONIBLE','PRESTADO','ENMANTENIMIENTO','DADOBAJA') NOT NULL,
  R_FechaAlta DATETIME NOT NULL
);

CREATE TABLE Prestamos (
  P_Id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  P_FechaInicio DATETIME NOT NULL,
  P_FechaVencimiento DATETIME NOT NULL,
  P_FechaDevolucion DATETIME NULL,
  P_Estado ENUM('ACTIVO','DEVUELTO','VENCIDO','CANCELADO') NOT NULL,

  U_LegajoHace VARCHAR(50) NOT NULL,
  R_Codigo VARCHAR(50) NOT NULL,

    FOREIGN KEY (U_LegajoHace) REFERENCES Usuarios(U_Legajo),
    FOREIGN KEY (R_Codigo) REFERENCES Recursos(R_Codigo)
  
);

CREATE INDEX idx_prestamo_usuario ON Prestamos (U_LegajoHace);
CREATE INDEX idx_prestamo_recurso ON Prestamos (R_Codigo);
CREATE INDEX idx_prestamo_estado  ON Prestamos (P_Estado);

CREATE TABLE Sanciones (
  S_Id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  S_DiaAtraso INT NOT NULL,
  S_FechaInicioBloqueo DATETIME NOT NULL,
  S_FechaFinBloqueo DATETIME NOT NULL,
  S_Motivo VARCHAR(300) NOT NULL,
  P_Id BIGINT NOT NULL UNIQUE,
  U_Legajo VARCHAR(50) NOT NULL,
  
    FOREIGN KEY (U_Legajo) REFERENCES Usuarios(U_Legajo)
);
CREATE INDEX idx_sancion_usuario ON Sanciones (U_Legajo);
select *
from sanciones;
alter table sanciones
drop S_Motivo;
select *
from recursos
where r_codigo="12"