package CasandraSinf;

import java.util.Objects;

public class Destino {
    private String destino_id;
    private String nombre;
    private String pais;
    private String descripcion;
    private String clima;

    public Destino() {
        this.destino_id = "";
        this.nombre = "";
        this.pais = "";
        this.descripcion = "";
        this.clima = "";
    }
    public Destino(String destino_id, String nombre, String pais, String descripcion, String clima) {
        this.destino_id = destino_id;
        this.nombre = nombre;
        this.pais = pais;
        this.descripcion = descripcion;
        this.clima = clima;
    }


    public String getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(String destino_id) {
        this.destino_id = destino_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Destino destino)) return false;
        return Objects.equals(getDestino_id(), destino.getDestino_id()) && Objects.equals(getNombre(), destino.getNombre()) && Objects.equals(getPais(), destino.getPais()) && Objects.equals(getDescripcion(), destino.getDescripcion()) && Objects.equals(getClima(), destino.getClima());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDestino_id(), getNombre(), getPais(), getDescripcion(), getClima());
    }

    @Override
    public String toString() {
        return "Destino{" +
                "destino_id='" + destino_id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", pais='" + pais + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", clima='" + clima + '\'' +
                '}';
    }
}