package CasandraSinf;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static DataBase cs;

    public static void main(String[] args) {
        //Cassandra cs = new Cassandra();
        if(elegirDB()){
            cs = new Mongo();
        }else{
            cs = new Cassandra();
        }
        try {
            cs.connect("127.0.0.1");
            if(crearDB()) {
                cs.crearDataBase();
            }
            menuOpciones();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Error de conexion");
        } finally {
            cs.close();
        }

    }

    public static boolean menuOpciones() {
        while (true) {
            System.out.println("---------------------------------------------");
            System.out.println("--------------Menu de opciones---------------");
            System.out.println("------ Consultas tipo 1 -------");
            System.out.println("1. Obtener todos los destinos disponibles en la agencia de viajes.");
            System.out.println("2. Obtener los detalles de un paquete turístico específico a través de su ID.");
            System.out.println("3. Obtener todas las reservas realizadas por un cliente específico.");
            System.out.println("4. Obtener todos los paquetes turísticos disponibles para un destino en particular.");
            System.out.println("5. Obtener todos los clientes que han realizado reservas en un rango de fechas específico.");
            System.out.println("------ Consultas tipo 2 -------");
            System.out.println("6. Generar un índice para acelerar la búsqueda de paquetes por nombre.");
            System.out.println("7. Crear una tabla de resumen para almacenar el número total de reservas realizadas por cada paquete.");
            System.out.println("8. Obtener todos los clientes que han realizado reservas en destinos con un clima específico.");
            System.out.println("9. Generar un índice compuesto para optimizar la búsqueda de reservas de un cliente en un rango de fechas determinado.");
            System.out.println("10. Crear una tabla de índice para acelerar la búsqueda de destinos por país.");
            System.out.println("------ Consultas tipo 3 -------");
            System.out.println("11. Obtener todos los destinos populares (los más reservados) en orden descendente según el número de reservas.");
            System.out.println("12. Generar un índice para acelerar la búsqueda de clientes por correo electrónico.");
            System.out.println("13. Crear una tabla de índice para almacenar información sobre la disponibilidad de paquetes por destino y fecha.");
            System.out.println("14. Obtener todos los paquetes turísticos disponibles para un destino específico y con una duración determinada.");
            System.out.println("15. Generar un índice compuesto para optimizar la búsqueda de reservas por cliente, destino y estado de pago");
            System.out.println("16. Salir del programa");
            try {
                Scanner sc = new Scanner(System.in);
                System.out.print("Selecione una opcion: ");
                String seleccion = sc.nextLine();
                Integer valor = Integer.parseInt(seleccion);
                if (valor < 0 || valor > 16) {
                    System.out.println("Valor de la seleccion no valido");
                } else {
                    switch (valor) {
                        case 1:
                            opcion1();
                            break;
                        case 2:
                            opcion2();
                            break;
                        case 3:
                            opcion3();
                            break;
                        case 4:
                            opcion4();
                            break;
                        case 5:
                            opcion5();
                            break;
                        case 6:
                            opcion6();
                            break;
                        case 7:
                            opcion7();
                            break;
                        case 8:
                            opcion8();
                            break;
                        case 9:
                            opcion9();
                            break;
                        case 10:
                            opcion10();
                            break;
                        case 11:
                            opcion11();
                            break;
                        case 12:
                            opcion12();
                            break;
                        case 13:
                            opcion13();
                            break;
                        case 14:
                            opcion14();
                            break;
                        case 15:
                            opcion15();
                            break;
                        case 16:
                            return false;
                    }
                    System.out.println("Pulse enter para continuar");
                    seleccion = sc.nextLine();
                }

            } catch (Exception e) {
                System.out.println("Valor de la seleccion no valido");
            }
        }
    }


    public static void opcion1() {
        System.out.println("---- Lista de destinos ----");
        LinkedList<Destino> lisDestinos = cs.todosLosDestinos();
        Iterator it1 = lisDestinos.iterator();
        while (it1.hasNext()) {
            System.out.println(it1.next().toString());
        }
        System.out.println();
        System.out.println();
    }

    public static void opcion2() {
        System.out.println("---- Paquete by Id----");
        System.out.print("Selecione el ID del paquete: ");
        try {
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            Paquete paq = cs.paqueteByID(seleccion);
            System.out.println(paq.toString());
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error ID paquete");
        }
    }

    public static void opcion3() {

        System.out.println("---- Lista reservas por id cliente ----");
        try {
            System.out.print("Selecione el ID del cliente");
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            LinkedList<Reserva> listaReservasByClienteId = cs.reservasByClienteID(seleccion);
            Iterator et = listaReservasByClienteId.iterator();
            while (et.hasNext()) {
                System.out.println(et.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion4() {
        try {
            System.out.println("---- Lista paquetes para un destino particular ----");
            System.out.print("Seleccione el ID del destino: ");
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            LinkedList<Paquete> listaReservasByClienteId0 = cs.paquetesByDestinoID(seleccion);
            Iterator et2 = listaReservasByClienteId0.iterator();
            while (et2.hasNext()) {
                System.out.println(et2.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion5() {
        try {
            System.out.println("---- Lista clientes para reservar en intervalo fechas ----");
            System.out.print("Selecione el intervalo de fechas (Formato: 2021-09-14T07:21:02.641Z)");
            Scanner sc = new Scanner(System.in);
            System.out.print("Fecha inicio: ");
            System.out.println();
            String seleccion = sc.nextLine();
            System.out.print("Fecha inicio: ");
            String seleccion2 = sc.nextLine();
            System.out.println();
            LinkedList<Cliente> listaClientes0 = cs.clientesByReservaRngDate(seleccion, seleccion2);
            Iterator et3 = listaClientes0.iterator();
            while (et3.hasNext()) {
                System.out.println(et3.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion6() {
        try {
            System.out.println("---- Lista paquetes por nombre ----");
            System.out.print("Seleccione el nombre del paquete: ");
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            LinkedList<Paquete> listaReservasByClienteId1 = cs.paquetesByNombre(seleccion);
            Iterator et4 = listaReservasByClienteId1.iterator();
            while (et4.hasNext()) {
                System.out.println(et4.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion7() {
        try {

        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion8() {
        try {
            System.out.println("---- cleintes por destino clima ---");
            System.out.print("Seleccione el tipo de clima: ");
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            LinkedList<Cliente> listaClientes1 = cs.clienteResvervasByClima(seleccion);
            Iterator et5 = listaClientes1.iterator();
            while (et5.hasNext()) {
                System.out.println(et5.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion9() {
        try {
            System.out.println("---- Reserva cliente ragno fechas ----");
            System.out.print("Selecione el intervalo de fechas (Formato: 2021-09-14T07:21:02.641Z)");
            Scanner sc = new Scanner(System.in);
            System.out.print("Fecha inicio: ");
            System.out.println();
            String seleccion = sc.nextLine();
            System.out.print("Fecha fin: ");
            String seleccion2 = sc.nextLine();
            System.out.print("Seleciene el id del cliente: ");
            String seleccion3 = sc.nextLine();
            System.out.println();
            LinkedList<Reserva> listaReservas = cs.resservaByClienteRngDate(seleccion3, seleccion, seleccion2);
            Iterator it = listaReservas.iterator();
            while (it.hasNext()){
                Reserva rsv = (Reserva) it.next();
                System.out.println(rsv.toString());
            }
            //resservaByClienteRngDate
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion10() {
        try {
            System.out.println("---- Destinos por pais ----");
            System.out.print("Seleccione el pais: ");
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            LinkedList<Destino> listaDestinos = cs.destinosByPais(seleccion);
            Iterator et6 = listaDestinos.iterator();
            while (et6.hasNext()) {
                System.out.println(et6.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion11() {
        try {

        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion12() {
        try {
            System.out.println("---- Clientes por mail ----");
            System.out.print("Seleccione el mail del cliente: ");
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            LinkedList<Cliente> listaClientes = cs.clientesByEmail(seleccion);
            Iterator et7 = listaClientes.iterator();
            while (et7.hasNext()) {
                System.out.println(et7.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion13() {
        try {

        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion14() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("---- paquetes destino duracion ----");
            System.out.print("Selecione el ID del destino: ");
            String seleccion = sc.nextLine();
            System.out.print("Selecione la duración del destino: ");
            String seleccion2 = sc.nextLine();
            Integer duracion = Integer.parseInt(seleccion2);
            LinkedList<Paquete> listaPaquetes = cs.paqueteByDestinoDuracion(seleccion, duracion);
            Iterator et8 = listaPaquetes.iterator();
            while (et8.hasNext()) {
                System.out.println(et8.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static void opcion15() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("---- reservas cliente destino duracion ----");
            System.out.print("Selecione el ID del cliente: ");
            String cliente = sc.nextLine();
            System.out.print("Selecione el ID del destino: ");
            String seleccion = sc.nextLine();
            System.out.print("Selecione la duración del destino: ");
            String seleccion2 = sc.nextLine();
            Integer duracion = Integer.parseInt(seleccion2);
            LinkedList<Reserva> listaReservas2 = cs.reservasByClienteDestinoDuracion(cliente, seleccion, duracion);
            Iterator et9 = listaReservas2.iterator();
            while (et9.hasNext()) {
                System.out.println(et9.next().toString());
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private static boolean crearDB() {
        while (true) {
            System.out.println("¿Desea crear y rellenar la BD s/n?");
            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            if (seleccion.equals("s") || seleccion.equals("S")) {
                return true;
            }
            if (seleccion.equals("n") || seleccion.equals("N")) {
                return false;
            }
            limpiar();
        }
    }

    private static boolean elegirDB() {
        while (true) {
            System.out.println("Eliga entre \n1. mongoDB \n2. Cassandra");

            Scanner sc = new Scanner(System.in);
            String seleccion = sc.nextLine();
            if (seleccion.equals("1")) {
                return true;
            }
            if (seleccion.equals("2")) {
                return false;
            }

            limpiar();
        }
    }

    private static void limpiar() {
        try {
            String operatingSystem = System.getProperty("os.name"); // Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}