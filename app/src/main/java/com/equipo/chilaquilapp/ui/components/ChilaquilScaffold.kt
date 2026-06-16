package com.equipo.chilaquilapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Color de la barra superior (casi negro), como en los mockups de Stitch. */
private val TopBarDark = Color(0xFF121212)

/**
 * Barra superior reutilizable con la marca "ChilaquilApp" centrada sobre fondo
 * oscuro. A la izquierda muestra el ícono de menú o, si se pasa [onBack], una
 * flecha para volver. El ícono de carrito de los mockups se incluye de forma
 * decorativa (el plan no contempla carrito de varios productos).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChilaquilTopBar(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "ChilaquilApp",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.RestaurantMenu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TopBarDark
        )
    )
}

/** Pestañas de la barra de navegación inferior. */
enum class NavInferior { MENU, HISTORIAL, PERFIL }

/**
 * Barra de navegación inferior con las pestañas Menú, Historial y Perfil, como
 * en los mockups. La pestaña activa se indica con la píldora de color del tema.
 * "Perfil" no tiene pantalla en el plan, así que por defecto no hace nada.
 */
@Composable
fun ChilaquilBottomBar(
    seleccionado: NavInferior,
    onMenu: () -> Unit,
    onHistorial: () -> Unit,
    modifier: Modifier = Modifier,
    onPerfil: () -> Unit = {}
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        val colores = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        NavigationBarItem(
            selected = seleccionado == NavInferior.MENU,
            onClick = onMenu,
            icon = { Icon(Icons.Filled.LunchDining, contentDescription = null) },
            label = { Text("Menú") },
            colors = colores
        )
        NavigationBarItem(
            selected = seleccionado == NavInferior.HISTORIAL,
            onClick = onHistorial,
            icon = { Icon(Icons.Filled.History, contentDescription = null) },
            label = { Text("Historial") },
            colors = colores
        )
        NavigationBarItem(
            selected = seleccionado == NavInferior.PERFIL,
            onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            label = { Text("Perfil") },
            colors = colores
        )
    }
}
