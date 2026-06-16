package com.equipo.chilaquilapp.ui.confirmar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.chilaquilapp.data.session.BorradorPedido
import com.equipo.chilaquilapp.domain.model.Extra
import com.equipo.chilaquilapp.domain.model.ProductoDetalle
import com.equipo.chilaquilapp.domain.model.Proteina
import com.equipo.chilaquilapp.ui.components.ChilaquilTextField
import com.equipo.chilaquilapp.ui.components.ChilaquilTopBar
import com.equipo.chilaquilapp.ui.components.EstadoCentrado
import com.equipo.chilaquilapp.ui.components.FoodImage
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PriceBadge
import com.equipo.chilaquilapp.ui.theme.ChilaquilAppTheme
import com.equipo.chilaquilapp.util.formatearPrecio

@Composable
fun ConfirmarScreen(
    onConfirmar: () -> Unit,
    onVolver: () -> Unit,
    viewModel: ConfirmarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.pedidoEnviado) {
        if (uiState.pedidoEnviado) onConfirmar()
    }

    ConfirmarContent(
        uiState = uiState,
        onDireccionChange = viewModel::onDireccionChange,
        onToggleEditarDireccion = viewModel::toggleEditarDireccion,
        onEnviarPedido = viewModel::enviarPedido,
        onVolver = onVolver
    )
}

/** UI sin estado de la pantalla de confirmar pedido, para previsualizar. */
@Composable
private fun ConfirmarContent(
    uiState: ConfirmarUiState,
    onDireccionChange: (String) -> Unit,
    onToggleEditarDireccion: () -> Unit,
    onEnviarPedido: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = { ChilaquilTopBar(onBack = onVolver) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        val borrador = uiState.borrador
        if (borrador == null) {
            EstadoCentrado(
                icono = Icons.Filled.Restaurant,
                titulo = "No hay un pedido en proceso",
                descripcion = "Vuelve al menú y elige tus chilaquiles para personalizarlos.",
                textoBoton = "Ir al menú",
                onBoton = onVolver,
                modifier = Modifier.padding(innerPadding)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Hero()

            ResumenProductoCard(borrador = borrador)

            DireccionCard(
                direccion = uiState.direccion,
                editando = uiState.editandoDireccion,
                onDireccionChange = onDireccionChange,
                onToggleEditar = onToggleEditarDireccion
            )

            TotalCard(total = borrador.total)

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
                text = if (uiState.enviando) "Enviando…" else "Enviar pedido",
                onClick = onEnviarPedido,
                enabled = !uiState.enviando,
                loading = uiState.enviando,
                icon = Icons.Filled.ShoppingCartCheckout
            )
        }
    }
}

@Composable
private fun Hero() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Text(
            text = "Confirma tu festín",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Revisa tu creación antes de prender el fuego.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ResumenProductoCard(borrador: BorradorPedido) {
    TarjetaBlanca {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = borrador.producto.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Cantidad: ${borrador.cantidad}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            PriceBadge(precio = borrador.producto.precio)
        }

        if (borrador.proteinas.isNotEmpty()) {
            SeccionIngredientes(
                icono = Icons.Filled.Egg,
                titulo = "Proteínas",
                items = borrador.proteinas.map { it.nombre },
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        if (borrador.extras.isNotEmpty()) {
            SeccionIngredientes(
                icono = Icons.Filled.AddCircle,
                titulo = "Extras",
                items = borrador.extras.map { it.nombre },
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        FoodImage(
            nombreImagen = borrador.producto.imagen,
            contentDescription = borrador.producto.nombre,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun SeccionIngredientes(
    icono: ImageVector,
    titulo: String,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            items.forEach { nombre ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    )
                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DireccionCard(
    direccion: String,
    editando: Boolean,
    onDireccionChange: (String) -> Unit,
    onToggleEditar: () -> Unit
) {
    TarjetaBlanca {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Dirección de entrega",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            TextButton(onClick = onToggleEditar) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = if (editando) "Listo" else "Editar",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        if (editando) {
            ChilaquilTextField(
                value = direccion,
                onValueChange = onDireccionChange,
                label = "Dirección",
                leadingIcon = Icons.Filled.LocationOn,
                modifier = Modifier.padding(top = 12.dp)
            )
        } else {
            Surface(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Text(
                    text = direccion.ifBlank { "Sin dirección. Toca \"Editar\" para agregar una." },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun TotalCard(total: Double) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total", style = MaterialTheme.typography.headlineMedium)
                Text(text = formatearPrecio(total), style = MaterialTheme.typography.headlineLarge)
            }
            Text(
                text = "El pago se acuerda directo con el negocio.",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/** Tarjeta blanca con esquinas redondeadas y borde sutil, base de las secciones. */
@Composable
private fun TarjetaBlanca(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) { content() }
    }
}

@Preview(showBackground = true, heightDp = 1100)
@Composable
private fun ConfirmarContentPreview() {
    ChilaquilAppTheme {
        ConfirmarContent(
            uiState = ConfirmarUiState(
                borrador = BorradorPedido(
                    producto = ProductoDetalle(
                        id = 1,
                        nombre = "Chilaquiles Tradicionales",
                        descripcion = "Crujientes totopos en salsa roja.",
                        precio = 65.0,
                        imagen = "chilaquiles_rojos",
                        salsa = "roja",
                        proteinas = emptyList(),
                        extras = emptyList()
                    ),
                    cantidad = 1,
                    proteinas = listOf(Proteina(1, "Pollo deshebrado"), Proteina(2, "Huevo estrellado")),
                    extras = listOf(Extra(1, "Aguacate extra"), Extra(2, "Crema"))
                ),
                direccion = "Av. Reforma 222, Cuauhtémoc, CDMX"
            ),
            onDireccionChange = {},
            onToggleEditarDireccion = {},
            onEnviarPedido = {},
            onVolver = {}
        )
    }
}
