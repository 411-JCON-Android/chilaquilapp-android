package com.equipo.chilaquilapp.data.session

import com.equipo.chilaquilapp.domain.model.Usuario
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Guarda en memoria al usuario con la sesión activa. Como la API usa
 * autenticación simple **sin token** (sección 5 del plan), basta con conservar
 * el [Usuario] devuelto por login/registro para poder mandar su `id` en las
 * llamadas que lo requieren (ej. crear pedido o historial).
 *
 * Es un singleton inyectable: el login lo llena y las pantallas de la Fase 1
 * leen `usuarioId` desde aquí.
 */
@Singleton
class SessionManager @Inject constructor() {

    var usuarioActual: Usuario? = null
        private set

    val usuarioId: Int?
        get() = usuarioActual?.id

    fun iniciarSesion(usuario: Usuario) {
        usuarioActual = usuario
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}
