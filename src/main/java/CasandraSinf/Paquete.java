package CasandraSinf;

import java.math.BigDecimal;
import java.util.Objects;

public class Paquete {
    private String paquete_id;
    private String nombre;
    private String destino_id;
    private Integer duracion;
    private BigDecimal precio;


    public Paquete() {
        this.paquete_id = "";
        this.nombre = "";
        this.destino_id = "";
        this.duracion = 0;
        this.precio = new BigDecimal(0);
    }
    public Paquete(String paquete_id, String destino_id, Integer duracion, String nombre, BigDecimal precio) {
        this.paquete_id = paquete_id;
        this.nombre = nombre;
        this.destino_id = destino_id;
        this.duracion = duracion;
        this.precio = precio;
    }

    public String getPaquete_id() {
        return paquete_id;
    }

    public void setPaquete_id(String paquete_id) {
        this.paquete_id = paquete_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(String destino_id) {
        this.destino_id = destino_id;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete paquete)) return false;
        return Objects.equals(getPaquete_id(), paquete.getPaquete_id()) && Objects.equals(getNombre(), paquete.getNombre()) && Objects.equals(getDestino_id(), paquete.getDestino_id()) && Objects.equals(getDuracion(), paquete.getDuracion()) && Objects.equals(getPrecio(), paquete.getPrecio());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPaquete_id(), getNombre(), getDestino_id(), getDuracion(), getPrecio());
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "paquete_id='" + paquete_id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", destino_id='" + destino_id + '\'' +
                ", duracion=" + duracion +
                ", precio=" + precio +
                '}';
    }
}
