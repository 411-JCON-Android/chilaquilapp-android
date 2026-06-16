package com.equipo.chilaquilapp.ui.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.chilaquilapp.data.repository.PedidoRepository
import com.equipo.chilaquilapp.data.repository.Resultado
import com.equipo.chilaquilapp.data.session.SessionManager
import com.equipo.chilaquilapp.domain.model.PedidoResumen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado de la pantalla de historial: los pedidos previos del usuario, si se
 * están cargando y un posible error. La lista vacía con `cargando = false` y
 * sin error se muestra como estado "sin pedidos".
 */
data class HistorialUiState(
    val cargando: Boolean = true,
    val pedidos: List<PedidoResumen> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistorialUiState())
    val uiState: StateFlow<HistorialUiState> = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        val usuarioId = sessionManager.usuarioId
        if (usuarioId == null) {
            _uiState.update {
                it.copy(cargando = false, error = "Inicia sesión para ver tus pedidos")
            }
            return
        }

        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            when (val resultado = pedidoRepository.obtenerHistorial(usuarioId)) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(cargando = false, pedidos = resultado.datos)
                }

                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}
