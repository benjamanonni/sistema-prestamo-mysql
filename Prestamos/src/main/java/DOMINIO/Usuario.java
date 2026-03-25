package DOMINIO;

import DATOS.TipoUsuario;

import java.time.LocalDateTime;

public class Usuario {
    private final String  legajo;
    private final String nombreCompleto;
    private final TipoUsuario tipoUsuario;
    private final String correo;
    private LocalDateTime bloqueadoHasta;

    //contructor
    public Usuario(String legajo, String nombreCompleto, TipoUsuario tipoUsuario, String correo, LocalDateTime bloqueadoHasta){

        if (legajo == null || legajo.isBlank()) {
            throw new IllegalArgumentException("Legajo obligatorio");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("Nombre obligatorio");
        }
        if (tipoUsuario == null) {
            throw new IllegalArgumentException("Tipo de usuario obligatorio");
        }
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("Correo obligatorio");
        }
        this.legajo = legajo;
        this.nombreCompleto=nombreCompleto;
        this.tipoUsuario=tipoUsuario;
        this.correo = correo;
        this.bloqueadoHasta = bloqueadoHasta;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public LocalDateTime getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    //getter
    public TipoUsuario getTipoUsuario(){
        return tipoUsuario;
    }

    public String getCorreo(){
        return correo;
    }

    public String getLegajo() {
        return legajo;
    }

    public boolean getbloqueado(){
        //si existe fecha de bloqueado y todavia falta para que llegue a esa fecha
        if(bloqueadoHasta!=null && bloqueadoHasta.isAfter(LocalDateTime.now())){
            return true;//esta bloqueado
        }
        return false;//no esta bloqueado
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "nombreCompleto='" + nombreCompleto + '\'' +
                "legajo= " + legajo + '\'' +
                "tipo de usuario= " + tipoUsuario + '\'' +
                "correo=" + correo +
                ", bloqueado hasta=" + bloqueadoHasta +
                '}';
    }
}
