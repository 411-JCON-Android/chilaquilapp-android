package com.equipo.chilaquilapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.equipo.chilaquilapp.util.formatearPrecio

/**
 * Etiqueta de precio en forma de píldora, como en los mockups. Por defecto usa
 * el color "spiced orange" (acción); con [contenedor] = true usa el tono más
 * claro del contenedor, como las etiquetas del historial.
 */
@Composable
fun PriceBadge(
    precio: Double,
    modifier: Modifier = Modifier,
    contenedor: Boolean = false
) {
    val fondo = if (contenedor) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.secondary
    }
    val texto = if (contenedor) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondary
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 50),
        color = fondo,
        contentColor = texto,
        shadowElevation = 6.dp
    ) {
        Text(
            text = formatearPrecio(precio),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
