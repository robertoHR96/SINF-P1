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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import javax.print.Doc;

import static java.util.Arrays.asList;

import java.util.LinkedList;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Mongo implements DataBase {
    private MongoClient mongoClient;
    private MongoDatabase db;
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
            /*
            db.createCollection("destinos");
            rellenarDestinos();
            db.createCollection("paquetes");
            rellenarPaquetes();
            rellenarClientes();
            rellenarReservas();
            MongoCollection<Document> collection = db.getCollection("paquetes");
            collection.createIndex(new Document("destino_id", 1));

            MongoCollection<Document> collection = db.getCollection("reservas");
            collection.createIndex(new Document("cleinte_id", 1));
            MongoCollection<Document> reservasCollection = db.getCollection("reservas");
            reservasCollection.createIndex(new Document("cliente_id", 1)
                    .append("fecha_inicio", 1)
                    .append("fecha_fin", 1));
            reservasCollection.createIndex(
                    new Document("fecha_inicio", 1)
                            .append("fecha_fin", 1)
            );

            MongoCollection<Document> collection = db.getCollection("paquetes");
            collection.createIndex(new Document("nombre", 1));

            MongoCollection<Document> collection = db.getCollection("destinos");
            collection.createIndex(new Document("clima", 1));

            MongoCollection<Document> collection = db.getCollection("destinos");
            collection.createIndex(new Document("pais", 1));
            MongoCollection<Document> collection = db.getCollection("clientes");
            collection.createIndex(new Document("correo_electronico", 1));
            MongoCollection<Document> collection = db.getCollection("paquetes");
            collection.createIndex(new Document("destino_id", 1).append("duracion",1));
             */


        } catch (Exception e) {
            System.out.println("Error al conectarse a la bd");
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        mongoClient.close();
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
            listaDestinos.add(new Destino(doc.getObjectId("_id").toString(), doc.getString("nombre"), doc.getString("pais"), doc.getString("descripcion"), doc.getString("clima")));
        }
        return listaDestinos;
    }

    @Override
    public Paquete paqueteByID(String paquete_id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(paquete_id);
        } catch (IllegalArgumentException e) {
            return null; // Si el ID no es válido como ObjectId
        }
        Bson filter = Filters.eq("_id", objectId);
        FindIterable<Document> paquetes = db.getCollection("paquetes").find(filter);
        Iterator<Document> it = paquetes.iterator();
        Paquete paq;
        while (it.hasNext()) {
            Document doc = it.next();
            Decimal128 precioDecimal = doc.get("precio", Decimal128.class);
            BigDecimal precio = precioDecimal.bigDecimalValue();
            return new Paquete(doc.getObjectId("_id").toString(), doc.getString("destino_id"), doc.getInteger("duracion"), doc.getString("nombre"), precio);
        }

        return null;
    }

    @Override
    public LinkedList<Reserva> reservasByClienteID(String cliente_id) {
        LinkedList<Reserva> listaReservas = new LinkedList<Reserva>();
        Bson filter = Filters.eq("cliente_id", cliente_id);
        FindIterable<Document> clientes = db.getCollection("reservas").find(filter);
        Iterator<Document> it = clientes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            listaReservas.add(
                    // (String reserva_id,  String cliente_id, String fecha_inicio, String fecha_fin, boolean pagado, String paquete_id)
                    new Reserva(doc.getObjectId("_id").toString(), doc.getString("cliente_id"), doc.getDate("fecha_inicio").toString(), doc.getDate("fecha_fin").toString(), doc.getBoolean("pagado"), doc.getString("paquete_id")

                    ));
        }
        return listaReservas;
    }

    @Override
    public LinkedList<Paquete> paquetesByDestinoID(String destino_id) {

        LinkedList<Paquete> listaPaquetes = new LinkedList<>();
        Bson filter = Filters.eq("destino_id", destino_id);
        FindIterable<Document> paquetes = db.getCollection("paquetes").find(filter);
        Iterator<Document> it = paquetes.iterator();
        Paquete paq;
        while (it.hasNext()) {
            Document doc = it.next();
            Decimal128 precioDecimal = doc.get("precio", Decimal128.class);
            BigDecimal precio = precioDecimal.bigDecimalValue();
            listaPaquetes.add(new Paquete(doc.getObjectId("_id").toString(), doc.getString("destino_id"), doc.getInteger("duracion"), doc.getString("nombre"), precio));
        }
        return listaPaquetes;
    }

    @Override
    public LinkedList<Cliente> clientesByReservaRngDate(String fecha_inicio, String fecha_fin) {

        LinkedList<Cliente> listaClientes = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date fechaInicio = sdf.parse(fecha_inicio);
            Date fechaFin = sdf.parse(fecha_fin);

            Document query = new Document("fecha_inicio", new Document("$gte", fechaInicio).append("$lte", fechaFin)).append("fecha_fin", new Document("$gte", fechaInicio).append("$lte", fechaFin));

            FindIterable<Document> clientes = db.getCollection("reservas").find(query);

            Iterator<Document> it = clientes.iterator();
            while (it.hasNext()) {
                Document doc = it.next();
                Cliente clt = traerClienteByID(doc.getString("cliente_id"));
                if (clt != null) {
                    if (anadirCliente(clt.getCliente_id(), listaClientes)) {
                        listaClientes.add(clt);
                    }
                }
            }
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        return listaClientes;
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

    public Cliente traerClienteByID(String cliente_id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(cliente_id);
        } catch (IllegalArgumentException e) {
            return null; // Si el ID no es válido como ObjectId
        }
        Bson filter = Filters.eq("_id", objectId);
        FindIterable<Document> cliente = db.getCollection("clientes").find(filter);
        Iterator<Document> it = cliente.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            // (String cliente_id, String correo_electronico, String nombre, String telefono) {
            return new Cliente(doc.getObjectId("_id").toString(), doc.getString("correo_electronico"), doc.getString("nombre"), doc.getString("telefono")

            );
        }
        return null;
    }

    @Override
    public LinkedList<Paquete> paquetesByNombre(String nombre) {
        LinkedList<Paquete> listaPaquetes = new LinkedList<>();
        Bson filter = Filters.eq("nombre", nombre);
        FindIterable<Document> paquetes = db.getCollection("paquetes").find(filter);
        Iterator<Document> it = paquetes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            Decimal128 precioDecimal = doc.get("precio", Decimal128.class);
            BigDecimal precio = precioDecimal.bigDecimalValue();
            listaPaquetes.add(
                    // (String paquete_id, String destino_id, Integer duracion, String nombre, BigDecimal precio)
                    new Paquete(doc.getObjectId("_id").toString(), doc.getString("destino_id"), doc.getInteger("duracion"), doc.getString("nombre"), precio));
        }

        return listaPaquetes;
    }

    @Override
    public LinkedList<Cliente> clienteResvervasByClima(String clima) {
        LinkedList<Cliente> listaClientes = new LinkedList<>();
        LinkedList<Reserva> listaReservas = new LinkedList<>();
        Bson filter = Filters.eq("clima", clima);
        FindIterable<Document> reservas = db.getCollection("reservas").find();
        Iterator<Document> it = reservas.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            // (String reserva_id,  String cliente_id, String fecha_inicio, String fecha_fin, boolean pagado, String paquete_id)
            Reserva rsv = new Reserva(doc.getObjectId("_id").toString(), doc.getString("cliente_id"), doc.getDate("fecha_inicio").toString(), doc.getDate("fecha_fin").toString(), doc.getBoolean("pagado"), doc.getString("paquete_id")

            );
            if (reservaClimaOk(rsv.getPaquete_id(), clima)) {
                Cliente clt = traerClienteByID(rsv.getCliente_id());
                if (anadirCliente(clt.getCliente_id(), listaClientes)) {
                    listaClientes.add(clt);
                }
            }

        }
        return listaClientes;
    }

    @Override
    public LinkedList<Reserva> resservaByClienteRngDate(String cliente_id, String fecha_inicio, String fecha_fin) {

        LinkedList<Reserva> listaReserva = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date fechaInicio = sdf.parse(fecha_inicio);
            Date fechaFin = sdf.parse(fecha_fin);

            Document query = new Document("fecha_inicio", new Document("$gte", fechaInicio).append("$lte", fechaFin)).append("fecha_fin", new Document("$gte", fechaInicio).append("$lte", fechaFin));

            FindIterable<Document> clientes = db.getCollection("reservas").find(query);

            Iterator<Document> it = clientes.iterator();
            while (it.hasNext()) {
                Document doc = it.next();
                listaReserva.add(
                        new Reserva(doc.getObjectId("_id").toString(), doc.getString("cliente_id"), doc.getDate("fecha_inicio").toString(), doc.getDate("fecha_fin").toString(), doc.getBoolean("pagado"), doc.getString("paquete_id")
                        ));
            }
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        return listaReserva;
    }

    public boolean reservaClimaOk(String paquete_id, String clima) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(paquete_id);
        } catch (IllegalArgumentException e) {
            return false;
        }
        Bson filter = Filters.eq("_id", objectId);
        FindIterable<Document> paquetes = db.getCollection("paquetes").find(filter);
        Iterator<Document> it = paquetes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            String destino_id = doc.getString("destino_id");
            if (destinoClimaOk(destino_id, clima)) {
                return true;
            }
        }
        return false;
    }

    public boolean destinoClimaOk(String destino_id, String clima) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(destino_id);
        } catch (IllegalArgumentException e) {
            return false;
        }
        Bson filter = Filters.eq("_id", objectId);
        FindIterable<Document> paquetes = db.getCollection("destinos").find(filter);
        Iterator<Document> it = paquetes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            if (doc.getString("clima").equals(clima)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LinkedList<Destino> destinosByPais(String pais) {
        LinkedList<Destino> listaDestinos = new LinkedList<>();
        Bson filter = Filters.eq("pais", pais);
        FindIterable<Document> destinos = db.getCollection("destinos").find(filter);
        Iterator<Document> it = destinos.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            listaDestinos.add(
                    // (String destino_id, String nombre, String pais, String descripcion, String clima)
                    new Destino(doc.getObjectId("_id").toString(), doc.getString("nombre"), doc.getString("pais"), doc.getString("descripcion"), doc.getString("clima")));
        }
        return listaDestinos;
    }

    @Override
    public LinkedList<Cliente> clientesByEmail(String mail) {
        LinkedList<Cliente> listaClientes = new LinkedList<>();
        Bson filter = Filters.eq("correo_electronico", mail);
        FindIterable<Document> clientes = db.getCollection("clientes").find(filter);
        Iterator<Document> it = clientes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            listaClientes.add(new Cliente(doc.getObjectId("_id").toString(), doc.getString("correo_electronico"), doc.getString("nombre"), doc.getString("telefono")));
        }
        return listaClientes;
    }

    @Override
    public LinkedList<Paquete> paqueteByDestinoDuracion(String destino_id, int duracion) {
        LinkedList<Paquete> listaPaquetes = new LinkedList<>();
        Document filter = new Document("destino_id", destino_id).append("duracion", duracion);
        FindIterable<Document> paquetes = db.getCollection("paquetes").find(filter);
        Iterator<Document> it = paquetes.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            Decimal128 precioDecimal = doc.get("precio", Decimal128.class);
            BigDecimal precio = precioDecimal.bigDecimalValue();
            listaPaquetes.add(
                    // (String paquete_id, String destino_id, Integer duracion, String nombre, BigDecimal precio)
                    new Paquete(doc.getObjectId("_id").toString(), doc.getString("destino_id"), doc.getInteger("duracion"), doc.getString("nombre"), precio));
        }
        return listaPaquetes;
    }

    @Override
    public LinkedList<Reserva> reservasByClienteDestinoDuracion(String cliente_id, String destino_id, int duracion) {
        LinkedList<Reserva> listaReservas = new LinkedList<>();
        LinkedList<Reserva> listaReservasByCliente = reservasByClienteID(cliente_id);
        LinkedList<Paquete> listaReservasByDestinoDuracion = paqueteByDestinoDuracion(destino_id, duracion);
        Iterator it = listaReservasByCliente.iterator();
        while (it.hasNext()) {
            Reserva rsv = (Reserva) it.next();
            if (estaEnPaquetes(rsv.getPaquete_id(), listaReservasByDestinoDuracion)) {
                listaReservas.add(rsv);
            }
        }

        return listaReservas;
    }

    public boolean estaEnPaquetes(String paquet_id, LinkedList<Paquete> listaPaquetes) {
        Iterator et = listaPaquetes.iterator();
        while (et.hasNext()) {
            Paquete pqt = (Paquete) et.next();
            if (pqt.getPaquete_id().equals(paquet_id)) {
                return true;
            }
        }
        return false;
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
            getDb().getCollection("clientes").insertOne(new Document().append("nombre", name.firstName()).append("correo_electronico", name.username() + "@gmail.com").append("telefono", generarNumeroTelefono()));
        }
    }

    public void rellenarDestinos() {
        for (int i = 0; i < 25; i++) {
            // Obtener la referencia a la colección "destinos"
            getDb().getCollection("destinos").insertOne(new Document().append("nombre", ciudadesPorPais[i][0]).append("pais", nombresPaises[i]).append("descripcion", "Esto es una descripcion").append("clima", "Bueno"));


            getDb().getCollection("destinos").insertOne(new Document().append("nombre", ciudadesPorPais[i][1]).append("pais", nombresPaises[i]).append("descripcion", "Esto es una descripcion para otro pais").append("clima", "Malo"));
        }
    }

    public void rellenarPaquetes() {
        LinkedList<Destino> listaDestinos = todosLosDestinos();
        // (String paquete_id, String destino_id, Integer duracion, String nombre, BigDecimal precio
        Integer[] duraciones = {10, 20, 30};
        BigDecimal[] precios = {new BigDecimal(20.34), new BigDecimal(99.99), new BigDecimal(432.98)};
        for (int i = 0; i < 100; i++) {
            BigDecimal precioRedondeado = precios[i % 3].setScale(2, RoundingMode.HALF_UP);
            getDb().getCollection("paquetes").insertOne(new Document().append("destino_id", listaDestinos.get(i % 50).getDestino_id()).append("duracion", duraciones[i % 3]).append("nombre", "Paquete :" + i).append("precio", new Decimal128(precioRedondeado)));
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

                getDb().getCollection("reservas").insertOne(new Document().append("paquete_id", listaPaquetes.get(i % 100).getPaquete_id()).append("cliente_id", listaClientes.get(i % 200).getCliente_id()).append("fecha_inicio", (randomStartDate)).append("fecha_fin", (randomEndDate)).append("pagado", pagados[i % 2]));
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
                    new Cliente(doc.getObjectId("_id").toString(), doc.getString("correo_electronico"), doc.getString("nombre"), doc.getString("telefono")

                    ));
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
                    new Paquete(doc.getObjectId("_id").toString(), doc.getString("destino_id"), doc.getInteger("duracion"), doc.getString("nombre"), precio));
        }

        return listaPaquetes;
    }
}
