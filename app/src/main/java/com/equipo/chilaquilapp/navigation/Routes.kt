package com.equipo.chilaquilapp.navigation

/**
 * Rutas de navegación de la app, según el contrato de pantallas de la Fase 1.
 */
sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Registro : Routes("registro")
    object Menu : Routes("menu")
    object Confirmar : Routes("confirmar")
    object Confirmado : Routes("confirmado")
    object Historial : Routes("historial")

    object Detalle : Routes("detalle/{productoId}") {
        const val ARG_PRODUCTO_ID = "productoId"
        fun createRoute(productoId: String) = "detalle/$productoId"
    }

    object DetallePedido : Routes("detallePedido/{pedidoId}") {
        const val ARG_PEDIDO_ID = "pedidoId"
        fun createRoute(pedidoId: String) = "detallePedido/$pedidoId"
    }
}
