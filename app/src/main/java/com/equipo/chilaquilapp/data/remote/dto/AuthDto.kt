package com.equipo.chilaquilapp.data.remote.dto

import com.equipo.chilaquilapp.domain.model.Usuario

data class RegistroRequest(
    val nombre: String,
    val nombreUsuario: String,
    val contrasena: String,
    val direccion: String
)

data class LoginRequest(
    val nombreUsuario: String,
    val contrasena: String
)

data class UsuarioDto(
    val id: Int,
    val nombre: String,
    val nombreUsuario: String,
    val direccion: String
) {
    fun toDomain() = Usuario(
        id = id,
        nombre = nombre,
        nombreUsuario = nombreUsuario,
        direccion = direccion
    )
}
