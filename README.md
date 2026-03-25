# SISTEMA-RESERVAS-MYSQL
ejercicio completo de sistema reserva con mysql listo para conectar a spring bot
📘 Agenda de Contactos / Sistema de Préstamos
🧠 Cambio de enfoque: de memoria a base de datos

Al integrar una base de datos (MySQL), cambia completamente la lógica del sistema:

La fuente de verdad pasa a ser la base de datos
Las estructuras en memoria (como HashMap) dejan de ser necesarias
Los datos en memoria se usan solo temporalmente
🗑️ Eliminación de UsuariosService

Antes:

UsuariosService manejaba validaciones y almacenamiento

Ahora:

Se elimina porque:
Ya no usamos estructuras en memoria
Las validaciones se trasladan a SistemaPrestamo
El DAO es quien interactúa con la base de datos

👉 Nueva responsabilidad:

SistemaPrestamo → UsuarioDAO
🧩 DAO (Data Access Object)

El DAO es el encargado de:

Conectarse a la base de datos
Ejecutar consultas
Mapear datos entre Java y MySQL
🔁 Uso de GET

Aunque ya no usamos estructuras en memoria:

Seguimos usando getters
Pero ahora sirven para:
Obtener atributos del objeto
Enviar datos al DAO

Ejemplo:

usuario.getNombre();
🔤 ENUM → STRING

MySQL no maneja enums como Java, por lo tanto:

enumEstado.name()

👉 Convierte el enum a String para guardarlo en la base

📅 Manejo de fechas

Se usa:

import java.sql.Timestamp;

👉 Permite guardar fechas en formato DATETIME de MySQL

⚠️ Problema:

Timestamp no maneja bien valores null
Hay que validar antes de convertir
❌ Error común: ID autogenerado en memoria
static int id;

👉 Esto está mal porque:

La fuente de verdad es MySQL
El ID debe venir de la base de datos (AUTO_INCREMENT)
⚖️ Comparación de objetos (equals)

❌ Incorrecto:

obj1.equals(obj2)

👉 Problema:

Compara referencias en memoria

✅ Correcto:

Comparar por clave (ID)
⚠️ Uso de set

Antes (con estructuras en memoria):

Tenía sentido usar set

Ahora (con base de datos):

No tiene sentido persistente
Solo afecta al objeto en memoria temporal
⚙️ Orden de validaciones

Siempre:

if (obj == null) → primero

👉 Para evitar NullPointerException

⚖️ Coherencia vs Rendimiento

Problema:

Muchas funciones consultan la base de datos

Optimización:

Pasar objetos ya obtenidos como parámetro
Evitar consultas innecesarias

Ejemplo:

En devolución:
Sabemos que solo cambia fechaDevolucion
Podemos modificar el objeto en memoria con set

👉 Trade-off:

Mejor rendimiento
Ligera pérdida de coherencia (controlada)
🚫 Uso de System.out.println

❌ Mala práctica

✅ Mejor:

throw new IllegalArgumentException("mensaje");
🧠 Manejo de errores
Tipo de error	Solución
Parámetro inválido	IllegalArgumentException
Regla de negocio	if
Estado inválido (bloqueado, límite)	IllegalStateException
Error técnico / BD	RuntimeException
🚫 Bandera para listas vacías

❌ Incorrecto:

boolean vacio = true;

✅ Correcto:

lista.isEmpty()
👤 Creación de Usuario

Antes:

SistemaPrestamo → valida → DAO

Ahora:

Validaciones en el constructor
Código más limpio

👉 Flujo final:

SistemaPrestamo → UsuarioDAO
💉 Inyección de dependencias

❌ Mala práctica:

private final UsuarioDAO usuarioDAO = new UsuarioDAO();

👉 Problemas:

Alto acoplamiento
Difícil de testear

✅ Correcto:

Pasar el DAO por constructor
public SistemaPrestamo(UsuarioDAO usuarioDAO) {
    this.usuarioDAO = usuarioDAO;
}
🧪 Beneficio

Ahora podés:

Usar distintos DAO (ej: mock para testing)
Testear más fácil

👉 Idea clave:

“No crees tus dependencias, recibilas”

🏗️ Cambios estructurales
1️⃣ Maven
Permite escalabilidad
Manejo de dependencias (MySQL)
Proyecto portable
2️⃣ Estructura de carpetas

Separación clara:

📂 datos
📂 sistema
📂 interfaz
📂 dominio
3️⃣ Constructores mejorados
Se crean múltiples constructores en:
Préstamos
Sanciones

👉 Beneficio:

Validaciones centralizadas
Mejor coherencia
🧠 Idea final

👉 Antes:

Memoria como fuente de verdad

👉 Ahora:

Base de datos como fuente de verdad
