package com.equipo.chilaquilapp.data.repository

import com.equipo.chilaquilapp.data.remote.ApiService
import com.equipo.chilaquilapp.data.remote.dto.LoginRequest
import com.equipo.chilaquilapp.data.remote.dto.RegistroRequest
import com.equipo.chilaquilapp.domain.model.Usuario
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun registro(
        nombre: String,
        nombreUsuario: String,
        contrasena: String,
        direccion: String
    ): Resultado<Usuario> = safeApiCall {
        apiService.registro(RegistroRequest(nombre, nombreUsuario, contrasena, direccion))
            .toResultado { it.toDomain() }
    }

    suspend fun login(nombreUsuario: String, contrasena: String): Resultado<Usuario> = safeApiCall {
        apiService.login(LoginRequest(nombreUsuario, contrasena))
            .toResultado { it.toDomain() }
    }
}
