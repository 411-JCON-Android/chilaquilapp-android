package com.equipo.chilaquilapp.ui.menu

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun MenuScreen(
    onProductoClick: (productoId: String) -> Unit,
    onVerHistorial: () -> Unit
) {
    PlaceholderScreen(
        title = "Menú",
        description = "Catálogo de productos (placeholder)."
    ) {
        PillButton(text = "Ver producto de ejemplo", onClick = { onProductoClick("1") })
        PillOutlinedButton(text = "Ver historial de pedidos", onClick = onVerHistorial)
    }
}
