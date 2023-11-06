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
}
