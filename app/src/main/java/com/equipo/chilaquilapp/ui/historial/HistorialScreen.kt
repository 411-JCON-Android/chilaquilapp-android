package com.equipo.chilaquilapp.ui.historial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.chilaquilapp.domain.model.PedidoResumen
import com.equipo.chilaquilapp.ui.components.CargandoIndicador
import com.equipo.chilaquilapp.ui.components.ChilaquilBottomBar
import com.equipo.chilaquilapp.ui.components.ChilaquilTopBar
import com.equipo.chilaquilapp.ui.components.ErrorEstado
import com.equipo.chilaquilapp.ui.components.EstadoCentrado
import com.equipo.chilaquilapp.ui.components.FoodImage
import com.equipo.chilaquilapp.ui.components.NavInferior
import com.equipo.chilaquilapp.ui.components.PriceBadge
import com.equipo.chilaquilapp.ui.theme.ChilaquilAppTheme
import com.equipo.chilaquilapp.util.formatearFecha

@Composable
fun HistorialScreen(
    onPedidoClick: (pedidoId: String) -> Unit,
    onVolver: () -> Unit,
    viewModel: HistorialViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistorialContent(
        uiState = uiState,
        onPedidoClick = onPedidoClick,
        onVolver = onVolver,
        onReintentar = viewModel::cargarHistorial
    )
}

/** UI sin estado de la pantalla de historial, para previsualizar con `@Preview`. */
@Composable
private fun HistorialContent(
    uiState: HistorialUiState,
    onPedidoClick: (pedidoId: String) -> Unit,
    onVolver: () -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = { ChilaquilTopBar() },
        bottomBar = {
            ChilaquilBottomBar(
                seleccionado = NavInferior.HISTORIAL,
                onMenu = onVolver,
                onHistorial = {}
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            when {
                uiState.cargando -> CargandoIndicador()

                uiState.error != null -> ErrorEstado(
                    mensaje = uiState.error,
                    onReintentar = onReintentar
                )

                uiState.pedidos.isEmpty() -> EstadoCentrado(
                    icono = Icons.Filled.ReceiptLong,
                    titulo = "Aún no tienes pedidos",
                    descripcion = "¿Con hambre? Tu próximo platillo está a un toque.",
                    textoBoton = "Ver el menú",
                    onBoton = onVolver
                )

                else -> ListaHistorial(
                    pedidos = uiState.pedidos,
                    onPedidoClick = onPedidoClick
                )
            }
        }
    }
}

@Composable
private fun ListaHistorial(
    pedidos: List<PedidoResumen>,
    onPedidoClick: (pedidoId: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Historial de pedidos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tu delicioso recorrido con nosotros",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        items(items = pedidos, key = { it.id }) { pedido ->
            PedidoCard(pedido = pedido, onClick = { onPedidoClick(pedido.id.toString()) })
        }
    }
}

@Composable
private fun PedidoCard(
    pedido: PedidoResumen,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FoodImage(
                nombreImagen = "",
                contentDescription = pedido.productoNombre,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = pedido.productoNombre,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    PriceBadge(precio = pedido.total, contenedor = true)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = formatearFecha(pedido.creadoEn),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    EstadoChip(texto = "Recibido")
                    Text(
                        text = "Cantidad: ${pedido.cantidad}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Chip de estado del pedido. El historial siempre muestra "Recibido" porque,
 * según el plan, el dueño actualiza el estado por fuera de la app (supuesto #7)
 * y el contrato de historial no devuelve el estado.
 */
@Composable
private fun EstadoChip(texto: String) {
    Surface(
        shape = RoundedCornerShape(percent = 50),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            text = texto.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun HistorialContentPreview() {
    ChilaquilAppTheme {
        HistorialContent(
            uiState = HistorialUiState(
                cargando = false,
                pedidos = listOf(
                    PedidoResumen(10, "Chilaquiles Rojos", 2, 130.0, "2026-05-24T10:15:00Z"),
                    PedidoResumen(11, "Chilaquiles Verdes", 1, 70.0, "2026-05-18T13:45:00Z")
                )
            ),
            onPedidoClick = {},
            onVolver = {},
            onReintentar = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 700, name = "Historial vacío")
@Composable
private fun HistorialVacioPreview() {
    ChilaquilAppTheme {
        HistorialContent(
            uiState = HistorialUiState(cargando = false, pedidos = emptyList()),
            onPedidoClick = {},
            onVolver = {},
            onReintentar = {}
        )
    }
}
