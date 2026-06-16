package com.equipo.chilaquilapp.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val LOCALE_MX = Locale("es", "MX")

/**
 * Formatea un precio como `$12.50`. Los precios viajan en la API como `Double`
 * (camelCase, sección 5 del plan) y se muestran siempre con dos decimales.
 */
fun formatearPrecio(precio: Double): String =
    "$" + String.format(Locale.US, "%.2f", precio)

/**
 * Convierte la fecha ISO 8601 que devuelve la API (ej. `2026-06-14T10:00:00Z`)
 * a un formato legible en español como `14 de jun, 2026 • 10:00 a. m.`.
 *
 * Si la cadena no se puede interpretar, se devuelve tal cual para no romper la
 * pantalla (manejo defensivo: la API es el contrato, pero la UI no debe caerse
 * si llega algo inesperado).
 */
fun formatearFecha(iso: String): String = try {
    val fecha = OffsetDateTime.parse(iso)
    val formato = DateTimeFormatter.ofPattern("d 'de' MMM, yyyy • hh:mm a", LOCALE_MX)
    fecha.format(formato)
} catch (e: Exception) {
    iso
}
