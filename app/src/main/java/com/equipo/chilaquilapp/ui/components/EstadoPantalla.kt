package com.equipo.chilaquilapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Indicador de carga centrado, reutilizable en todas las pantallas. */
@Composable
fun CargandoIndicador(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
    }
}

/**
 * Estado de error centrado, con un mensaje y un botón opcional para reintentar.
 * Unifica el manejo de errores de red/servidor en toda la app.
 */
@Composable
fun ErrorEstado(
    mensaje: String,
    modifier: Modifier = Modifier,
    onReintentar: (() -> Unit)? = null
) {
    EstadoCentrado(
        icono = Icons.Filled.ErrorOutline,
        titulo = "Algo salió mal",
        descripcion = mensaje,
        modifier = modifier,
        textoBoton = if (onReintentar != null) "Reintentar" else null,
        onBoton = onReintentar
    )
}

/** Estado genérico centrado (vacío o error): ícono + título + texto + botón. */
@Composable
fun EstadoCentrado(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    textoBoton: String? = null,
    onBoton: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (textoBoton != null && onBoton != null) {
            PillButton(
                text = textoBoton,
                onClick = onBoton,
                icon = null,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}
