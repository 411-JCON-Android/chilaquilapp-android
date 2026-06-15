package com.equipo.chilaquilapp.ui.confirmar

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun ConfirmarScreen(
    onConfirmar: () -> Unit,
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        title = "Confirmar pedido",
        description = "Resumen del pedido antes de confirmarlo (placeholder)."
    ) {
        PillButton(text = "Confirmar pedido", onClick = onConfirmar)
        PillOutlinedButton(text = "Volver", onClick = onVolver)
    }
}
