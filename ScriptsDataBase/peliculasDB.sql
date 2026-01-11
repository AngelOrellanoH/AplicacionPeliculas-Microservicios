-- Crear base de datos
CREATE DATABASE IF NOT EXISTS peliculas_db;
USE peliculas_db;

-- Tabla de películas
CREATE TABLE Peliculas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    anio INT,
    duracion INT, -- en minutos
    pais VARCHAR(100),
    direccion VARCHAR(255),
    genero VARCHAR(100),
    sinopsis TEXT,
    imagen_portada VARCHAR(500) -- ruta o URL
);

-- Tabla de actores
CREATE TABLE Actores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE,
    pais_nacimiento VARCHAR(100)
);

-- Tabla intermedia Pelicula_Actor (relación N:M)
CREATE TABLE Pelicula_Actor (
    id_pelicula INT,
    id_actor INT,
    PRIMARY KEY (id_pelicula, id_actor),
    FOREIGN KEY (id_pelicula) REFERENCES Peliculas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_actor) REFERENCES Actores(id) ON DELETE CASCADE
);
