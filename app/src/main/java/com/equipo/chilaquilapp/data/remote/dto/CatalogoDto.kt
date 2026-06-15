package com.equipo.chilaquilapp.data.remote.dto

import com.equipo.chilaquilapp.domain.model.Extra
import com.equipo.chilaquilapp.domain.model.Proteina

data class ProteinaDto(
    val id: Int,
    val nombre: String
) {
    fun toDomain() = Proteina(id = id, nombre = nombre)
}

data class ExtraDto(
    val id: Int,
    val nombre: String
) {
    fun toDomain() = Extra(id = id, nombre = nombre)
}
