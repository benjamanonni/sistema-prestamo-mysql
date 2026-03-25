package DOMINIO;

import DATOS.TipoEstadoPrestamo;

import java.time.LocalDateTime;

public class Prestamo {
    private  String usuario;
    private  String recurso;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaVencimiento;
    private  LocalDateTime fechaDevolucion;//fecha cuando se devuelve
    private TipoEstadoPrestamo estadoPrestamo;
    private int id;
    //contructor al registrar prestamo sin id
    public Prestamo(String usuario, String recurso, LocalDateTime fechaInicio,
                    LocalDateTime fechaVencimiento, LocalDateTime fechaDevolucion,TipoEstadoPrestamo estado) {

        if (usuario == null) {
            throw new IllegalArgumentException("usuario vacio");
        }

        if (recurso== null) {
            throw new IllegalArgumentException("recurso vacio");
        }
        if(fechaInicio==null){
            throw new IllegalArgumentException("fecha vacia");
        }
        if(fechaVencimiento==null){
            throw new IllegalArgumentException("fecha vacia");
        }
        if(fechaInicio.isAfter(fechaVencimiento)){
            throw new IllegalArgumentException("fecha de inicio no puede estar despues de fecha de vencimiento");
        }

        //si todavia no se devuelve el prestamo esta activo
        if(fechaDevolucion==null){
            estadoPrestamo= TipoEstadoPrestamo.ACTIVO;
        }
        this.usuario=usuario;
        this.recurso=recurso;
        this.fechaInicio=fechaInicio;
        this.fechaVencimiento=fechaVencimiento;
        this.fechaDevolucion=fechaDevolucion;
        //ya no es valido esto ya que ahora que llamamos al contructor no siempre es activo
        this.estadoPrestamo=estado;
    }
    //contructor al listar prestamos
    public Prestamo(String usuario, String recurso, LocalDateTime fechaInicio,
                    LocalDateTime fechaVencimiento, LocalDateTime fechaDevolucion, int id, TipoEstadoPrestamo estado) {

        if (usuario == null) {
            throw new IllegalArgumentException("usuario vacio");
        }

        if (recurso== null) {
            throw new IllegalArgumentException("recurso vacio");
        }
        if(fechaInicio==null){
            throw new IllegalArgumentException("fecha vacia");
        }
        if(fechaVencimiento==null){
            throw new IllegalArgumentException("fecha vacia");
        }
        if(fechaInicio.isAfter(fechaVencimiento)){
            throw new IllegalArgumentException("fecha de inicio no puede estar despues de fecha de vencimiento");
        }

        //si todavia no se devuelve el prestamo esta activo
        if(fechaDevolucion==null){
            estadoPrestamo= TipoEstadoPrestamo.ACTIVO;
        }
        this.id=id;
        this.usuario=usuario;
        this.recurso=recurso;
        this.fechaInicio=fechaInicio;
        this.fechaVencimiento=fechaVencimiento;
        this.fechaDevolucion=fechaDevolucion;
        //ya no es valido esto ya que ahora que llamamos al contructor no siempre es activo
        this.estadoPrestamo=estado;
    }
    //getter
    public String getLegajoUsuario(){
        return usuario;
    }
    public String getCodigoRecurso(){
        return recurso;
    }
    public LocalDateTime getFechaInicio(){
        return fechaInicio;
    }
    public LocalDateTime getFechaVencimiento(){
        return fechaVencimiento;
    }
    public LocalDateTime getFechaDevolucion(){
        return fechaDevolucion;
    }
    public TipoEstadoPrestamo getEstadoPrestamo(){
        return estadoPrestamo;
    }

    public int getId() {
        return id;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id="+ id+
                ", usuario=" + usuario+
                ", recurso=" + recurso+
                ", fechaInicio=" + fechaInicio +
                ", fechaVencimiento=" + fechaVencimiento +
                ", fechaDevolucion=" + fechaDevolucion +
                ", estadoPrestamo='" + estadoPrestamo + '\'' +
                '}';
    }
}
