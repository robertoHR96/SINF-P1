create keyspace if not exists cassandraP1 WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};
use cassandraP1;

drop table if exists destinos;
drop table if exists paquetes;
drop table if exists clientes;
drop table if exists reservas;


create table destinos
(
    destino_id  UUID,
    nombre      text,
    pais        text,
    descripcion text,
    clima       text,
    PRIMARY KEY ( destino_id )
);
create index if not exists destino_id on destinos (clima);
create index if not exists pais_id on destinos (pais);

insert into destinos (destino_id, nombre, pais, descripcion, clima)
values (uuid(), 'Cáceres', 'España', 'Cáceres es una ciudad preciosa', 'Bueno');

insert into destinos (destino_id, nombre, pais, descripcion, clima)
values (uuid(), 'Cáceres', 'Francia', 'Cáceres es una ciudad preciosa', 'Malo');

select * from destinos where pais = 'Francia';
select * from destinos where destino_id = 03770ec7-877f-4b61-b188-3e294ac58af7 and clima = 'Malo';


///////////////

create table if not exists paquetes
(
    paquete_id uuid,
    nombre     text,
    destino_id uuid,
    duracion   int,
    precio     decimal,
    PRIMARY KEY ( paquete_id  )
);

create index if not exists paquetes_por_destinos on paquetes (destino_id);

create index if not exists paquetes_por_nombres on paquetes (nombre);
create index if not exists paquetes_por_nombres on paquetes (duracion);

// Malo
insert into paquetes (paquete_id, nombre, destino_id, duracion, precio)
values (uuid(), 'Destino 2', 03770ec7-877f-4b61-b188-3e294ac58af7, 20, 50 );

// buen clima
insert into paquetes (paquete_id, nombre, destino_id, duracion, precio)
values (uuid(), 'Destino 2', 019a6e6f-6160-4d0c-b493-a03e5abb8d4c, 10, 40 );


select * from paquetes;
select * from paquetes where nombre = 'Destino 2';
select * from paquetes where destino_id = 03770ec7-877f-4b61-b188-3e294ac58af7 and paquete_id = 9e3d3306-27c8-43fe-a41b-863500f24412;
select * from paquetes where nombre = 'Destino 2' and paquete_id = 9e3d3306-27c8-43fe-a41b-863500f24412;
select * from paquetes where destino_id = 03770ec7-877f-4b61-b188-3e294ac58af7 and duracion=20 allow filtering ;

create table if not exists clientes
(
    cliente_id         uuid,
    nombre             text,
    correo_electronico text,
    telefono           text,
    primary key ( cliente_id )
);

create index if not exists on clientes(correo_electronico);

insert into clientes (cliente_id, correo_electronico, nombre, telefono)
values (uuid(), 'roberto@gmail.com', 'roberto', '600000000');

insert into clientes (cliente_id, correo_electronico, nombre, telefono)
values (uuid(), 'juan@gmail.com', 'juan', '666666666');

select * from clientes;
select * from clientes where correo_electronico = 'roberto@gmail.com';

create table if not exists reservas
(
    reserva_id   uuid,
    paquete_id   uuid,
    cliente_id   uuid,
    fecha_inicio date,
    fecha_fin    date,
    pagado       boolean,
    primary key ( reserva_id )
);
create index if not exists reserva_por_pagado on reservas (pagado);
create index if not exists destino_por_usuario on reservas (cliente_id);
create index if not exists cliente_por_fehcas on reservas (fecha_inicio);
// create index if not exists destino_por_usuario on reservas (cliente_id, (fecha_inicio, fecha_fin));

// juan va a destino mal clima
insert into reservas (reserva_id, cliente_id, fecha_fin, fecha_inicio, pagado, paquete_id)
VALUES (uuid(), b6354ef6-0fd4-491d-8211-389af2cebb80, '2023-11-23', '2023-11-13', true,
        9e3d3306-27c8-43fe-a41b-863500f24412);
insert into reservas (reserva_id, cliente_id, fecha_fin, fecha_inicio, pagado, paquete_id)

// Alvaro y robero van a buel clima
VALUES (uuid(), 989456f8-e5d1-4248-a9d4-e10c5bd639c6, '2024-11-23', '2024-11-13', true,
        6c608588-5e32-451b-9588-6f33e9e35d49 );
select * from reservas where cliente_id=b6354ef6-0fd4-491d-8211-389af2cebb80 and pagado=true;
SELECT * FROM reservas WHERE fecha_inicio >= '2023-01-01' AND fecha_fin <= '2024-11-22' allow filtering ;

select * from reservas where cliente_id = b6354ef6-0fd4-491d-8211-389af2cebb80 and fecha_inicio > '2023-01-01' AND fecha_fin < '2024-11-22';




//select * from paquetes where paquete_id=45f5342f-e868-46b0-a816-20ea6c53a112;


