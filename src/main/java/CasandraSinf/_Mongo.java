package CasandraSinf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import static java.util.Arrays.asList;

import java.util.LinkedList;

import java.util.logging.Level;
import java.util.logging.Logger;


import com.mongodb.Block;


public class _Mongo {


    private MongoClient mongoClient;
    private MongoDatabase db;

    public _Mongo() {

    }

    /*
     * Conexión al servidor Mongo.
     */

    public void connect() {
        try {
            this.setMongoClient(new MongoClient("localhost", 27017));
            setDb(mongoClient.getDatabase("mongoSinf"));
            System.out.println("Conectado correctamente a la bd");
        } catch (Exception e) {
            System.out.println("Error al conectarse a la bd");
        }
    }

    /*
     * Conexión a la base de datos.
     * Si no existe la crea.
     */
    public void Connect_Collection(String database) {
        setDb(mongoClient.getDatabase(database));
    }

    /*
     * Crear colección, si no existe la crea
     */
    public void crear_coleccion(String coleccion) {
        getDb().getCollection(coleccion);
    }

    /*
     * Eliminar colección
     */
    public void eliminar_coleccion(String coleccion) {
        getDb().getCollection(coleccion).drop();
    }

    /**
     * Usamos el método <b>insertOne</b> para añadir un documento a la base de datos de ejemplo.
     */
    public void insertOneDataTest() {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            // Añadimos un documento a la base de datos directamente.
            getDb().getCollection("restaurants").insertOne(
                    new Document("address", asList(
                            new Document()
                                    .append("street", "Avenida Castrelos 25 Bajo")
                                    .append("zipcode", "36210")
                                    .append("building", "180")
                                    .append("coord", asList(-73.9557413, 40.7720266)),
                            new Document()
                                    .append("street", "Urzáiz 77 Bajo")
                                    .append("zipcode", "36004")
                                    .append("building", "40")
                                    .append("coord", asList(-73.9557413, 40.7720266))))
                            .append("borough", "Vigo")
                            .append("cuisine", "Galician")
                            .append("grades", asList(
                                    new Document()
                                            .append("date", format.parse("2015-10-11T00:00:00Z"))
                                            .append("grade", "A")
                                            .append("score", 12),
                                    new Document()
                                            .append("date", format.parse("2015-12-11T00:00:00Z"))
                                            .append("grade", "B")
                                            .append("score", 18)))
                            .append("name", "Xules"));
        } catch (ParseException ex) {
            Logger.getLogger(Mongo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * listamos los datos de la colección
     */

    public void listRestaurants() {
        // Para devolver todos los documentos en una colección, llamamos al método find sin ningún documento <b>criteria</b>
        FindIterable<Document> iterable = getDb().getCollection("restaurants").find();
        // Iterate the results and apply a block to each resulting document.
        // Iteramos los resultados y aplicacimos un bloque para cada documento.
        iterable.forEach(new Block<Document>() {
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }

    /**
     * Usamos el método <b>insertMany</b> para añadir un conjunto de documentos a la base de datos de ejemplo.
     */
    public void insertManyDataTest() {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            // We create a List<Document> (Creamos una List<Document>).
            LinkedList<Document> dataList = new LinkedList<Document>();
            // We add a document to the list (Añadimos un documento a la lista).
            dataList.add(new Document("address", asList(
                    new Document()
                            .append("street", "Avenida Castrelos 25 Bajo")
                            .append("zipcode", "36210")
                            .append("building", "180")
                            .append("coord", asList(-73.9557413, 40.7720266)),
                    new Document()
                            .append("street", "Urzáiz 77 Bajo")
                            .append("zipcode", "36004")
                            .append("building", "40")
                            .append("coord", asList(-73.9557413, 40.7720266))))
                    .append("borough", "Vigo")
                    .append("cuisine", "Galician")
                    .append("grades", asList(
                            new Document()
                                    .append("date", format.parse("2015-10-11T00:00:00Z"))
                                    .append("grade", "A")
                                    .append("score", 12),
                            new Document()
                                    .append("date", format.parse("2015-12-11T00:00:00Z"))
                                    .append("grade", "B")
                                    .append("score", 18)))
                    .append("name", "Xules"));
            dataList.add(new Document("address", asList(
                    new Document()
                            .append("street", "Avenida Ruz Perez")
                            .append("zipcode", "30204")
                            .append("building", "50")
                            .append("coord", asList(-73.9557413, 40.7720266))))
                    .append("borough", "Ourense")
                    .append("cuisine", "Galician")
                    .append("grades", asList(
                            new Document()
                                    .append("date", format.parse("2015-09-01T00:00:00Z"))
                                    .append("grade", "A")
                                    .append("score", 10),
                            new Document()
                                    .append("date", format.parse("2015-12-01T00:00:00Z"))
                                    .append("grade", "B")
                                    .append("score", 14)))
                    .append("name", "Código Xules"));
            // Now we insert all documents in the database (Ahora introducimos todos los documentos en la base de datos).
            getDb().getCollection("restaurants").insertMany(dataList);
        } catch (ParseException ex) {
            Logger.getLogger(Mongo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Obtenemos la lista de todos los restaurantes de la base de datos filtrando
     * por el tipo de cocina con el paso del parámetro <b>cuisine</b> y los mostramos por pantalla.
     *
     * @param cuisine <code>String</code> data value to filter (valor para filtrar)
     */
    public void listRestaurantsByCuisine(String cuisine) {
        // We return documents with the find method by setting a <b>criteria</ b> element equal to the cuisine.
        // Devolvemos los documentos con el método find estableciendo un <b>criteria</b> igual para el elemento cuisine.
        FindIterable<Document> iterable = getDb().getCollection("restaurants").find(new Document("borough", cuisine));
        // Iterate the results and apply a block to each resulting document.
        // Iteramos los resultados y aplicacimos un bloque para cada documento.
        iterable.forEach(new Block<Document>() {
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }


    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }


}
