package CasandraSinf;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

import com.github.javafaker.Faker;
import com.datastax.driver.core.*;

public class Cassandra implements DataBase {
    private Cluster cluster;
    private Session session;

    public Cassandra() {

    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void createSchema() {
        session.execute("CREATE KEYSPACE IF NOT EXISTS simplex WITH replication " + "= {'class':'SimpleStrategy', 'replication_factor':1};");
        session.execute("CREATE TABLE IF NOT EXISTS simplex.songs (" + "id uuid PRIMARY KEY," + "title text," + "album text," + "artist text," + "tags set<text>," + "data blob" + ");");
        session.execute("CREATE TABLE IF NOT EXISTS simplex.playlists (" + "id uuid," + "title text," + "album text, " + "artist text," + "song_id uuid," + "PRIMARY KEY (id, title, album, artist)" + ");");
    }

    public void loadData() {
        session.execute("INSERT INTO simplex.songs (id, title, album, artist, tags) " + "VALUES (" + "756716f7-2e54-4715-9f00-91dcbea6cf50," + "'La Petite Tonkinoise'," + "'Bye Bye Blackbird'," + "'Joséphine Baker'," + "{'jazz', '2013'})" + ";");
        session.execute("INSERT INTO simplex.playlists (id, song_id, title, album, artist) " + "VALUES (" + "2cc9ccb7-6221-4ccb-8387-f22b6a1b354d," + "756716f7-2e54-4715-9f00-91dcbea6cf50," + "'La Petite Tonkinoise'," + "'Bye Bye Blackbird'," + "'Joséphine Baker'" + ");");
    }

    public void querySchema() {
        ResultSet results = session.execute("SELECT * FROM simplex.playlists " + "WHERE id = 2cc9ccb7-6221-4ccb-8387-f22b6a1b354d;");
        System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "title", "album", "artist", "-------------------------------+-----------------------+--------------------"));
        for (Row row : results) {
            System.out.println(String.format("%-30s\t%-20s\t%-20s", row.getString("title"), row.getString("album"), row.getString("artist")));
        }
        System.out.println();
    }

    @Override
    public void crearDataBase() {
        System.out.println("Cargando datos en la BD de cassandra...");
        System.out.println();
        System.out.println("Creando keyspace...");
        System.out.println();
        ResultSet resultSet = session.execute("create keyspace if not exists cassandraP1 WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};");
        resultSet = session.execute("use cassandraP1;");
        System.out.println("Crando tablas...");
        System.out.println();
        resultSet = session.execute("create table if not exists destinos ( destino_id  UUID, nombre text, pais text, descripcion text, clima text, PRIMARY KEY ( destino_id) ); ");
        resultSet = session.execute("create table if not exists paquetes( paquete_id uuid, nombre text, destino_id uuid, duracion int, precio decimal, PRIMARY KEY ( paquete_id ) ); ");
        resultSet = session.execute("create table if not exists clientes (cliente_id uuid, nombre text, correo_electronico text, telefono text, primary key ( cliente_id ) ); ");
        resultSet = session.execute("create table if not exists reservas ( reserva_id uuid, paquete_id uuid, cliente_id uuid, fecha_inicio date, fecha_fin date, pagado boolean, primary key ( reserva_id ) );");
        System.out.println("Creando indices adicionales...");
        System.out.println("");
        resultSet = session.execute("create index if not exists destino_clima on destinos (pais);");
        resultSet = session.execute("create index if not exists destino_clima on destinos (clima);");

        resultSet = session.execute("create index if not exists paquetes_por_destinos on paquetes (pais);");
        resultSet = session.execute("create index if not exists paquetes_por_nombres on paquetes (nombre);");

        resultSet = session.execute("create index if not exists destino_por_usuario on reservas (cliente_id);");
        resultSet = session.execute("create index if not exists cliente_por_fehcas on reservas (fecha_inicio);");

        /*
        resultSet = session.execute("");
         */
    }

    @Override
    public void connect(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
        }
        session = cluster.connect();
    }

    @Override
    public void close() {
        session.close();
        cluster.close();
        System.out.println("Connection closed");
    }

    @Override
    public LinkedList<Destino> todosLosDestinos() {
        session.execute("use cassandrap1;");
        ResultSet results = session.execute("select * from destinos;");

        LinkedList<Destino> destinos = new LinkedList<Destino>();
        for (Row row : results)
            destinos.add(new Destino(row.getUUID(0).toString(), row.getString(3), row.getString(4), row.getString(2), row.getString(1)));
        return destinos;
    }

    @Override
    public Paquete paqueteByID(String paquete_id) {
        session.execute("use cassandrap1;");
        ResultSet results = session.execute("select * from paquetes where paquete_id=" + paquete_id + ";");
        Row row = results.one(); // Obtener una única fila de resultados
        //results.iterator()
        //results.all()
        if (row != null) {
            return new Paquete(row.getUUID(0).toString(), row.getUUID(1).toString(), row.getInt(2), row.getString(3), row.getDecimal(4));
        } else {
            return null; // O algún tipo de manejo para indicar que no se encontró el paquete
        }
    }

    @Override
    public LinkedList<Reserva> reservasByClienteID(String cliente_id) {
        session.execute("use cassandrap1;");

        ResultSet result = session.execute(" select * from reservas where cliente_id = " + cliente_id + " ;");
        LinkedList<Reserva> listaReservas = new LinkedList<Reserva>();
        for (Row row : result) {
            listaReservas.add(new Reserva(
                    row.getUUID(0).toString(),
                    row.getUUID(1).toString(),
                    row.getDate(2).toString(),
                    row.getDate(3).toString(),
                    row.getBool(4),
                    row.getUUID(5).toString()));
        }
        return listaReservas;
    }

    @Override
    public LinkedList<Paquete> paquetesByDestinoID(String destino_id) {
        session.execute("use cassandrap1;");
        ResultSet result = session.execute("select * from paquetes where destino_id = " + destino_id + " ;");
        LinkedList<Paquete> listaDestinos = new LinkedList<Paquete>();
        for (Row row : result) {
            listaDestinos.add(
                    new Paquete(
                            row.getUUID(0).toString(),
                            row.getUUID(1).toString(),
                            row.getInt(2),
                            row.getString(3),
                            row.getDecimal(4)
                    )
            );
        }
        return listaDestinos;
    }

    @Override
    public LinkedList<Cliente> clientesByReservaRngDate(String fecha_inicio, String fecha_fin) {
        session.execute("use cassandrap1");
        ResultSet resultSet = session.execute("SELECT * FROM reservas WHERE fecha_inicio >= '" + fecha_inicio + "' AND fecha_fin <= '" + fecha_fin + "' allow filtering ;");
        LinkedList<Cliente> listaClientes = new LinkedList<Cliente>();
        for (Row row : resultSet) {
            String cliente_id = "Select * from clientes where cliente_id = " + row.getUUID(1).toString() + " ;";
            ResultSet rs = session.execute(cliente_id);
            Row row2 = rs.one();
            if (anadirCliente(row2.getUUID(0).toString(), listaClientes)) {
                listaClientes.add(
                        new Cliente(
                                row2.getUUID(0).toString(),
                                row2.getString(1),
                                row2.getString(2),
                                row2.getString(3)
                        )
                );
            }
        }
        return listaClientes;
    }

    @Override
    public LinkedList<Paquete> paquetesByNombre(String nombre) {
        session.execute("use cassandrap1");
        ResultSet resultSet = session.execute("select * from paquetes where nombre = '" + nombre + "' ;");
        LinkedList<Paquete> listaPaquetes = new LinkedList<Paquete>();
        for (Row row : resultSet) {
            listaPaquetes.add(
                    new Paquete(
                            row.getUUID(0).toString(),
                            row.getUUID(1).toString(),
                            row.getInt(2),
                            row.getString(3),
                            row.getDecimal(4)
                    )
            );
        }
        return listaPaquetes;
    }

    @Override
    public LinkedList<Cliente> clienteResvervasByClima(String clima) {
        session.execute("use cassandrap1;");

        ResultSet result = session.execute(" select * from reservas ;");
        LinkedList<Reserva> listaReservas = new LinkedList<Reserva>();
        LinkedList<Cliente> listaCliente = new LinkedList<Cliente>();
        for (Row row : result) {
            Reserva rsv = new Reserva(
                    row.getUUID(0).toString(),
                    row.getUUID(1).toString(),
                    row.getDate(2).toString(),
                    row.getDate(3).toString(),
                    row.getBool(4),
                    row.getUUID(5).toString());

            if (reservaCumpleClima(row.getUUID(5).toString(), clima)) {
                if (anadirCliente(row.getUUID(1).toString(), listaCliente)) {
                    Cliente clt = clienteById(row.getUUID(1).toString());
                    if (clt != null) {
                        listaCliente.add(clt);
                    }
                }
            }
        }
        return listaCliente;
    }

    @Override
    public LinkedList<Destino> destinosByPais(String pais) {
        LinkedList<Destino> listaDestinos = new LinkedList<Destino>();
        session.execute("use cassandrap1");
        ResultSet resultSet = session.execute("select * from destinos where pais = '" + pais + "' ;");
        for (Row row : resultSet) {
            listaDestinos.add(new Destino(row.getUUID(0).toString(), row.getString(3), row.getString(4), row.getString(2), row.getString(1)));
        }
        return listaDestinos;
    }

    @Override
    public LinkedList<Cliente> clientesByEmail(String mail) {
        LinkedList<Cliente> listaClientes = new LinkedList<Cliente>();
        session.execute("use cassandrap1");
        ResultSet resultSet = session.execute("select * from clientes where correo_electronico = '" + mail + "' ;");
        for (Row row : resultSet) {
            listaClientes.add(new Cliente(
                    row.getUUID(0).toString(),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3)
            ));
        }
        return listaClientes;
    }

    @Override
    public LinkedList<Paquete> paqueteByDestinoDuracion(String destino_id, int duracion) {
        LinkedList <Paquete> listaPaquetes = new LinkedList<Paquete>();
        session.execute("use cassandrap1");
        ResultSet resultSet = session.execute("select * from paquetes where destino_id = "+destino_id+" and duracion = '"+duracion+"' ;");
        for (Row row : resultSet) {
            listaPaquetes.add(
                    new Paquete(
                            row.getUUID(0).toString(),
                            row.getUUID(1).toString(),
                            row.getInt(2),
                            row.getString(3),
                            row.getDecimal(4)
                    )
            );
        }
        return listaPaquetes;
    }

    @Override
    public LinkedList<Reserva> reservasByClienteDestinoDuracion(String cliente_id, String destino_id, int duracion) {
        return null;
    }

    public Cliente clienteById(String id_cliente) {

        session.execute("use cassandrap1;");
        ResultSet resultSet = session.execute("select * from clientes where cliente_id =" + id_cliente + " ;");
        Row row = resultSet.one();
        if (row != null) {
            return new Cliente(
                    row.getUUID(0).toString(),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3)
            );
        }
        return null;
    }

    public boolean reservaCumpleClima(String id_paquete, String clima) {
        session.execute("use cassandrap1;");
        ResultSet resultSet = session.execute("select * from paquetes where paquete_id = " + id_paquete + " ;");
        LinkedList<Paquete> listaPaquetes = new LinkedList<Paquete>();
        for (Row row : resultSet) {
            String destino_id = row.getUUID(1).toString();
            if (destinoConClima(destino_id, clima)) {
                return true;
            }
        }
        return false;
    }

    public boolean destinoConClima(String id_destino, String clima) {
        session.execute("use cassandrap1");
        ResultSet resultSet = session.execute("select * from destinos where destino_id=" + id_destino + " and clima='" + clima + "' ;");
        Row row = resultSet.one();
        if (row != null) {
            return true;
        }
        return false;
    }

    public boolean anadirCliente(String cId, LinkedList<Cliente> lisCl) {
        Iterator it = lisCl.iterator();
        while (it.hasNext()) {
            Cliente cc = (Cliente) it.next();
            if (cc.getCliente_id().equals(cId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cassandra cassandra)) return false;
        return Objects.equals(getCluster(), cassandra.getCluster()) && Objects.equals(getSession(), cassandra.getSession());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCluster(), getSession());
    }

    @Override
    public String toString() {
        return "Cassandra{" + "cluster=" + cluster + ", session=" + session + '}';
    }
}
