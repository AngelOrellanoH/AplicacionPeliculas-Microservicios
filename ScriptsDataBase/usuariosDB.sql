-- Crear base de datos
CREATE DATABASE IF NOT EXISTS usuarios_db;
USE usuarios_db;

-- Tabla de usuarios
CREATE TABLE Users (
    username VARCHAR(100) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Tabla de roles/autorizaciones
CREATE TABLE Authorities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    authority VARCHAR(50),
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE
);

-- Tabla de críticas
CREATE TABLE Criticas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pelicula INT NOT NULL, -- ID de película (referencia cruzada externa)
    username VARCHAR(100) NOT NULL,
    valoracion TEXT,
    nota INT,
    fecha DATE,
    FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE,
    CHECK (nota BETWEEN 1 AND 10)
);
