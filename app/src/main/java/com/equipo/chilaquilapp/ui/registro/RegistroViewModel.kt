package com.equipo.chilaquilapp.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.chilaquilapp.data.repository.AuthRepository
import com.equipo.chilaquilapp.data.repository.Resultado
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estados posibles del proceso de registro, consumidos por [RegistroScreen]
 * para reaccionar a la carga, el éxito y los errores de validación/red.
 */
sealed interface RegistroEstado {
    data object Idle : RegistroEstado
    data object Loading : RegistroEstado
    data object Success : RegistroEstado
    data class Error(val mensaje: String) : RegistroEstado
}

/**
 * Estado de la UI de la pantalla de registro: los cuatro campos del formulario
 * más el [estado] del proceso. Se expone como un único [StateFlow] inmutable.
 */
data class RegistroUiState(
    val nombre: String = "",
    val nombreUsuario: String = "",
    val contrasena: String = "",
    val direccion: String = "",
    val estado: RegistroEstado = RegistroEstado.Idle
)

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onNombreChange(valor: String) = actualizarCampo { it.copy(nombre = valor) }

    fun onNombreUsuarioChange(valor: String) = actualizarCampo { it.copy(nombreUsuario = valor) }

    fun onContrasenaChange(valor: String) = actualizarCampo { it.copy(contrasena = valor) }

    fun onDireccionChange(valor: String) = actualizarCampo { it.copy(direccion = valor) }

    /**
     * Valida que ningún campo esté vacío. Si falta alguno emite un [RegistroEstado.Error];
     * si todo es válido pasa a [RegistroEstado.Loading], llama a [AuthRepository.registro]
     * y mapea el [Resultado] a [RegistroEstado.Success] o [RegistroEstado.Error].
     */
    fun registrarUsuario() {
        val estadoActual = _uiState.value

        if (estadoActual.nombre.isBlank() ||
            estadoActual.nombreUsuario.isBlank() ||
            estadoActual.contrasena.isBlank() ||
            estadoActual.direccion.isBlank()
        ) {
            _uiState.update { it.copy(estado = RegistroEstado.Error("Por favor, completa todos los campos.")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(estado = RegistroEstado.Loading) }

            val resultado = authRepository.registro(
                nombre = estadoActual.nombre.trim(),
                nombreUsuario = estadoActual.nombreUsuario.trim(),
                contrasena = estadoActual.contrasena,
                direccion = estadoActual.direccion.trim()
            )

            _uiState.update {
                when (resultado) {
                    is Resultado.Exito -> it.copy(estado = RegistroEstado.Success)
                    is Resultado.Error -> it.copy(estado = RegistroEstado.Error(resultado.mensaje))
                }
            }
        }
    }

    /**
     * Actualiza un campo y, si había un error de validación visible, lo limpia
     * para que el mensaje desaparezca en cuanto el usuario corrige el formulario.
     */
    private fun actualizarCampo(transform: (RegistroUiState) -> RegistroUiState) {
        _uiState.update { estado ->
            val actualizado = transform(estado)
            if (actualizado.estado is RegistroEstado.Error) {
                actualizado.copy(estado = RegistroEstado.Idle)
            } else {
                actualizado
            }
        }
    }
}
