package SISTEMA;


import DATOS.*;
import DOMINIO.Prestamo;
import DOMINIO.Recurso;
import DOMINIO.Sancion;
import DOMINIO.Usuario;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

//el encargado de manejar los otros singulares
public class SistemaPrestamo {
    //Separación real de responsabilidades
    private final UsuarioDAO usuarioDAO;
    private final RecursoDAO recursoDAO ;
    private final PrestamoDAO prestamoDAO;
    private final SancionDAO sancionDAO;
    //creamos contructor
    public SistemaPrestamo(UsuarioDAO usuarioDAO, RecursoDAO recursoDAO, PrestamoDAO prestamoDAO, SancionDAO sancionDAO){
        this.usuarioDAO=usuarioDAO;
        this.recursoDAO=recursoDAO;
        this.prestamoDAO=prestamoDAO;
        this.sancionDAO=sancionDAO;
    }

    //-----------------USUARIO------------
    //1-registrar nuevo usuario *REVISADO
    public void registrarNuevoUsuario(String legajo, String nombreCompleto, TipoUsuario tipoUsuario, String correo) {
        Usuario usuario= new Usuario(legajo,nombreCompleto,tipoUsuario,correo,null);
        usuarioDAO.registrarUsuario(usuario);
    }

    //2-listar usuarios *REVISADO
    public void listarUsuario() {
        ArrayList<Usuario> lista = usuarioDAO.listarUsuarios();
        //1-primero para no entrar en un for inecesario
        if(lista.isEmpty()){
            System.out.println("no hay usuarios registrados");
        }
        for (Usuario u : lista) {
            System.out.println(u);
        }
    }
    //3-listar usuario por legajo *REVISADO
    public void listarUsuarioLegajo(String legajo) {
        if (legajo == null|| legajo.isBlank()) {
            throw new IllegalArgumentException("legajo de parametro vacio");
        }
        Usuario u = usuarioDAO.buscarPorLegajo(legajo);
        if (u == null) {
            throw new IllegalStateException("no se encontro usuario con legajo " + legajo);
        }
        System.out.println(u);
    }

    //-------------RECURSO----------------
//1-registrar nuevo recurso *REVISADO
    public void registrarNuevoRecurso(String nombre, String codigo, TipoRecurso tipoRecurso){
        Recurso r = new Recurso(nombre, codigo,tipoRecurso,TipoEstadoRecurso.DISPONIBLE, LocalDateTime.now());
        recursoDAO.registrarNuevoRecurso(r);
    }

    //2-lista recurso por estado *REVISADO
    public void listarRecursosEstado(TipoEstadoRecurso estado) {
        if (estado == null) {
            throw  new IllegalArgumentException("estado de parametro vacio");
        }
        HashMap<String, Recurso> recursos = recursoDAO.listarRecursoEstado(estado);
        if (recursos.isEmpty()) {
            throw new IllegalStateException("no existen recurso con estado " + estado);
        }
        for (Recurso r : recursos.values()) {
                System.out.println(r);
        }
    }

    //3-consultar recurso por codigo *REVISADO
    public void listarRecursoCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("codigo vacio error");
        }
        Recurso recurso = recursoDAO.obtenerRecursoPorCodigo(codigo);
        if (recurso==null) {
            throw new IllegalStateException("no se encontro recurso con codigo " + codigo);
        }
        System.out.println(recurso);
    }

    //4-consultar recursos por tipo *REVISADO
    public void listarRecursosTipo(TipoRecurso tipoRecurso) {
        if (tipoRecurso == null) {
            throw new IllegalArgumentException("tipo recurso vacio");
        }
        ArrayList<Recurso> recurso = recursoDAO.obtenerlistadeRecursoTipo(tipoRecurso);
        if (recurso == null) {
            throw new IllegalStateException("no se encontraron recurso del tipo: " + tipoRecurso);
        }
        for (Recurso r : recurso) {
            System.out.println(r);
        }
    }

    //5-cambiar estado del recurso *REVISADO
    public void cambiarEstadoRecurso(TipoEstadoRecurso tipoestado, String codigo) {
        //verificacion
        if (tipoestado == null || codigo == null || codigo.isBlank()) {
           throw  new IllegalArgumentException("parametros vacios");
        }
        //traemos el objeto para verificarlo aca
        Recurso r = recursoDAO.obtenerRecursoPorCodigo(codigo);
        //verificar que exista el recurso
        if(r==null){
            throw new IllegalArgumentException("recurso con codigo: "+ codigo + " no existe");
        }
        if (r.getEstado() == TipoEstadoRecurso.PRESTADO) {
            throw new IllegalStateException("no se puede modificar el estado de un recurso que esta prestado");
        }
        if (r.getEstado() == TipoEstadoRecurso.DADOBAJA) {
            throw new IllegalStateException("no se puede modificar el estado de un recurso que esta dado de baja");
        }
        //no podemos poner un estado en prestado sin que haya prestamo
        if (tipoestado == TipoEstadoRecurso.PRESTADO) {
           throw new IllegalStateException("no se puede poner como prestado sin que haya prestamo");
        }
        //controlar de que no se pueda cambiar al mismo estado que ya tiene
        if (r.getEstado() == tipoestado) {
            throw new IllegalStateException("estado repetido este ya lo posee");
        }
        recursoDAO.cambiarEstadoRecurso(codigo, tipoestado);
    }

    //6-listar recursos activos *REVISADO
    public void listarRecursosDisponibles() {
        HashMap<String, Recurso> recursos = recursoDAO.listarRecursoActivo();
        if (recursos.isEmpty()) {
            throw new IllegalArgumentException("no hay recursos disponibles");
        }
        for (Recurso r : recursos.values()) {
                System.out.println(r);
        }
    }

    //7-buscar recurso por nombre o texto parcial *REVISADO
    public void listarRecursosNombre(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("descripcion invalida");
        }
        HashMap<String, Recurso> recursos = recursoDAO.listarRecursosPorNombre(descripcion);
        if (recursos.isEmpty()) {
            throw new IllegalArgumentException("no hay recursos que contengan esa descripcion");
        }
        for (Recurso r : recursos.values()) {
            System.out.println(r);
        }
    }

    //------------------PRESTAMO----------------- vive en sistemaPrestamo porque es el que maneja la globalidad de las otras clases


    //1,1-Verificacion de usuario de nuevo prestamo *REVISADO
    public Usuario devolverUsuarioVerificado(String legajoUsuario) {
        //que parametro no este vacio
        if (legajoUsuario == null||legajoUsuario.isBlank()) {
           throw new IllegalArgumentException("el parametro legajoUsuario no puede estar vacio");
        }
        Usuario u = usuarioDAO.buscarPorLegajo(legajoUsuario);
        //si u esta vacio
        if (u == null) {
            throw  new IllegalArgumentException("usuario vacio,no se encontro legajo");
        }
        //usuario bloqueado
        if (u.getbloqueado()) {
            throw new IllegalStateException("usuario bloqueado no puede hacer prestamos");
        }
        //cumple limite por usuario,llamamos a funcion
        if (!cumpleLimitesPorUsuario(u)) {
            throw new IllegalStateException("se alcanzo el limite por usuario");
        }
        return u;
    }

    //1.2-Verificacion de recurso para nuevo prestamo *REVISADO
    public Recurso devolverRecursoVerificado(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("codigo vacio de parametro");
        }
        Recurso r = recursoDAO.obtenerRecursoPorCodigo(codigo);
        //si no se encuentra legajo
        if (r == null) {
            throw new IllegalArgumentException("recurso vacio,no se encontro codigo");
        }
        //solo se puede prestar un recurso disponible
        if (r.getEstado() != TipoEstadoRecurso.DISPONIBLE) {
            throw new IllegalStateException("no se puede prestar un recurso que no este disponible");
        }
        // revisamos que el recurso no haya sido prestado es una revision extra
        if (!verificarEstadoRecurso(codigo)) {
            throw new IllegalArgumentException("el recurso ha sido prestado,no se puede volver a prestar");
        }
        return r;
    }

    //1-Registrar nuevo prestamo *REVISADO
    public void registrarNuevoPrestamo(String legajoUsuario, String codigoRecurso, LocalDateTime fechaInicioPrestamo,
                                          LocalDateTime fechaVencimientoPrestamo) {

        Prestamo prestamo=new Prestamo(legajoUsuario,codigoRecurso,fechaInicioPrestamo,fechaVencimientoPrestamo,null
                ,TipoEstadoPrestamo.ACTIVO);

        //actualizamos estados de prestamos
        pasarPrestamosaVencidos();
        Usuario usuario = devolverUsuarioVerificado(legajoUsuario);
        Recurso recurso = devolverRecursoVerificado(codigoRecurso);
        if (usuario == null)throw new IllegalArgumentException("usuario vacio");
        if (recurso == null) {
            throw new IllegalArgumentException("recurso vacio");
        }
        //validar recursos restringidos
        if (!validarRecursosRestringidos(legajoUsuario, codigoRecurso)) {
            throw new IllegalStateException("recurso restringido");
        }

        //añadimos prestamo
        prestamoDAO.registrarNuevoPrestamo(prestamo);
        //cambiar estado del recurso
        recursoDAO.cambiarEstadoRecursoPrestado(codigoRecurso);
    }

    /*1.1-validar cantidad de prestamos por alumnos y docentes aca se cuenta activo y vencido
    aca teniamos un error manejabamos 2 contador y 2 loops esto lo podes simplificar
    REVISADO
*/
    private boolean cumpleLimitesPorUsuario(Usuario usuario) {
        ArrayList<Prestamo> prestamos = prestamoDAO.listarPrestamos();
        if (usuario == null) throw new IllegalArgumentException("usuario vacio");
        int contador = 0;
        for (Prestamo p : prestamos) {
            //sincronizamos usuario con prestamo
            if (!p.getLegajoUsuario().equals(usuario.getLegajo())) {
                continue;//si es diferente pasamos al siguiente indice del for
            }
            //usamos contador
            if (p.getEstadoPrestamo() == TipoEstadoPrestamo.ACTIVO || p.getEstadoPrestamo() == TipoEstadoPrestamo.VENCIDO) {
                contador++;
            }
            //validadores dentro del for ya que queremos que se corte antes del limite
            if (usuario.getTipoUsuario() == TipoUsuario.ALUMNO && contador >= 1) {
                throw new IllegalStateException("alumno puede tener solo un prestamo");
            }
            if (usuario.getTipoUsuario() == TipoUsuario.DOCENTE && contador >= 3) {
                throw new IllegalStateException("docente puede tener hasta 3 prestamos");
            }
        }
        return true;
    }

    //1.2-validar recursos restringidos *REVISADO
    private boolean validarRecursosRestringidos(String legajoUsuario, String codigoRecurso) {
        if(legajoUsuario==null||legajoUsuario.isBlank()){
            throw new IllegalArgumentException("parametro legajo usuario vacio");
        }
        if(codigoRecurso==null||codigoRecurso.isBlank()){
            throw new IllegalArgumentException("parametro codigo de recurso vacio");
        }
        Usuario usuario = usuarioDAO.buscarPorLegajo(legajoUsuario);
        Recurso recurso = recursoDAO.obtenerRecursoPorCodigo(codigoRecurso);
        if (recurso == null ) {
            throw new IllegalArgumentException("no se encontro recurso");
        }
        if(usuario==null){
            throw new IllegalArgumentException("no se encontro usuario");
        }
        //Recursos restringidos,kitLaboratio solo docente
        if (usuario.getTipoUsuario() == TipoUsuario.ALUMNO
                && recurso.getTipoRecurso() == TipoRecurso.KITLABORATORIO) {
            throw new IllegalStateException("kit laboratorio solo lo puede usar docente");
        }
        //Recursos restringidos ,notebook solo alumnos
        if (usuario.getTipoUsuario() == TipoUsuario.DOCENTE
                && recurso.getTipoRecurso() == TipoRecurso.NOTEBOOK) {
            throw new IllegalStateException("notebook solo la pueden pedir alumnos");
        }
        return true;
    }

    //1.3-verificar prestamos buscando si esta ese recurso prestado *REVISADO
    private boolean verificarEstadoRecurso(String codigoRecurso) {
        ArrayList<Prestamo> prestamos = prestamoDAO.listarPrestamos();
        Recurso r = recursoDAO.obtenerRecursoPorCodigo(codigoRecurso);
        if (r == null) {
            throw new IllegalArgumentException("no existe recurso con ese codigo");
        }

        //si lo encuentra es porque esta prestado
        for (Prestamo p : prestamos) {
            if (p.getCodigoRecurso().equals(r.getCodigo()) && p.getEstadoPrestamo() == TipoEstadoPrestamo.ACTIVO) {
                throw new IllegalStateException ("no se puede prestar recurso que no este activo");
            }
        }
        return true;//recurso no prestado
    }

    //1.4-pasar prestamos a vencidos *REVISADO
    public void pasarPrestamosaVencidos() {
        prestamoDAO.pasarPrestamosVencidos();
    }

    //2-listar prestamos *REVISADO
    public void listarPrestamos() {
        ArrayList<Prestamo> prestamos = prestamoDAO.listarPrestamos();
        if (prestamos.isEmpty()) {
            throw new IllegalArgumentException("no hay prestamos activos");
        }
        for (Prestamo p : prestamos) {
            System.out.println(p);
        }
    }

    //3-listar prestamos por usuario *REVISADO
    public void listarPrestamosPorUsuario(String legajo) {
        if (legajo == null||legajo.isBlank()) {
            throw new IllegalArgumentException("el parametro legajo no puede estar vacio");
        }
        Usuario u = usuarioDAO.buscarPorLegajo(legajo);
        boolean hayUsuarioConPrestamo = false;
        //si usuario esta vacio
        if (u == null) {
            throw new IllegalArgumentException("no existe usuarios con ese legajo");
        }
        ArrayList<Prestamo> prestamos = prestamoDAO.listarPrestamos();
        for (Prestamo p : prestamos) {
            if (p.getLegajoUsuario().equals(u.getLegajo())) {
                System.out.println(p);
                hayUsuarioConPrestamo = true;
            }
        }
        if (!hayUsuarioConPrestamo) {
            throw new IllegalArgumentException("no existen prestamo con ese usuario");
        }
    }

    //4-listar prestamo por recurso *REVISADO
    public void listarPrestamoPorRecurso(String codigo) {
        if (codigo == null||codigo.isBlank()) {
            throw new IllegalArgumentException("el parametro codigo no puede estar vacio");
        }
        Recurso recurso = recursoDAO.obtenerRecursoPorCodigo(codigo);
        boolean hayPrestamos = false;
        if (recurso == null) {
           throw new IllegalArgumentException("no existe RECURSO con codigo: " + codigo);
        }
        ArrayList<Prestamo> prestamos = prestamoDAO.listarPrestamos();
        for (Prestamo p : prestamos) {
            if (p.getCodigoRecurso().equals(recurso.getCodigo())) {
                System.out.println(p);
                hayPrestamos = true;
            }
        }
        if (!hayPrestamos) {
            throw new IllegalArgumentException("no existen prestamos con el recurso " + recurso.getNombre());
        }
    }

    //5-consultar prestamo por id *REVISADO
    public void listarPrestamoPorId(int id) {
        Prestamo prestamo = prestamoDAO.listarprestamoPorId(id);
        if (prestamo==null) {
            throw new IllegalArgumentException("no existe prestamo con id: " + id);
        }
        System.out.println(prestamo);
    }

    // 6-registrarDevolucion *REVISADO
    public void registrarDevolucion(int idPrestamo) {
        //obtenemos el prestamo
        Prestamo prestamo = prestamoDAO.listarprestamoPorId(idPrestamo);
        //no se encuentra prestamo
        if (prestamo == null) {
            throw new IllegalArgumentException("no se encontro el prestamo con id:" + idPrestamo);
        }
        //VERIFICAR QUE ESTADO SEA ACTIVO O VENCIDO
        if (prestamo.getEstadoPrestamo() != TipoEstadoPrestamo.ACTIVO && prestamo.getEstadoPrestamo() != TipoEstadoPrestamo.VENCIDO) {
           throw new IllegalStateException ("error,el estado del prestamo para poder devolver debe ser activo o vencido");
        }
        //verificar que devolucion sea despues de prestamo inicio
        LocalDateTime fechaDevolucion = LocalDateTime.now();
        if (fechaDevolucion.isBefore(prestamo.getFechaInicio())) {
           throw new IllegalStateException("la fecha de devolucion no puede ser antes que la creacion del prestamo");
        }
        //llamamos a la funcion dao para registrar prestamo
        boolean seRegistroPrestamo = prestamoDAO.registrarDevolucion(idPrestamo);
        if (!seRegistroPrestamo) {
            throw new IllegalArgumentException("no se puedo registrar prestamo");
        }
        //editamos el objeto de la ram
        prestamo.setFechaDevolucion(fechaDevolucion);
        //se aplica sancion al usuario si es que hay dias de atraso
        aplicarSancion(prestamo);
    }

    //7-ver prestamos vencidos *REVISADO
    public void listarPrestamosVencidos() {
        ArrayList<Prestamo> prestamos = prestamoDAO.listarPrestamosVencidos();
        if (prestamos.isEmpty()) {
            throw new IllegalArgumentException("no hay prestamos vencidos");
        }
        for (Prestamo p : prestamos) {
            System.out.println(p);
        }
    }

    //8-ver historial de prestamo de un recurso *REVISADO
    public void listarHistorialRecurso(String legajo) {
        if(legajo==null||legajo.isBlank()){
            throw new IllegalArgumentException("parametro vacio");
        }
        Recurso recurso = recursoDAO.obtenerRecursoPorCodigo(legajo);
        if (recurso == null) {
            throw new IllegalArgumentException("no existe ese recurso con id:" + legajo);
        }
        ArrayList<Prestamo> prestamos = prestamoDAO.listarPrestamos();
        boolean tieneHistorial = false;
        for (Prestamo p : prestamos) {
            if (p.getCodigoRecurso().equals(recurso.getCodigo())) {
                System.out.println(p);
                tieneHistorial = true;
            }
        }
        if (!tieneHistorial) {
            throw new IllegalArgumentException("no tiene historial");
        }
    }

    //---------------------SANCIONES-------------------------

    //1-calcular atraso *REVISADO
    private long calcularAtraso(Prestamo prestamo) {
        //revisamos que exista prestamo
        if (prestamo == null) {
            throw new IllegalArgumentException("Prestamo inexistente");
        }
        //revisamos que exista fecha de devolucion
        if (prestamo.getFechaDevolucion() == null) {
            throw new IllegalArgumentException("este prestamo no ha sido devuelto");
        }
        //si fue entregado a tiempo
        if (prestamo.getFechaDevolucion().isBefore(prestamo.getFechaVencimiento())) {
            return 0;
        }
        //calculamos dia de atraso
        long diasAtraso = ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), prestamo.getFechaDevolucion());
        return diasAtraso;
    }

    //2-aplicar sancion al devolver el prestamo *REVISADO
    private boolean aplicarSancion(Prestamo prestamo) {
        long diasAtraso = calcularAtraso(prestamo);
        //si no hay atraso
        if (diasAtraso <= 0) {
            return false;
        }
        LocalDateTime ahora = LocalDateTime.now();

        //obtenemos legajo usuario
        String legajo = prestamo.getLegajoUsuario();
        //se bloquea desde el dia de la sancion mas los dias de atraso
        LocalDateTime fechaFinBloqueo = ahora.plusDays(diasAtraso);
        Sancion sancion=new Sancion(prestamo.getId(),legajo,diasAtraso,fechaInicioBloqueo,fechaFinBloqueo);
        //aplicamos sancion a usuario
        sancionDAO.bloquearUsuario(legajo, fechaFinBloqueo);
        //generamos sancion
        sancionDAO.registrarSancion(sancion);
        return true;
    }

    //3-consultar sanciones de un usuario *REVISADO
    public void consultarSancionesUsuario(String id) {
        if (id == null ||id.isBlank()) {
            throw new IllegalArgumentException("el parametro no puede estar vacio");
        }
        Usuario usuario = usuarioDAO.buscarPorLegajo(id);
        boolean tieneSanciones = false;
        if (usuario == null) {
            throw new IllegalArgumentException("no existe usuario con legajo: " + id);
        }
        ArrayList<Sancion> sanciones = sancionDAO.obtenerSanciones();
        for (Sancion s : sanciones) {
            if (s.getUsuariolegajo().equals(usuario.getLegajo())) {
                System.out.println(s);
                tieneSanciones = true;
            }
        }
        if (!tieneSanciones) {
            throw new IllegalArgumentException("este usuario nunca tuvo una sancion");
        }
    }

    //4-ver usuarios bloqueados *REVISADO
    public void verUsuariosBloqueados() {
        ArrayList<Sancion> sanciones = sancionDAO.obtenerSanciones();
        if (sanciones.isEmpty()) {
            throw new IllegalArgumentException("no hay usuarios bloqueados");
        }
        for (Sancion s : sanciones) {
            if(s.getFechaFinBloqueo().isAfter(LocalDateTime.now())){
                System.out.println("legajo "+ s.getUsuariolegajo() + " ,fecha de fin de bloqueo " + s.getFechaFinBloqueo());
            }

        }
    }
}

