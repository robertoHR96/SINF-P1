CREATE TABLE IF NOT EXISTS Destinos
(
    destino_id  UUID PRIMARY KEY,
    nombre      text,
    pais        text,
    descripcion text,
    clima       text
);

CREATE TABLE IF NOT EXISTS Paquetes
(
    paquete_id UUID PRIMARY KEY,
    nombre     text,
    destino_id UUID,
    duracion   int,
    precio     decimal,
);

CREATE TABLE IF NOT EXISTS Clientes
(
    cliente_id         UUID PRIMARY KEY,
    nombre             text,
    correo_electronico text,
    telefono           text
);

CREATE TABLE IF NOT EXISTS Reservas
(
    reserva_id   UUID PRIMARY KEY,
    paquete_id   UUID,
    cliente_id   UUID,
    fecha_inicio date,
    fecha_fin    date,
    pagado       boolean,
);
