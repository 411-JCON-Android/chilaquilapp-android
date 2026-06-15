package com.equipo.chilaquilapp.ui.historial

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun HistorialScreen(
    onPedidoClick: (pedidoId: String) -> Unit,
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        title = "Mis pedidos",
        description = "Historial de pedidos del usuario (placeholder)."
    ) {
        PillButton(text = "Ver pedido de ejemplo", onClick = { onPedidoClick("1") })
        PillOutlinedButton(text = "Volver al menú", onClick = onVolver)
    }
}
