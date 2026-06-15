package com.equipo.chilaquilapp.ui.confirmado

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun ConfirmadoScreen(
    onVerHistorial: () -> Unit,
    onVolverAlMenu: () -> Unit
) {
    PlaceholderScreen(
        title = "¡Pedido confirmado!",
        description = "Pantalla de confirmación del pedido (placeholder)."
    ) {
        PillButton(text = "Ver mis pedidos", onClick = onVerHistorial)
        PillOutlinedButton(text = "Volver al menú", onClick = onVolverAlMenu)
    }
}
