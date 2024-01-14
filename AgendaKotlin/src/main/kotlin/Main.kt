import java.io.File
import java.util.*

val RUTA = File("C:\\Users\\ruben\\IdeaProjects\\AgendaKotlin\\src\\main\\kotlin").absolutePath

const val NOMBRE_FICHERO = "contactos.csv"

val RUTA_FICHERO = File(RUTA, NOMBRE_FICHERO).toString()

val OPCIONES_MENU = setOf("1", "2", "3", "4", "5", "6", "7", "8")

/**Muestra el menú de la agenda
 */

fun mostrarMenu() {
    val opcionesMenu = listOf(
        "Nuevo contacto",
        "Modificar contacto",
        "Eliminar contacto",
        "Vaciar agenda",
        "Cargar agenda inicial",
        "Mostrar contactos por criterio",
        "Mostrar la agenda completa",
        "Salir"
    )

    println("\nMENU")
    println("---------------------------")

    opcionesMenu.forEachIndexed { index, opcion ->
        println("${index + 1}. $opcion")
    }

    print("\nSeleccione una opción: ")
}

/**Limpia la consola
 */

fun borrarConsola() {
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
        ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
    } else {
        Runtime.getRuntime().exec("clear")
    }
}

/**
 * Carga los contactos iniciales de la agenda desde un archivo
 *
 * @param contactos Lista que se rellenará con la información del archivo
 */

fun cargarContactos(contactos: MutableList<MutableMap<String, Any>>) {
    try {
        val archivo = File(RUTA_FICHERO)

        if (!archivo.exists()) {
            println("El archivo de contactos no existe.")
            return
        }

        with(archivo.bufferedReader()) {
            for (linea in readLines()) {
                val partes = linea.split(";")

                val contacto = mutableMapOf(
                    "nombre" to partes[0],
                    "apellido" to (partes[1]),
                    "email" to (partes[2]),
                    "telefonos" to (partes.drop(3))
                )

                contactos.add(contacto)
            }

            println("Contactos cargados exitosamente.")
            println("Contactos en la lista después de la carga: $contactos")
        }
    } catch (e: Exception) {
        println("Error al cargar los contactos: $e")
        println("Contactos en la lista después de la carga fallida: $contactos")
    }
}

/** Crea un diccionario que representa un contacto
 *
 * @param contactos Lista de contactos donde se va a agregar un registro
 * */

fun agregarContacto(contactos: MutableList<MutableMap<String, Any>>) {
    var nombre = ""
    var salir1 = false

    while (!salir1) {
        print("Ingrese el nombre: ")
        nombre = readln()
        if (nombre.isEmpty()) {
            println("Debe introducir un nombre obligatoriamente")
        } else {
            salir1 = true
        }

    }

    var apellido = ""
    var salir2 = false

    while (!salir2) {
        print("Ingrese el apellido: ")
        apellido = readln()
        if (apellido.isEmpty()) {
            println("Debe introducir un apellido obligatoriamente.")
        } else {
            salir2 = true
        }

    }

    var email = ""
    var salir3 = false

    while (!salir3) {
        println("Ingrese el email (gmail.com):")
        email = readln()
        if (email.isEmpty()) {
            println("Debe introducir un email obligatoriamente")
        } else {
            if (validarEmail(email)) {
                salir3 = true
            } else {
                println("Email no válido, debe terminar con @gmail.com")
            }
        }
    }

    val telefonos = mutableListOf<String>()
    var salir4 = false

    while (!salir4) {
        println("Ingrese un teléfono de 9 dígitos que empiece con +34 opcionalmente (deje vacío para salir):")
        val telefono = readln()
        if (telefono.isEmpty()) {
            salir4 = true
        } else {
            if (validarTelefono(telefono)) {
                telefonos.add(telefono)
            } else {
                println("Teléfono no válido, introduce un número de 9 dígitos (+34 delante opcional)")
            }
        }

    }

    val nuevoContacto = mutableMapOf(
        "nombre" to nombre,
        "apellido" to apellido,
        "email" to email,
        "telefonos" to telefonos
    )

    contactos.add(nuevoContacto)
    println("Contacto agregado exitosamente.")
}

/** Controla que el email sea el correcto
 *
 * @param email Email que va a validar si es correcto o no
 * */

fun validarEmail(email: String): Boolean {
    val email = email.endsWith("@gmail.com")
    return email
}

/** Controla que el teléfono sea válido
 *
 * @param telefono Teléfono que va a validar si es correcto o no
 * */

fun validarTelefono(telefono: String): Boolean {
    val conPrefijo = telefono.startsWith("+34") && telefono.length == 12
    val sinPrefijo = telefono.length == 9
    return conPrefijo || sinPrefijo
}

/** Modifica la información de un contacto
 *
 * @param contactos Lista de contactos a modificar
 * */

fun modificarContacto(contactos: MutableList<MutableMap<String, Any>>) {
    print("Ingrese el email del contacto que desea modificar: ")
    val emailActual = readln()

    val posicion = buscarContacto(contactos, emailActual)

    if (posicion != null) {
        val contacto = contactos[posicion]

        println("\nInformación actual del contacto:")
        mostrarContacto(contacto)

        print("Ingrese el nuevo nombre (no introducir nada para no actualizar): ")
        val nuevoNombre = readln()
        print("Ingrese el nuevo apellido (no introducir nada para no actualizar): ")
        val nuevoApellido = readln()
        print("Ingrese el nuevo email (no introducir nada para no actualizar): ")
        val nuevoEmail = readln()

        if (!nuevoNombre.isNullOrEmpty()) {
            contacto["nombre"] = nuevoNombre
        }
        if (!nuevoApellido.isNullOrEmpty()) {
            contacto["apellido"] = nuevoApellido
        }
        if (!nuevoEmail.isNullOrEmpty()) {
            contacto["email"] = nuevoEmail
        }

        println("\nContacto modificado")

        println("\nInformación actualizada del contacto:")
        mostrarContacto(contacto)
    } else {
        println("\nNo se encontró el contacto para modificar.")
    }

    pulseTeclaParaContinuar()
    borrarConsola()
}

/** Vacia la agenda, eliminando todos los contactos
 *
 * @param contactos Lista de contactos donde se va a vaciar todo
 * */

fun vaciarAgenda(contactos: MutableList<MutableMap<String, Any>>) {
    print("¿Está seguro de que desea vaciar la agenda? (si/no): ")
    val confirmar = readln().toLowerCase()

    when (confirmar) {
        "si" -> {
            contactos.clear()
            println("La agenda se ha vaciado")
        }

        "no" -> println("No se ha vaciado la agenda")
        else -> println("Error, introduce una opción correcta")
    }
}

/**
 * Muestra los contactos ordenados por un criterio dentro de las opciones
 *
 * @param contactos Lista de contactos que se mostrarán por un criterio
 */

fun mostrarContactosPorCriterio(contactos: List<Map<String, Any>>) {
    if (contactos.isEmpty()) {
        println("La agenda está vacía.")
        return
    }

    var salir = false

    while (!salir) {
        println("\nREGISTROS POR CRITERIO")
        println("----------------------")

        println("Selecciona el criterio:")
        println("1. Nombre")
        println("2. Apellido")
        println("3. Email")
        println("4. Cancelar")

        val opciones = setOf("1", "2", "3", "4")

        val opcion = readln()

        if (opcion !in opciones) {
            println("Opción no válida. Introduce un número del 1 al 4.")
        } else {
            when (opcion) {
                "1" -> mostrarContactosOrdenadosPorCriterio(contactos, "nombre")
                "2" -> mostrarContactosOrdenadosPorCriterio(contactos, "apellido")
                "3" -> mostrarContactosOrdenadosPorCriterio(contactos, "email")
                "4" -> {
                    println("\nVolviendo al menú principal")
                    salir = true
                }
            }
        }

        pulseTeclaParaContinuar()
        borrarConsola()
    }
}

/**
 * Muestra los contactos ordenados por un criterio
 *
 * @param contactos Lista de contactos que se van a ordenar
 * @param criterio Criterio por el cual se ordenan los contactos
 */

fun mostrarContactosOrdenadosPorCriterio(contactos: List<Map<String, Any>>, criterio: String) {
    println("\nREGISTROS POR $criterio")
    println("-----------------------")

    val listaOrdenada = contactos.sortedBy { contacto -> contacto[criterio].toString() }

    for (contacto in listaOrdenada) {
        mostrarContacto(contacto)
    }
}

/**
 * Muestra todos los contactos de la agenda
 *
 * @param contactos Lista de contactos que se van a mostrar
 */

fun mostrarContactos(contactos: List<Map<String, Any>>) {
    if (contactos.isEmpty()) {
        println("La agenda está vacía.")
    } else {
        println("\nREGISTROS")
        println("--------------------------")

        val contactosOrdenados = contactos.sortedBy { contacto ->
            contacto["nombre"].toString()
        }

        for (contacto in contactosOrdenados) {
            mostrarContacto(contacto)
        }
    }
}

/**
 * Muestra un contacto en un formato específico
 *
 * @param contactos Diccionario donde se van a guardar los datos
 */

fun mostrarContacto(contacto: Map<String, Any>) {
    println("Nombre: ${contacto["nombre"]} ${contacto["apellido"]}")
    println("Email: ${contacto["email"]}")

    val telefonos = contacto["telefonos"] as List<String>
    val telefonosString = telefonos.joinToString(" - ")

    println("Teléfonos: $telefonosString\n")
}

/**
 * Elimina un contacto de la agenda
 *
 * @param contactos Lista de contactos donde se va a eliminar el contacto
 */

fun eliminarContacto(contactos: MutableList<MutableMap<String, Any>>) {
    print("Ingrese el email del contacto que desea eliminar: ")
    val eliminado = readln().toLowerCase()

    val pos = buscarContacto(contactos, eliminado)

    if (pos != null) {
        contactos.removeAt(pos)
        println("Se eliminó 1 contacto")
    } else {
        println("No se encontró el contacto para eliminar")
    }
}

/**
 * Pide al usuario que ingrese una opción del menú y la devuelve
 */

fun pedirOpcion(): String? {
    print("Seleccione una opción: ")
    val opcion = readln()
    return opcion
}

/**
 * Ejecuta el menú de la agenda con varias opciones
 *
 * @param contactos Lista de contactos a partir de la cual se van a ejecutar las opciones
 */

fun agenda(contactos: MutableList<MutableMap<String, Any>>) {
    var opcion: String? = null

    while (opcion != "8") {
        mostrarMenu()
        opcion = pedirOpcion()

        val opcionesValidas = OPCIONES_MENU

        if (opcion !in opcionesValidas) {
            println("Opción no válida. Introduce un número del 1 al 8.")
        } else {
            when (opcion) {
                "1" -> agregarContacto(contactos)
                "2" -> modificarContacto(contactos)
                "3" -> eliminarContacto(contactos)
                "4" -> vaciarAgenda(contactos)
                "5" -> cargarContactos(contactos)
                "6" -> mostrarContactosPorCriterio(contactos)
                "7" -> mostrarContactos(contactos)
                "8" -> println("\nSaliendo del programa...")
            }
        }

        pulseTeclaParaContinuar()
        borrarConsola()
    }
}

/**
 * Busca un contacto en la lista por su email y devuelve la posición si lo encuentra
 *
 * @param contactos Lista de contactos donde se va a buscar un registro
 * @param email Email a partir del cual se va a buscar al contacto
 */

fun buscarContacto(contactos: List<Map<String, Any>>, email: String): Int? {
    for ((index, contacto) in contactos.withIndex()) {
        if (contacto["email"] == email) {
            return index
        }
    }
    return null
}

/**Muestra un mensaje y realiza una pausa hasta que se pulse una tecla
 */

fun pulseTeclaParaContinuar() {
    println("\nPresiona Enter para continuar...")
    Scanner(System.`in`).nextLine()
}

fun main() {
    borrarConsola()
    val contactos = mutableListOf<MutableMap<String, Any>>()
    cargarContactos(contactos)
    agenda(contactos)
}
