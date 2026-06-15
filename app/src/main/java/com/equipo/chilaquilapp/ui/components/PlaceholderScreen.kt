package com.equipo.chilaquilapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Estructura base para las pantallas placeholder de la Fase 0: título,
 * descripción y acciones de navegación. Cada equipo reemplazará el contenido
 * de su pantalla en la Fase 1 sin tocar [com.equipo.chilaquilapp.navigation.NavGraph].
 */
@Composable
fun PlaceholderScreen(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit = {}
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                actions()
            }
        }
    }
}
