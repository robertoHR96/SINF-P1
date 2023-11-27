package CasandraSinf;

import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        //Cassandra cs = new Cassandra();
        Mongo cs = new Mongo();
        try {
            cs.connect("127.0.0.1");


            cs.crearDataBase();

            /*
            System.out.println("---- Lista de destinos ----");
            LinkedList<Destino> lisDestinos = cs.todosLosDestinos();
            Iterator it1 = lisDestinos.iterator();
            while(it1.hasNext()){
                System.out.println(it1.next().toString());
            }
            System.out.println();
            System.out.println();

            System.out.println("---- Lista reservas por id cliente ----");
            LinkedList<Reserva> listaReservasByClienteId = cs.reservasByClienteID("b6354ef6-0fd4-491d-8211-389af2cebb80");
            Iterator et = listaReservasByClienteId.iterator();
            while (et.hasNext()){
                System.out.println(et.next().toString());
            }
            System.out.println();
            System.out.println();

            System.out.println("---- Lista paquetes para un destino particular ----");
            LinkedList<Paquete> listaReservasByClienteId0 = cs.paquetesByDestinoID("03770ec7-877f-4b61-b188-3e294ac58af7");
            Iterator et2 = listaReservasByClienteId0.iterator();
            while (et2.hasNext()){
                System.out.println(et2.next().toString());
            }
            System.out.println();
            System.out.println();

            System.out.println("---- Lista clientes para reservar en intervalo fechas ----");
            LinkedList <Cliente> listaClientes0 = cs.clientesByReservaRngDate("2023-01-01","2024-11-22");
            Iterator et3 = listaClientes0.iterator();
            while (et3.hasNext()){
                System.out.println(et3.next().toString());
            }
            System.out.println();
            System.out.println();

            System.out.println("---- Lista paquetes por nombrek ----");
            LinkedList<Paquete> listaReservasByClienteId1 = cs.paquetesByNombre("Destino 2");

            Iterator et4 = listaReservasByClienteId1.iterator();
            while (et4.hasNext()){
                System.out.println(et4.next().toString());
            }
            System.out.println();
            System.out.println();

            System.out.println("---- cleintes por destino clima ---");
            LinkedList <Cliente> listaClientes1 = cs.clienteResvervasByClima("Malo");
            Iterator et5 = listaClientes1.iterator();
            while (et5.hasNext()){
                System.out.println(et5.next().toString());
            }
            System.out.println();
            System.out.println();

            System.out.println("---- Destinos por pais ----");
            LinkedList<Destino> listaDestinos = cs.destinosByPais("España");
            Iterator et6 = listaDestinos.iterator();
            while(et6.hasNext()){
                System.out.println(et6.next().toString());
            }
            System.out.println();
            System.out.println();


            System.out.println("---- Clientes por mail ----");
            LinkedList<Cliente> listaClientes = cs.clientesByEmail("roberto@gmail.com");
            Iterator et7 = listaClientes.iterator();
            while(et7.hasNext()){
                System.out.println(et7.next().toString());
            }

            */

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Error de conexion");
        }finally {
            cs.close();
        }
    }
}