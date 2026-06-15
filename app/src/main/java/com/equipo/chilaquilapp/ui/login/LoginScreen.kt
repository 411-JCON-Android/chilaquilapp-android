package com.equipo.chilaquilapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.chilaquilapp.ui.components.ChilaquilTextField
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.theme.BrownDark
import com.equipo.chilaquilapp.ui.theme.BrownDarkest
import com.equipo.chilaquilapp.ui.theme.ChilaquilAppTheme
import com.equipo.chilaquilapp.ui.theme.OliveDarkest

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) onLoginExitoso()
    }

    LoginContent(
        uiState = uiState,
        onNombreUsuarioChange = viewModel::onNombreUsuarioChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        onEntrarClick = viewModel::iniciarSesion,
        onIrARegistro = onIrARegistro
    )
}

/**
 * UI sin estado de la pantalla de login, para poder previsualizarla con
 * `@Preview` sin depender del ViewModel.
 */
@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onNombreUsuarioChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onEntrarClick: () -> Unit,
    onIrARegistro: () -> Unit
) {
    // Fondo cálido que evoca la foto de chilaquiles del mockup. Si más adelante
    // se agrega un recurso de imagen para el hero, puede sustituir este degradado.
    val fondo = Brush.verticalGradient(listOf(BrownDark, BrownDarkest, OliveDarkest))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 16.dp,
                modifier = Modifier.widthIn(max = 420.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "ChilaquilApp",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Sabor auténtico a un clic de distancia",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    ChilaquilTextField(
                        value = uiState.nombreUsuario,
                        onValueChange = onNombreUsuarioChange,
                        label = "Usuario",
                        leadingIcon = Icons.Filled.Person
                    )
                    ChilaquilTextField(
                        value = uiState.contrasena,
                        onValueChange = onContrasenaChange,
                        label = "Contraseña",
                        leadingIcon = Icons.Filled.Lock,
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )

                    // Mensaje de error (credenciales inválidas, fallo de red, etc.)
                    if (uiState.error != null) {
                        Text(
                            text = uiState.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    PillButton(
                        text = if (uiState.cargando) "Entrando…" else "Entrar",
                        onClick = onEntrarClick,
                        enabled = !uiState.cargando
                    )

                    EnlaceRegistro(onClick = onIrARegistro)
                }
            }

            Text(
                text = "Hecho con amor en México 🇲🇽",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun EnlaceRegistro(onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "¿No tienes cuenta? ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Regístrate",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun LoginContentPreview() {
    ChilaquilAppTheme {
        LoginContent(
            uiState = LoginUiState(nombreUsuario = "juancarlos"),
            onNombreUsuarioChange = {},
            onContrasenaChange = {},
            onEntrarClick = {},
            onIrARegistro = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun LoginContentErrorPreview() {
    ChilaquilAppTheme {
        LoginContent(
            uiState = LoginUiState(
                nombreUsuario = "juancarlos",
                contrasena = "1234",
                error = "Usuario o contraseña incorrectos"
            ),
            onNombreUsuarioChange = {},
            onContrasenaChange = {},
            onEntrarClick = {},
            onIrARegistro = {}
        )
    }
}
