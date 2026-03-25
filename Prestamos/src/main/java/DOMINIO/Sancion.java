package DOMINIO;

import java.time.LocalDateTime;

public class Sancion {
     private  int idSancion;
     private  int prestamoid;
     //lo ideal es tener usuario para no generar subconsultas para obtener el usuario
     private String usuariolegajo;
     private final long diaAtraso;
     private final LocalDateTime fechaInicioBloqueo;
     private final LocalDateTime fechaFinBloqueo;
     //contructor para registrar sancion
    public Sancion(int prestamoid, String usuariolegajo, long diaAtraso, LocalDateTime fechaInicioBloqueo, LocalDateTime fechaFinBloqueo){
        //validaciones
        if(diaAtraso<0){
            throw new IllegalArgumentException("dia de atraso no puede ser menor a 0");
        }
        if(fechaInicioBloqueo==null){
            throw new IllegalArgumentException("fecha vacia");
        }
        if(fechaFinBloqueo==null){
            throw new IllegalArgumentException("fecha vacia");
        }
        if(fechaInicioBloqueo.isAfter(fechaFinBloqueo)){
            throw new IllegalArgumentException("no puede ir antes la fecha de inicio de bloqueo que la de fin");
        }
        this.prestamoid = prestamoid;
        this.usuariolegajo=usuariolegajo;
        this.diaAtraso=diaAtraso;
        this.fechaInicioBloqueo=fechaInicioBloqueo;
        this.fechaFinBloqueo=fechaFinBloqueo;

    }
     public Sancion(int prestamoid, String usuariolegajo, long diaAtraso, LocalDateTime fechaInicioBloqueo, LocalDateTime fechaFinBloqueo, int id){
         //validaciones
         if(diaAtraso<0){
             throw new IllegalArgumentException("dia de atraso no puede ser menor a 0");
         }
         if(fechaInicioBloqueo==null){
             throw new IllegalArgumentException("fecha vacia");
         }
         if(fechaFinBloqueo==null){
             throw new IllegalArgumentException("fecha vacia");
         }
         if(fechaInicioBloqueo.isAfter(fechaFinBloqueo)){
             throw new IllegalArgumentException("no puede ir antes la fecha de inicio de bloqueo que la de fin");
         }

         this.idSancion=id;
         this.prestamoid = prestamoid;
         this.usuariolegajo=usuariolegajo;
         this.diaAtraso=diaAtraso;
         this.fechaInicioBloqueo=fechaInicioBloqueo;
         this.fechaFinBloqueo=fechaFinBloqueo;

     }
//getter

    public int getPrestamoid() {
        return prestamoid;
    }
    public String getUsuariolegajo(){
         return usuariolegajo;
    }

    public int getIdSancion() {
        return idSancion;
    }

    public long getDiaAtraso() {
        return diaAtraso;
    }

    public LocalDateTime getFechaInicioBloqueo() {
        return fechaInicioBloqueo;
    }

    public LocalDateTime getFechaFinBloqueo() {
        return fechaFinBloqueo;
    }

    public void setIdSancion(int idSancion) {
        this.idSancion = idSancion;
    }

    @Override
    public String toString() {
        return "Sancion{" +
                "prestamo=" + prestamoid +
                ", dias de Atraso =" + diaAtraso +
                ", fechaInicioBloqueo=" + fechaInicioBloqueo +
                ", fechaFinBloqueo=" + fechaFinBloqueo +
                ", usuario legajo=" + usuariolegajo +
                '}';
    }
}
