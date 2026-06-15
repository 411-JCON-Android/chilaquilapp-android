package com.equipo.chilaquilapp.ui.detalle

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun DetalleScreen(
    productoId: String,
    onConfirmar: () -> Unit,
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        title = "Detalle del producto",
        description = "Detalle y personalización del producto $productoId (placeholder)."
    ) {
        PillButton(text = "Agregar al pedido", onClick = onConfirmar)
        PillOutlinedButton(text = "Volver al menú", onClick = onVolver)
    }
}
