package com.equipo.chilaquilapp.data.repository

import com.equipo.chilaquilapp.data.remote.dto.ErrorResponseDto
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

/**
 * Envoltura mínima de resultado para las llamadas a la API. El manejo global
 * de errores (estados de carga/éxito/error en la UI) se completa en la tarea #21.
 */
sealed class Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>()
    data class Error(val codigo: String, val mensaje: String) : Resultado<Nothing>()
}

private val errorGson = Gson()

/**
 * Convierte una [Response] de Retrofit en un [Resultado], leyendo el cuerpo de
 * error con el formato único `{ "error": { "codigo", "mensaje" } }` cuando la
 * llamada no fue exitosa.
 */
fun <T, R> Response<T>.toResultado(transform: (T) -> R): Resultado<R> {
    if (isSuccessful) {
        val body = body()
        return if (body != null) {
            Resultado.Exito(transform(body))
        } else {
            Resultado.Error("RESPUESTA_VACIA", "La respuesta del servidor está vacía")
        }
    }

    val error = errorBody()?.charStream()?.use { reader ->
        runCatching { errorGson.fromJson(reader, ErrorResponseDto::class.java) }.getOrNull()
    }
    return Resultado.Error(
        codigo = error?.error?.codigo ?: "ERROR_DESCONOCIDO",
        mensaje = error?.error?.mensaje ?: "Ocurrió un error inesperado"
    )
}

/**
 * Ejecuta una llamada a la API atrapando errores de conexión (sin internet,
 * timeout, etc.) y devolviéndolos como [Resultado.Error].
 */
suspend fun <T> safeApiCall(block: suspend () -> Resultado<T>): Resultado<T> {
    return try {
        block()
    } catch (e: IOException) {
        Resultado.Error("ERROR_RED", "No se pudo conectar con el servidor")
    }
}
