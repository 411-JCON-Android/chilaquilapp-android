package com.equipo.chilaquilapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Imagen de un platillo resuelta **por su nombre** de recurso drawable, tal como
 * lo define el plan (la BD solo guarda el nombre, ej. `chilaquiles_rojos`, y la
 * app lo convierte en la imagen local). Sección 3 y supuesto #4.
 *
 * Si el drawable todavía no existe en el proyecto (por ejemplo durante el
 * desarrollo, antes de cargar las fotos del menú) se muestra un marcador de
 * posición con un ícono, en lugar de fallar.
 */
@Composable
fun FoodImage(
    nombreImagen: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val drawableId = remember(nombreImagen) {
        if (nombreImagen.isBlank()) {
            0
        } else {
            context.resources.getIdentifier(nombreImagen, "drawable", context.packageName)
        }
    }

    if (drawableId != 0) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        // Marcador de posición cuando aún no hay foto local para este producto.
        val degradado = Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                MaterialTheme.colorScheme.surfaceContainerHighest
            )
        )
        Box(
            modifier = modifier.background(degradado),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.RestaurantMenu,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxSize(fraction = 0.25f)
            )
        }
    }
}
