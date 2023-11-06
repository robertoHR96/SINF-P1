create keyspace if not exists cassandraP1 WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};
use cassandraP1;
create table if not exists destinos (
                                        destino_id UUID,
                                        nombre text,
                                        pais text,
                                        descripcion text,
                                        clima text,
                                        PRIMARY KEY ( destino_id )
);

insert into destinos ( destino_id, nombre, pais, descripcion, clima)
values (uuid(),'Cáceres','España','Cáceres es una ciudad preciosa','Bueno');

select * from destinos;

create table if not exists paquetes(
                                       paquete_id uuid,
                                       nombre text,
                                       destino_id uuid,
                                       duracion int,
                                       precio decimal,
                                       PRIMARY KEY ( paquete_id )
);
//insert into paquetes (paquete_id, nombre, destino_id, duracion, precio) values (uuid(), 'Destino 2', 6ea6130e-c0a7-4d85-bd86-79df2e05dae4, 10, 999.89 );
select * from paquetes;

//select * from paquetes where paquete_id=45f5342f-e868-46b0-a816-20ea6c53a112;

select * from destinos;


create table if not exists clientes (
                                        cliente_id uuid,
                                        nombre text,
                                        correo_electronico text,
                                        telefono text,
                                        primary key ( cliente_id )
);

insert into clientes (cliente_id, correo_electronico, nombre, telefono)
values (uuid(), 'alvaro@gmail.com','alvaro','234234534');

select * from clientes;

create table if not exists reservas (
                                        reserva_id uuid,
                                        paquete_id uuid,
                                        cliente_id uuid,
                                        fecha_inicio date,
                                        fecha_fin date,
                                        pagado boolean,
                                        primary key ( reserva_id )
);

insert into reservas (reserva_id, cliente_id, fecha_fin, fecha_inicio, pagado, paquete_id)
VALUES (uuid(),e9297500-3aff-442e-ab27-0b2c4fe8d9c5,'2023-11-23','2023-11-13',true,45f5342f-e868-46b0-a816-20ea6c53a112);

insert into reservas (reserva_id, cliente_id, fecha_fin, fecha_inicio, pagado, paquete_id)
VALUES (uuid(),e9297500-3aff-442e-ab27-0b2c4fe8d9c5,'2023-11-23','2023-11-13',true,45f5342f-e868-46b0-a816-20ea6c53a112);

select * from reservas;

create index if not exists destino_por_usuario on reservas(cliente_id);

select * from reservas where cliente_id=e9297500-3aff-442e-ab27-0b2c4fe8d9c5;

create index if not exists paquetes_por_destinos on paquetes(destino_id);

select * from paquetes where destino_id=6ea6130e-c0a7-4d85-bd86-79df2e05dae4;
