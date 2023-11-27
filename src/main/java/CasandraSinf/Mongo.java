package CasandraSinf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.Decimal128;

import static java.util.Arrays.asList;

import java.util.LinkedList;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Mongo implements DataBase {
    private MongoClient mongoClient;
    private MongoDatabase db;
    private String[] nombresPaises = {
            "Estados Unidos",
            "Canadá",
            "México",
            "Brasil",
            "Argentina",
            "Reino Unido",
            "Francia",
            "Alemania",
            "Italia",
            "España",
            "Portugal",
            "Australia",
            "Japón",
            "China",
            "India",
            "Rusia",
            "Sudáfrica",
            "Egipto",
            "Nigeria",
            "Kenia",
            "Corea del Sur",
            "Indonesia",
            "Malasia",
            "Nueva Zelanda",
            "Países Bajos"
    };
    String[][] ciudadesPorPais = {
            {"Nueva York", "Los Ángeles"},// Ciudades para Estados Unidos
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


    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }

    @Override
    public void crearDataBase() {

    }

    @Override
    public void connect(String node) {
        try {
            this.setMongoClient(new MongoClient(node, 27017));
            setDb(mongoClient.getDatabase("mongoSinf"));
            System.out.println("Conectado correctamente a la bd correctamente");
            System.out.println("Rellenando tabla destinos");
            //db.createCollection("destinos");
            //rellenarDestinos();
            //db.createCollection("paquetes");
            //rellenarPaquetes();
            //rellenarClientes();
            rellenarReservas();


        } catch (Exception e) {
            System.out.println("Error al conectarse a la bd");
            e.printStackTrace();
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

    public void rellenarClientes() {
        Faker faker = new Faker(new Locale("es"));
        String nombre;
        String username;
        Name name;
        for (int i = 0; i < 200; i++) {
            name = faker.name();
            getDb().getCollection("clientes").insertOne(
                    new Document()
                            .append("nombre", name.firstName())
                            .append("correo_electronico", name.username() + "@gmail.com")
                            .append("telefono", generarNumeroTelefono())
            );
        }
    }

    public void rellenarDestinos() {
        for (int i = 0; i < 25; i++) {
            // Obtener la referencia a la colección "destinos"
            getDb().getCollection("destinos").insertOne(
                    new Document()
                            .append("nombre", ciudadesPorPais[i][0])
                            .append("pais", nombresPaises[i])
                            .append("descripcion", "Esto es una descripcion")
                            .append("clima", "Bueno")
            );


            getDb().getCollection("destinos").insertOne(
                    new Document()
                            .append("nombre", ciudadesPorPais[i][1])
                            .append("pais", nombresPaises[i])
                            .append("descripcion", "Esto es una descripcion para otro pais")
                            .append("clima", "Malo")
            );
        }
    }

    public void rellenarPaquetes() {
        LinkedList<Destino> listaDestinos = todosLosDestinos();
        // (String paquete_id, String destino_id, Integer duracion, String nombre, BigDecimal precio
        Integer[] duraciones = {10, 20, 30};
        BigDecimal[] precios = {new BigDecimal(20.34), new BigDecimal(99.99), new BigDecimal(432.98)};
        for (int i = 0; i < 100; i++) {
            BigDecimal precioRedondeado = precios[i % 3].setScale(2, RoundingMode.HALF_UP);
            getDb().getCollection("paquetes").insertOne(
                    new Document()
                            .append("destino_id", listaDestinos.get(i % 50).getDestino_id())
                            .append("duracion", duraciones[i % 3])
                            .append("nombre", "Paquete :" + i)
                            .append("precio", new Decimal128(precioRedondeado))
            );
        }
    }

    public void rellenarReservas() {
        LinkedList<Paquete> listaPaquetes = todosPaquetes();
        LinkedList<Cliente> listaClientes = todosClientes();
        Random random = new Random();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        boolean[] pagados = {true, false};
        try {
            for (int i = 0; i < 500; i++) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -5);
                Date startDate = cal.getTime(); // Fecha mínima
                cal = Calendar.getInstance();
                Date endDate = cal.getTime(); // Fecha máxima (fecha actual)
                long randomStartMillis = startDate.getTime() + (long) (random.nextDouble() * (endDate.getTime() - startDate.getTime()));
                cal.setTimeInMillis(randomStartMillis);
                Date randomStartDate = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR, random.nextInt(30)); // Agregar días aleatorios para fecha fin
                Date randomEndDate = cal.getTime();

                getDb().getCollection("reservas").insertOne(
                        new Document()
                                .append("paquete_id", listaPaquetes.get(i % 100).getPaquete_id())
                                .append("cliente_id", listaClientes.get(i % 200).getCliente_id())
                                .append("fecha_inicio", format.format(randomStartDate))
                                .append("fecha_fin", format.format(randomEndDate))
                                .append("pagado", pagados[i % 2])
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Cliente> todosClientes() {
        LinkedList<Cliente> listaClientes = new LinkedList<Cliente>();
        FindIterable<Document> clientes = db.getCollection("clientes").find();
        Iterator<Document> it = clientes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            listaClientes.add(
                    // (String cliente_id, String correo_electronico, String nombre, String telefono) {
                    new Cliente(
                            doc.getObjectId("_id").toString(),
                            doc.getString("nombre"),
                            doc.getString("correo_electronico"),
                            doc.getString("telefono")

                    )
            );
        }
        return listaClientes;
    }

    ;

    public LinkedList<Paquete> todosPaquetes() {
        LinkedList<Paquete> listaPaquetes = new LinkedList<Paquete>();

        FindIterable<Document> paquetes = db.getCollection("paquetes").find();
        Iterator<Document> it = paquetes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            Decimal128 precioDecimal = doc.get("precio", Decimal128.class);
            BigDecimal precio = precioDecimal.bigDecimalValue();
            listaPaquetes.add(
                    // (String paquete_id, String destino_id, Integer duracion, String nombre, BigDecimal precio)
                    new Paquete(
                            doc.getObjectId("_id").toString(),
                            doc.getString("destino_id"),
                            doc.getInteger("duracion"),
                            doc.getString("nombre"),
                            precio
                    )
            );
        }

        return listaPaquetes;
    }

    @Override
    public void close() {

    }

    @Override
    public LinkedList<Destino> todosLosDestinos() {
        LinkedList<Destino> listaDestinos = new LinkedList<Destino>();
        FindIterable<Document> destinos = db.getCollection("destinos").find();
        Iterator<Document> iterator = destinos.iterator();
        // String destino_id, String nombre, String pais, String descripcion, String clima
        int i = 0;
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            listaDestinos.add(
                    new Destino(
                            doc.getObjectId("_id").toString(),
                            doc.getString("nombre"),
                            doc.getString("pais"),
                            doc.getString("descripcion"),
                            doc.getString("clima")
                    )
            );
        }
        return listaDestinos;
    }

    @Override
    public Paquete paqueteByID(String paquete_id) {
        return null;
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
    public LinkedList<Paquete> paquetesByNombre(String nombre) {
        return null;
    }

    @Override
    public LinkedList<Cliente> clienteResvervasByClima(String clima) {
        return null;
    }

    @Override
    public LinkedList<Destino> destinosByPais(String pais) {
        return null;
    }

    @Override
    public LinkedList<Cliente> clientesByEmail(String mail) {
        return null;
    }

    @Override
    public LinkedList<Paquete> paqueteByDestinoDuracion(String destino_id, String duracion) {
        return null;
    }
}
