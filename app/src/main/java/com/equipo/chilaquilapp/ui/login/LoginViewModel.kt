package com.equipo.chilaquilapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.chilaquilapp.data.repository.AuthRepository
import com.equipo.chilaquilapp.data.repository.Resultado
import com.equipo.chilaquilapp.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado de la pantalla de inicio de sesión: lo que el usuario escribe, si hay
 * una llamada en curso, el mensaje de error a mostrar y si el login ya fue
 * exitoso (para que la pantalla navegue al menú).
 */
data class LoginUiState(
    val nombreUsuario: String = "",
    val contrasena: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onNombreUsuarioChange(valor: String) {
        _uiState.update { it.copy(nombreUsuario = valor, error = null) }
    }

    fun onContrasenaChange(valor: String) {
        _uiState.update { it.copy(contrasena = valor, error = null) }
    }

    fun iniciarSesion() {
        val estado = _uiState.value
        if (estado.nombreUsuario.isBlank() || estado.contrasena.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa tu usuario y contraseña") }
            return
        }
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            when (val resultado = authRepository.login(estado.nombreUsuario.trim(), estado.contrasena)) {
                is Resultado.Exito -> {
                    sessionManager.iniciarSesion(resultado.datos)
                    _uiState.update { it.copy(cargando = false, loginExitoso = true) }
                }

                is Resultado.Error -> {
                    _uiState.update { it.copy(cargando = false, error = resultado.mensaje) }
                }
            }
        }
    }
}
