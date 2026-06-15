package com.equipo.chilaquilapp.ui.login

import androidx.compose.runtime.Composable
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PillOutlinedButton
import com.equipo.chilaquilapp.ui.components.PlaceholderScreen

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    PlaceholderScreen(
        title = "Iniciar sesión",
        description = "Pantalla de inicio de sesión (placeholder)."
    ) {
        PillButton(text = "Entrar", onClick = onLoginExitoso)
        PillOutlinedButton(text = "Crear cuenta", onClick = onIrARegistro)
    }
}
