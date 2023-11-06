package CasandraSinf;

import java.util.Objects;

public class Cliente {
    private String cliente_id;
    private String nombre;
    private String correo_electronico;
    private String telefono;

    public Cliente( ) {
        this.cliente_id = "";
        this.nombre = "";
        this.correo_electronico = "";
        this.telefono = "";
    }

    public Cliente(String cliente_id, String nombre, String correo_electronico, String telefono) {
        this.cliente_id = cliente_id;
        this.nombre = nombre;
        this.correo_electronico = correo_electronico;
        this.telefono = telefono;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente cliente)) return false;
        return Objects.equals(getCliente_id(), cliente.getCliente_id()) && Objects.equals(getNombre(), cliente.getNombre()) && Objects.equals(getCorreo_electronico(), cliente.getCorreo_electronico()) && Objects.equals(getTelefono(), cliente.getTelefono());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCliente_id(), getNombre(), getCorreo_electronico(), getTelefono());
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cliente_id='" + cliente_id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo_electronico='" + correo_electronico + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
