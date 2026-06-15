package com.equipo.chilaquilapp.data.remote.dto

/**
 * Formato único de error de la API: `{ "error": { "codigo": "...", "mensaje": "..." } }`.
 */
data class ErrorResponseDto(
    val error: ErrorDetalleDto
)

data class ErrorDetalleDto(
    val codigo: String,
    val mensaje: String
)
