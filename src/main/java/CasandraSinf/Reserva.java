package CasandraSinf;

import java.util.Objects;

public class Reserva {
    private String reserva_id;
    private String paquete_id;
    private String cliente_id;
    private String fecha_inicio;
    private String fecha_fin;
    private boolean pagado;

    public Reserva() {
        this.reserva_id = "";
        this.paquete_id = "";
        this.cliente_id = "";
        this.fecha_inicio = "";
        this.fecha_fin = "";
        this.pagado = false;
    }
    public Reserva(String reserva_id,  String cliente_id, String fecha_inicio, String fecha_fin, boolean pagado, String paquete_id) {
        this.reserva_id = reserva_id;
        this.paquete_id = paquete_id;
        this.cliente_id = cliente_id;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.pagado = pagado;
    }

    public String getReserva_id() {
        return reserva_id;
    }

    public void setReserva_id(String reserva_id) {
        this.reserva_id = reserva_id;
    }

    public String getPaquete_id() {
        return paquete_id;
    }

    public void setPaquete_id(String paquete_id) {
        this.paquete_id = paquete_id;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva reserva)) return false;
        return isPagado() == reserva.isPagado() && Objects.equals(getReserva_id(), reserva.getReserva_id()) && Objects.equals(getPaquete_id(), reserva.getPaquete_id()) && Objects.equals(getCliente_id(), reserva.getCliente_id()) && Objects.equals(getFecha_inicio(), reserva.getFecha_inicio()) && Objects.equals(getFecha_fin(), reserva.getFecha_fin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReserva_id(), getPaquete_id(), getCliente_id(), getFecha_inicio(), getFecha_fin(), isPagado());
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "reserva_id='" + reserva_id + '\'' +
                ", paquete_id='" + paquete_id + '\'' +
                ", cliente_id='" + cliente_id + '\'' +
                ", fecha_inicio='" + fecha_inicio + '\'' +
                ", fecha_fin='" + fecha_fin + '\'' +
                ", pagado=" + pagado +
                '}';
    }
}
