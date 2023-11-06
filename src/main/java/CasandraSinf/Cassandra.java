package CasandraSinf;

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
            return new Paquete(
                    row.getUUID(0).toString(),
                    row.getUUID(1).toString(),
                    row.getInt(2),
                    row.getString(3),
                    row.getDecimal(4)
            );
        } else {
            return null; // O algún tipo de manejo para indicar que no se encontró el paquete
        }
    }

    @Override
    public LinkedList<Reserva> reservasByClienteID(String cliente_id) {
        return null;
    }

    @Override
    public LinkedList<Paquete> paquetesByDestinoID(String destino_id) {
        return null;
    }

    @Override
    public LinkedList<Cliente> clientesByReservaRngDate(String fecha_inicio, String fecha_fin) {
        return null;
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
