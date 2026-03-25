package DOMINIO;

import DATOS.TipoEstadoRecurso;
import DATOS.TipoRecurso;

import java.time.LocalDateTime;

public class Recurso {
    private final String nombre;
    private final String codigo;
    private final TipoRecurso tipoRecurso;
    private TipoEstadoRecurso estado;
    private final LocalDateTime fechaAlta;

    public Recurso(String nombre, String codigo, TipoRecurso tipoRecurso, TipoEstadoRecurso estado, LocalDateTime fechaAlta){
        //hacer validaciones en el contructor
        if(nombre==null||nombre.isBlank()){
            throw new IllegalArgumentException("nombre vacio");
        }
        if(codigo==null||codigo.isBlank()){
            throw new IllegalArgumentException("codigo vacio");
        }
        if(tipoRecurso==null){
            throw new IllegalArgumentException("tipo recurso vacio");
        }
        if(estado==null){
            throw new IllegalArgumentException("estado vacio");
        }
        if(fechaAlta==null) {
            throw new IllegalArgumentException("fecha vacia");
        }

        this.nombre = nombre;
        this.codigo=codigo;
        this.tipoRecurso=tipoRecurso;
        this.estado=estado;
        this.fechaAlta= fechaAlta;
    }
    //getter
    public String getNombre(){
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoRecurso getTipoRecurso(){
        return tipoRecurso;
    }
    public TipoEstadoRecurso getEstado(){
        return estado;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    @Override
    public String toString() {
        return "Recurso{" +
                "nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", tipoRecurso='" + tipoRecurso + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaAlta=" + fechaAlta +
                '}';
    }
}
