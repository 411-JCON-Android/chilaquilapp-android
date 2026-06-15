package com.equipo.chilaquilapp.ui.detallepedido

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.chilaquilapp.data.repository.PedidoRepository
import com.equipo.chilaquilapp.data.repository.Resultado
import com.equipo.chilaquilapp.domain.model.PedidoDetalle
import com.equipo.chilaquilapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetallePedidoUiState(
    val pedido: PedidoDetalle? = null,
    val cargando: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DetallePedidoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pedidoRepo: PedidoRepository
) : ViewModel() {

    private val pedidoId: Int =
        savedStateHandle.get<String>(Routes.DetallePedido.ARG_PEDIDO_ID)?.toIntOrNull() ?: 0

    private val _uiState = MutableStateFlow(DetallePedidoUiState())
    val uiState: StateFlow<DetallePedidoUiState> = _uiState.asStateFlow()

    init {
        cargarPedido()
    }

    private fun cargarPedido() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val resultado = pedidoRepo.obtenerPedido(pedidoId)) {
                is Resultado.Exito -> {
                    _uiState.update {
                        it.copy(pedido = resultado.datos, cargando = false)
                    }
                }
                is Resultado.Error -> {
                    _uiState.update {
                        it.copy(cargando = false, error = resultado.mensaje)
                    }
                }
            }
        }
    }

    fun reintentar() {
        cargarPedido()
    }
}
