package com.equipo.chilaquilapp.ui.confirmar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.chilaquilapp.data.repository.PedidoRepository
import com.equipo.chilaquilapp.data.repository.Resultado
import com.equipo.chilaquilapp.data.session.BorradorPedido
import com.equipo.chilaquilapp.data.session.PedidoEnProgreso
import com.equipo.chilaquilapp.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado de la pantalla de confirmar pedido: el [borrador] que se está por
 * enviar, la dirección de entrega (precargada del usuario pero editable), si la
 * dirección está en modo edición, el estado de envío y un posible error. Cuando
 * [pedidoEnviado] es `true` la pantalla navega a "pedido confirmado".
 */
data class ConfirmarUiState(
    val borrador: BorradorPedido? = null,
    val direccion: String = "",
    val editandoDireccion: Boolean = false,
    val enviando: Boolean = false,
    val error: String? = null,
    val pedidoEnviado: Boolean = false
)

@HiltViewModel
class ConfirmarViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val pedidoEnProgreso: PedidoEnProgreso,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ConfirmarUiState(
            borrador = pedidoEnProgreso.borrador,
            direccion = sessionManager.usuarioActual?.direccion.orEmpty()
        )
    )
    val uiState: StateFlow<ConfirmarUiState> = _uiState.asStateFlow()

    fun onDireccionChange(valor: String) {
        _uiState.update { it.copy(direccion = valor, error = null) }
    }

    fun toggleEditarDireccion() {
        _uiState.update { it.copy(editandoDireccion = !it.editandoDireccion) }
    }

    fun enviarPedido() {
        val estado = _uiState.value
        val borrador = estado.borrador
        val usuarioId = sessionManager.usuarioId

        if (borrador == null) {
            _uiState.update { it.copy(error = "No hay un pedido en proceso") }
            return
        }
        if (usuarioId == null) {
            _uiState.update { it.copy(error = "Inicia sesión para enviar tu pedido") }
            return
        }
        if (estado.direccion.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa una dirección de entrega") }
            return
        }

        _uiState.update { it.copy(enviando = true, error = null) }
        viewModelScope.launch {
            val resultado = pedidoRepository.crearPedido(
                usuarioId = usuarioId,
                productoId = borrador.producto.id,
                cantidad = borrador.cantidad,
                proteinaIds = borrador.proteinas.map { it.id },
                extraIds = borrador.extras.map { it.id },
                direccionEntrega = estado.direccion.trim()
            )

            when (resultado) {
                is Resultado.Exito -> {
                    pedidoEnProgreso.limpiar()
                    _uiState.update { it.copy(enviando = false, pedidoEnviado = true) }
                }

                is Resultado.Error -> _uiState.update {
                    it.copy(enviando = false, error = resultado.mensaje)
                }
            }
        }
    }
}
