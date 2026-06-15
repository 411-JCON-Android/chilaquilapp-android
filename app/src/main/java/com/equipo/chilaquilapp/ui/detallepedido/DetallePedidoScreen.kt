package com.equipo.chilaquilapp.ui.detallepedido

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun DetallePedidoScreen(
    pedidoId: String,
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        title = "Detalle del pedido",
        description = "Detalle del pedido $pedidoId (placeholder)."
    ) {
        PillOutlinedButton(text = "Volver al historial", onClick = onVolver)
    }
}
