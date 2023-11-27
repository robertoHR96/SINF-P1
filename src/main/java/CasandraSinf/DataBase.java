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
    LinkedList<Cliente> clienteResvervasByClima(String clima);
    LinkedList<Destino> destinosByPais(String pais);


    /////////////////////////////
    LinkedList<Cliente> clientesByEmail(String mail);
    LinkedList<Paquete> paqueteByDestinoDuracion(String destino_id, String duracion);
}
