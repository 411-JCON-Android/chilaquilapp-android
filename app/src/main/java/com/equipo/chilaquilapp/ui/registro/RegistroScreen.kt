package com.equipo.chilaquilapp.ui.registro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import com.equipo.chilaquilapp.ui.components.ChilaquilCard
import com.equipo.chilaquilapp.ui.components.ChilaquilTextField
import com.equipo.chilaquilapp.ui.components.PillButton

/**
 * Pantalla "Crear cuenta": formulario de registro dentro de una [ChilaquilCard],
 * con los campos del sistema de diseño y el botón principal en forma de píldora.
 * El estado vive en [RegistroViewModel]; la pantalla solo lo refleja y dispara
 * los callbacks de navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onVolverClick: () -> Unit,
    viewModel: RegistroViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Cuando el registro es exitoso, navegamos una sola vez.
    LaunchedEffect(uiState.estado) {
        if (uiState.estado is RegistroEstado.Success) {
            onRegistroExitoso()
        }
    }

    val cargando = uiState.estado is RegistroEstado.Loading
    val mensajeError = (uiState.estado as? RegistroEstado.Error)?.mensaje

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Restaurant,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                        Text(
                            text = "ChilaquilApp",
                            color = MaterialTheme.colorScheme.primaryContainer,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1B1B1B)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            ChilaquilCard {
                Text(
                    text = "¡Bienvenido a la familia!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Regístrate para empezar a disfrutar de los mejores chilaquiles.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ChilaquilTextField(
                        value = uiState.nombre,
                        onValueChange = viewModel::onNombreChange,
                        label = "Nombre",
                        leadingIcon = Icons.Filled.Person
                    )
                    ChilaquilTextField(
                        value = uiState.nombreUsuario,
                        onValueChange = viewModel::onNombreUsuarioChange,
                        label = "Nombre de usuario",
                        leadingIcon = Icons.Filled.AlternateEmail
                    )
                    ChilaquilTextField(
                        value = uiState.contrasena,
                        onValueChange = viewModel::onContrasenaChange,
                        label = "Contraseña",
                        leadingIcon = Icons.Filled.Lock,
                        isPassword = true
                    )
                    ChilaquilTextField(
                        value = uiState.direccion,
                        onValueChange = viewModel::onDireccionChange,
                        label = "Dirección",
                        leadingIcon = Icons.Filled.LocationOn,
                        keyboardType = KeyboardType.Text
                    )
                }

                if (mensajeError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = mensajeError,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                PillButton(
                    text = "Crear cuenta",
                    onClick = viewModel::registrarUsuario,
                    loading = cargando
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Inicia sesión aquí",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.clickable(enabled = !cargando) { onVolverClick() }
                    )
                }
            }
        }
    }
}
