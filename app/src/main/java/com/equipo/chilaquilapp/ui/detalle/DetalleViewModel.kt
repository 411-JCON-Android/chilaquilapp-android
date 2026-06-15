package com.equipo.chilaquilapp.ui.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.chilaquilapp.data.UserSession
import com.equipo.chilaquilapp.data.repository.PedidoRepository
import com.equipo.chilaquilapp.data.repository.ProductoRepository
import com.equipo.chilaquilapp.data.repository.Resultado
import com.equipo.chilaquilapp.domain.model.Extra
import com.equipo.chilaquilapp.domain.model.ProductoDetalle
import com.equipo.chilaquilapp.domain.model.Proteina
import com.equipo.chilaquilapp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetalleUiState(
    val producto: ProductoDetalle? = null,
    val proteinasSeleccionadas: Set<Int> = emptySet(),
    val extrasSeleccionados: Set<Int> = emptySet(),
    val cantidad: Int = 1,
    val cargando: Boolean = true,
    val error: String? = null,
    val pedidoEnviado: Boolean = false
)

@HiltViewModel
class DetalleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productoRepo: ProductoRepository,
    private val pedidoRepo: PedidoRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val productoId: Int =
        savedStateHandle.get<String>(Routes.Detalle.ARG_PRODUCTO_ID)?.toIntOrNull() ?: 0

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        cargarProducto()
    }

    private fun cargarProducto() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val resultado = productoRepo.obtenerProducto(productoId)) {
                is Resultado.Exito -> {
                    val producto = resultado.datos
                    // Pre-seleccionar todos los extras (son gratis)
                    val extrasPreseleccionados = producto.extras.map { it.id }.toSet()
                    _uiState.update {
                        it.copy(
                            producto = producto,
                            extrasSeleccionados = extrasPreseleccionados,
                            cargando = false
                        )
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

    fun toggleProteina(proteinaId: Int) {
        _uiState.update { state ->
            val nuevas = state.proteinasSeleccionadas.toMutableSet()
            if (proteinaId in nuevas) nuevas.remove(proteinaId) else nuevas.add(proteinaId)
            state.copy(proteinasSeleccionadas = nuevas)
        }
    }

    fun toggleExtra(extraId: Int) {
        _uiState.update { state ->
            val nuevos = state.extrasSeleccionados.toMutableSet()
            if (extraId in nuevos) nuevos.remove(extraId) else nuevos.add(extraId)
            state.copy(extrasSeleccionados = nuevos)
        }
    }

    fun incrementarCantidad() {
        _uiState.update { it.copy(cantidad = it.cantidad + 1) }
    }

    fun decrementarCantidad() {
        _uiState.update { state ->
            if (state.cantidad > 1) state.copy(cantidad = state.cantidad - 1) else state
        }
    }

    /** Precio total = precio del producto × cantidad. Extras son gratis. */
    fun calcularTotal(): Double {
        val state = _uiState.value
        val precioBase = state.producto?.precio ?: 0.0
        return precioBase * state.cantidad
    }

    /** Crea el pedido a través de la API. */
    fun crearPedido(onExito: () -> Unit) {
        val state = _uiState.value
        val usuario = userSession.usuario
        val producto = state.producto

        if (usuario == null || producto == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val resultado = pedidoRepo.crearPedido(
                usuarioId = usuario.id,
                productoId = producto.id,
                cantidad = state.cantidad,
                proteinaIds = state.proteinasSeleccionadas.toList(),
                extraIds = state.extrasSeleccionados.toList(),
                direccionEntrega = usuario.direccion
            )
            when (resultado) {
                is Resultado.Exito -> {
                    _uiState.update { it.copy(cargando = false, pedidoEnviado = true) }
                    onExito()
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
        cargarProducto()
    }
}
