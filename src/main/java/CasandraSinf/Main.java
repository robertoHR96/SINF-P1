package CasandraSinf;

import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        Cassandra cs = new Cassandra();
        try {
            cs.connect("127.0.0.1");
            /*
            LinkedList<Destino> destinos = cs.todosLosDestinos();
            //System.out.println("destino_id nombre pais descripcion cliema");
            Iterator it = destinos.iterator();
            while(it.hasNext()){
                Destino destino = (Destino) it.next();
                //System.out.println( destino.getDestino_id()+" "+destino.getNombre()+" "+destino.getPais()+" "+destino.getDescripcion()+" "+destino.getClima());
                System.out.println(destino.toString());
            }
             */
            System.out.println(cs.paqueteByID("97f73a7f-cf66-4416-a0b3-f14b6ca8cd72").toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Error de conexion");
        }finally {
            cs.close();
        }
    }
}