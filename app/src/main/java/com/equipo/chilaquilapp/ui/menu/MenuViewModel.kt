package com.equipo.chilaquilapp.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.chilaquilapp.data.repository.ProductoRepository
import com.equipo.chilaquilapp.data.repository.Resultado
import com.equipo.chilaquilapp.domain.model.Producto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado de la pantalla de menú: la lista de productos del catálogo, si se está
 * cargando y un posible mensaje de error. Cubre los estados de carga/éxito/error
 * que pide el plan (checklist de entrega).
 */
data class MenuUiState(
    val cargando: Boolean = true,
    val productos: List<Producto> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            when (val resultado = productoRepository.obtenerProductos()) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(cargando = false, productos = resultado.datos)
                }

                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}
