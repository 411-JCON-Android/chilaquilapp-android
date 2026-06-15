package com.equipo.chilaquilapp.data

import com.equipo.chilaquilapp.domain.model.Usuario
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton que mantiene en memoria al usuario logueado durante la sesión.
 *
 * Login y Registro escriben aquí; las pantallas que necesitan el usuarioId
 * o la dirección la leen de aquí. No persiste entre cierres de la app
 * (el usuario vuelve a login), que es suficiente para el proyecto escolar.
 */
@Singleton
class UserSession @Inject constructor() {

    private var _usuario: Usuario? = null

    /** Usuario activo o null si no ha hecho login. */
    val usuario: Usuario? get() = _usuario

    /** Guarda al usuario tras un login/registro exitoso. */
    fun iniciarSesion(usuario: Usuario) {
        _usuario = usuario
    }

    /** Limpia la sesión (logout). */
    fun cerrarSesion() {
        _usuario = null
    }
}
