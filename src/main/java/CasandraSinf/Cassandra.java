package CasandraSinf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import com.github.javafaker.Faker;
import com.datastax.driver.core.*;
import com.github.javafaker.Name;
import org.bson.Document;

public class Cassandra implements DataBase {
    private Cluster cluster;
    private Session session;
    private String[] nombresPaises = {"Estados Unidos", "Canadá", "México", "Brasil", "Argentina", "Reino Unido", "Francia", "Alemania", "Italia", "España", "Portugal", "Australia", "Japón", "China", "India", "Rusia", "Sudáfrica", "Egipto", "Nigeria", "Kenia", "Corea del Sur", "Indonesia", "Malasia", "Nueva Zelanda", "Países Bajos"};
    private String[][] ciudadesPorPais = {{"Nueva York", "Los Ángeles"},// Ciudades para Estados Unidos
            {"Toronto", "Montreal"},// Ciudades para Canadá
            {"Ciudad de México", "Guadalajara"},// Ciudades para México
            {"São Paulo", "Río de Janeiro"},// Ciudades para Brasil
            {"Buenos Aires", "Córdoba"},// Ciudades para Argentina
            {"Londres", "Manchester"},  // Reino Unido
            {"París", "Marsella"},      // Francia
            {"Berlín", "Múnich"},       // Alemania
            {"Roma", "Milán"},          // Italia
            {"Madrid", "Barcelona"},    // España
            {"Lisboa", "Oporto"},       // Portugal
            {"Sídney", "Melbourne"},    // Australia
            {"Tokio", "Osaka"},         // Japón
            {"Pekín", "Shanghái"},      // China
            {"Nueva Delhi", "Bombay"},  // India
            {"Moscú", "San Petersburgo"},// Rusia
            {"Johannesburgo", "Ciudad del Cabo"}, // Sudáfrica
            {"El Cairo", "Alejandría"}, // Egipto
            {"Lagos", "Abuya"},         // Nigeria
            {"Nairobi", "Mombasa"},     // Kenia
            {"Seúl", "Busan"},          // Corea del Sur
            {"Yakarta", "Bandung"},     // Indonesia
            {"Kuala Lumpur", "George Town"},  // Malasia
            {"Auckland", "Wellington"}, // Nueva Zelanda
            {"Ámsterdam", "Róterdam"}   // Países Bajos
    };

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


    @Override
    public void crearDataBase() {
        System.out.println("Cargando datos en la BD de cassandra...");
        System.out.println();
        System.out.println("Creando keyspace...");
        System.out.println();
        session.execute("create keyspace if not exists cassandraP1 WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};");
        session.execute("use cassandraP1;");
        session.execute("create table if not exists destinos ( destino_id  UUID, nombre text, pais text, descripcion text, clima text, PRIMARY KEY ( destino_id) ); ");
        session.execute("create table if not exists paquetes( paquete_id uuid, nombre text, destino_id uuid, duracion int, precio decimal, PRIMARY KEY ( paquete_id ) ); ");
        session.execute("create table if not exists clientes (cliente_id uuid, nombre text, correo_electronico text, telefono text, primary key ( cliente_id ) ); ");
        session.execute("create table if not exists reservas ( reserva_id uuid, paquete_id uuid, cliente_id uuid, fecha_inicio date, fecha_fin date, pagado boolean, primary key ( reserva_id ) );");
        session.execute("create index if not exists destino_clima on destinos (pais);");
        session.execute("create index if not exists destino_clima on destinos (clima);");

        session.execute("create index if not exists paquetes_por_destinos on paquetes (pais);");
        session.execute("create index if not exists paquetes_por_nombres on paquetes (nombre);");

        session.execute("create index if not exists destino_por_usuario on reservas (cliente_id);");
        session.execute("create index if not exists cliente_por_fehcas on reservas (fecha_inicio);");

        session.execute("use cassandraP1;");
        session.execute("CREATE TABLE if not exists reservas_paquetes (reserva_paquete_id uuid, numero_reservas int, paquete_id text, PRIMARY KEY (reserva_paquete_id));");
        session.execute("CREATE index if not exists ON reservas (paquete_id);");
        session.execute("create table if not exists destinos_populares (destino_popular_id uuid, destino_id text, primary key (destino_popular_id) );");
        session.execute("create index if not exists on destinos_populares(destino_id);");

        rellenarDatos();
    }

    public void rellenarDatos() {
        rellenarDestinos();
        rellenarPaquetes();
        rellenarClientes();
        rellenarReservas();
        rellenarReservasPaquetes();

        rellenarDestinoPopulares();
    }

    public void rellenarDestinos() {
        session.execute("use cassandrap1;");
        for (int i = 0; i < 25; i++) {
            session.execute("insert into destinos (destino_id, nombre, pais, descripcion, clima)" +
                    "values (uuid(), '" + ciudadesPorPais[i][0] + "', '" + nombresPaises[i] + "', 'Esto es una descripcion', 'Bueno');");
            session.execute("insert into destinos (destino_id, nombre, pais, descripcion, clima)" +
                    "values (uuid(), '" + ciudadesPorPais[i][1] + "', '" + nombresPaises[i] + "', 'Esto es una descripcion', 'Malo');");

        }
    }

    public void rellenarPaquetes() {
        LinkedList<Destino> listaDestinos = todosLosDestinos();
        Integer[] duraciones = {10, 20, 30};
        BigDecimal[] precios = {new BigDecimal(20.34), new BigDecimal(99.99), new BigDecimal(432.98)};
        session.execute("use cassandrap1;");
        for (int i = 0; i < 100; i++) {
            session.execute("insert into paquetes (paquete_id, nombre, destino_id, duracion, precio)" +
                    "values (uuid(), 'Destino_" + i + "', " + listaDestinos.get(i % 50).getDestino_id() + ", " + duraciones[i % 3] + ", " + precios[i % 3] + " );");
        }
    }

    public void rellenarClientes() {
        Faker faker = new Faker(new Locale("es"));
        Name name;
        session.execute("use cassandrap1;");
        for (int i = 0; i < 200; i++) {
            name = faker.name();
            session.execute("insert into clientes (cliente_id, correo_electronico, nombre, telefono)" +
                    "values (uuid(), '" + name.username() + "@gmail.com', '" + name.firstName() + "', '" + generarNumeroTelefono() + "');");
        }
    }

    public static String generarNumeroTelefono() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        // Generar el primer dígito (6 o 7)
        int primerDigito = 6 + random.nextInt(2); // Genera un número aleatorio entre 6 y 7 (inclusive)
        builder.append(primerDigito);

        // Generar los siguientes 8 dígitos
        for (int i = 0; i < 8; i++) {
            builder.append(random.nextInt(10)); // Genera dígitos aleatorios del 0 al 9
        }

        return builder.toString();
    }


    public void rellenarReservas() {
        LinkedList<Cliente> listaClientes = todosLosClientes();
        LinkedList<Paquete> listaPaquetes = todosLosPaquetes();
        session.execute("use cassandrap1;");
        for (int i = 0; i < 500; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -5);
            Date startDate = cal.getTime(); // Fecha mínima

            cal = Calendar.getInstance();
            Date endDate = cal.getTime(); // Fecha máxima (fecha actual)

            Random random = new Random();
            long randomStartMillis = startDate.getTime() + (long) (random.nextDouble() * (endDate.getTime() - startDate.getTime()));
            cal.setTimeInMillis(randomStartMillis);
            Date randomStartDate = cal.getTime();

            cal.add(Calendar.DAY_OF_YEAR, random.nextInt(30)); // Agregar días aleatorios para fecha fin
            Date randomEndDate = cal.getTime();

            String fechaInit = dateFormat.format(randomStartDate);
            String fechaEnd = dateFormat.format(randomEndDate);
            if (i % 2 == 0) {
                session.execute("insert into reservas (reserva_id, cliente_id, fecha_fin, fecha_inicio, pagado, paquete_id)" +
                        "VALUES (uuid(), " + listaClientes.get(i % 200).getCliente_id() + ", '" + fechaEnd + "', '" + fechaInit + "'," + true + "," + listaPaquetes.get(i % 100).getPaquete_id() + ");");
            } else {
                session.execute("insert into reservas (reserva_id, cliente_id, fecha_fin, fecha_inicio, pagado, paquete_id)" +
                        "VALUES (uuid(), " + listaClientes.get(i % 200).getCliente_id() + ", '" + fechaEnd + "', '" + fechaInit + "'," + false + "," + listaPaquetes.get(i % 100).getPaquete_id() + ");");
            }
        }
    }

    public void rellenarReservasPaquetes() {
        session.execute("use cassandraP1;");
        LinkedList<Paquete> listaPaquetes = todosLosPaquetes();
        Iterator it = listaPaquetes.iterator();
        while (it.hasNext()) {
            Paquete pqt = (Paquete) it.next();
            ResultSet resultados = session.execute("SELECT paquete_id, COUNT(*) AS numero_reservas FROM reservas WHERE paquete_id = " + pqt.getPaquete_id() + ";");
            for (Row row : resultados) {
                BigInteger cont = new BigInteger(String.valueOf(row.getLong("numero_reservas")));
                if (row.getUUID("paquete_id") != null) {
                    session.execute("insert into reservas_paquetes (reserva_paquete_id, numero_reservas, paquete_id) values (uuid(), " + cont.intValue() + " , '" + pqt.getPaquete_id() + "');");
                }
            }
        }

    }

    public void rellenarDestinoPopulares() {
        session.execute("use cassandraP1;");
        LinkedList<Reserva> listaReservas = todasLasReservas();
        Iterator it = listaReservas.iterator();
        while (it.hasNext()) {
            Reserva rsv = (Reserva) it.next();
            Paquete pqt = paqueteByID(rsv.getPaquete_id());
            String destino_id = pqt.getDestino_id();
            session.execute("insert into destinos_populares (destino_popular_id, destino_id) values (uuid(), '" + destino_id + "' );");
        }
    }

    public LinkedList<Reserva> todasLasReservas() {
        LinkedList<Reserva> listaReservas = new LinkedList<Reserva>();
        session.execute("use cassandrap1;");
        String qury = "Select * from reservas;";
        ResultSet resultSet = session.execute(qury);
        for (Row row : resultSet) {
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


    public LinkedList<Paquete> todosLosPaquetes() {
        LinkedList<Paquete> listaPaquetes = new LinkedList<>();
        session.execute("use cassandrap1;");
        String cliente_id = "Select * from paquetes;";

        ResultSet rs = session.execute(cliente_id);
        for (Row row : rs) {
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

    public LinkedList<Cliente> todosLosClientes() {
        LinkedList<Cliente> listaClientes = new LinkedList<>();
        session.execute("use cassandrap1;");
        ResultSet rs = session.execute("Select * from clientes ;");
        for (Row row : rs) {
            listaClientes.add(
                    new Cliente(
                            row.getUUID(0).toString(),
                            row.getString(1),
                            row.getString(2),
                            row.getString(3)
                    )
            );
        }
        return listaClientes;
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
    public void reservasPorPaquetes() {
        session.execute("use cassandrap1;");
        ResultSet result = session.execute(" select * from reservas_paquetes ;");
        for (Row row : result) {
            Paquete pqt = paqueteByID(row.getString("paquete_id"));
            int cont = row.getInt("numero_reservas");
            System.out.println("----------------");
            System.out.println(pqt.toString());
            System.out.println("Numero reservas: " + cont);
        }
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
    public LinkedList<Reserva> resservaByClienteRngDate(String cliente_id, String fecha_inicio, String fecha_fin) {
        return null;
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
    public void destinosMasPopulares() {
        try {
            session.execute("use cassandrap1");
            // Ejecutar la consulta
            ResultSet rs = session.execute("SELECT destino_id from destinos_populares");
            HashMap<String, Integer> destinosContador = new HashMap<>();
            // Iterar a través de los resultados
            // y rellenamos el hashmap
            for (Row row : rs) {
                String destinoId = row.getString("destino_id");
                if (destinosContador.containsKey(destinoId)) {
                    int cantidad = destinosContador.get(destinoId);
                    cantidad++;
                    destinosContador.put(destinoId, cantidad);
                } else {
                    destinosContador.put(destinoId, 0);
                }
            }
            // recorremos le has map para mostar los resultados
            for (Map.Entry<String, Integer> entry : destinosContador.entrySet()) {
                String destino_id = entry.getKey();
                int cantidad = entry.getValue();
                Destino dst = destinoById(destino_id);
                System.out.println("----");
                System.out.println(dst.toString());
                System.out.println("Numero veces: "+cantidad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Destino destinoById(String destino_id){
        session.execute("use cassandrap1");
        // Ejecutar la consulta
        ResultSet rs = session.execute("SELECT * from destinos where destino_id = "+destino_id);
        Row row = rs.one();
        return new Destino(row.getUUID(0).toString(), row.getString(3), row.getString(4), row.getString(2), row.getString(1));
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
        LinkedList<Paquete> listaPaquetes = new LinkedList<Paquete>();
        session.execute("use cassandrap1");
        ResultSet resultSet = session.execute("select * from paquetes where destino_id = " + destino_id + " and duracion = '" + duracion + "' ;");
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
        System.out.println("Indices compuestos - Error Cassandra");
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
