package com.equipo.chilaquilapp.ui.registro

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onVolver: () -> Unit
) {
    PlaceholderScreen(
        title = "Crear cuenta",
        description = "Pantalla de registro de usuario (placeholder)."
    ) {
        PillButton(text = "Registrarme", onClick = onRegistroExitoso)
        PillOutlinedButton(text = "Volver", onClick = onVolver)
    }
}
