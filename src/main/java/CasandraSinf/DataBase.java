package CasandraSinf;

import java.util.LinkedList;

public interface DataBase {

    void crearDataBase();
    void connect(String node);
    void close();
    LinkedList<Destino> todosLosDestinos();
    Paquete paqueteByID(String paquete_id);
    LinkedList<Reserva> reservasByClienteID(String cliente_id);
    LinkedList<Paquete> paquetesByDestinoID(String destino_id);
    LinkedList<Cliente> clientesByReservaRngDate(String fecha_inicio, String fecha_fin);

    ///////////////////////////////


    LinkedList<Paquete> paquetesByNombre(String nombre);
    void reservasPorPaquetes();
    LinkedList<Cliente> clienteResvervasByClima(String clima);

    LinkedList<Reserva> resservaByClienteRngDate(String cliente_id, String fecha_inicio, String fecha_fin);
    LinkedList<Destino> destinosByPais(String pais);




    /////////////////////////////
    void destinosMasPopulares();
    LinkedList<Cliente> clientesByEmail(String mail);
    LinkedList<Paquete> paqueteByDestinoDuracion(String destino_id, int duracion);
    //Generar un índice compuesto para optimizar la búsqueda de reservas por
    //cliente, destino y estado de pago.
    LinkedList<Reserva> reservasByClienteDestinoDuracion(String cliente_id, String destino_id, int duracion);
}
