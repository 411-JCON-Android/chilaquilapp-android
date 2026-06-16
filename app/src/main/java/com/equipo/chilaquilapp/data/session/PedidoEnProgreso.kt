package com.equipo.chilaquilapp.data.session

import com.equipo.chilaquilapp.domain.model.Extra
import com.equipo.chilaquilapp.domain.model.ProductoDetalle
import com.equipo.chilaquilapp.domain.model.Proteina
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Borrador del pedido que el cliente está armando: el producto elegido, la
 * cantidad y las proteínas/extras seleccionados. El total se calcula a partir
 * del precio del producto por la cantidad (los extras son gratis, supuesto del
 * plan), así que no se guarda aparte.
 */
data class BorradorPedido(
    val producto: ProductoDetalle,
    val cantidad: Int,
    val proteinas: List<Proteina>,
    val extras: List<Extra>
) {
    val total: Double get() = producto.precio * cantidad
}

/**
 * Puente en memoria entre la pantalla de detalle/personalización y la de
 * confirmar pedido. Como cada pedido es de un solo producto (sección 3) y la
 * navegación entre pantallas no lleva objetos complejos, la pantalla de detalle
 * deja aquí el [borrador] y la de confirmar lo lee para mostrarlo y enviarlo.
 *
 * Es un singleton inyectable, igual que [SessionManager].
 */
@Singleton
class PedidoEnProgreso @Inject constructor() {

    var borrador: BorradorPedido? = null

    fun establecer(borrador: BorradorPedido) {
        this.borrador = borrador
    }

    fun limpiar() {
        borrador = null
    }
}
