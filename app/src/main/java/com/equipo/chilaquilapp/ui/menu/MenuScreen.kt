package com.equipo.chilaquilapp.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.chilaquilapp.domain.model.Producto
import com.equipo.chilaquilapp.ui.components.CargandoIndicador
import com.equipo.chilaquilapp.ui.components.ChilaquilBottomBar
import com.equipo.chilaquilapp.ui.components.ChilaquilTopBar
import com.equipo.chilaquilapp.ui.components.ErrorEstado
import com.equipo.chilaquilapp.ui.components.FoodImage
import com.equipo.chilaquilapp.ui.components.NavInferior
import com.equipo.chilaquilapp.ui.components.PillButton
import com.equipo.chilaquilapp.ui.components.PriceBadge
import com.equipo.chilaquilapp.ui.theme.ChilaquilAppTheme

@Composable
fun MenuScreen(
    onProductoClick: (productoId: String) -> Unit,
    onVerHistorial: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuContent(
        uiState = uiState,
        onProductoClick = onProductoClick,
        onVerHistorial = onVerHistorial,
        onReintentar = viewModel::cargarProductos
    )
}

/** UI sin estado de la pantalla de menú, para previsualizar con `@Preview`. */
@Composable
private fun MenuContent(
    uiState: MenuUiState,
    onProductoClick: (productoId: String) -> Unit,
    onVerHistorial: () -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = { ChilaquilTopBar() },
        bottomBar = {
            ChilaquilBottomBar(
                seleccionado = NavInferior.MENU,
                onMenu = {},
                onHistorial = onVerHistorial
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

                else -> ListaProductos(
                    productos = uiState.productos,
                    onProductoClick = onProductoClick
                )
            }
        }
    }
}

@Composable
private fun ListaProductos(
    productos: List<Producto>,
    onProductoClick: (productoId: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "La mejor forma de empezar tu día",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Ingredientes frescos, auténtico sabor con chile.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        items(items = productos, key = { it.id }) { producto ->
            ProductoCard(producto = producto, onClick = { onProductoClick(producto.id.toString()) })
        }
    }
}

@Composable
private fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            FoodImage(
                nombreImagen = producto.imagen,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
            PriceBadge(
                precio = producto.precio,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            PillButton(
                text = "Pedir ahora",
                onClick = onClick,
                icon = null,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun MenuContentPreview() {
    ChilaquilAppTheme {
        MenuContent(
            uiState = MenuUiState(
                cargando = false,
                productos = listOf(
                    Producto(1, "Chilaquiles Rojos", "Crujientes totopos en salsa roja de guajillo con crema, queso y cebolla.", 65.0, "chilaquiles_rojos", "roja"),
                    Producto(2, "Chilaquiles Verdes", "Salsa de tomatillo y serrano con pollo deshebrado y aguacate.", 70.0, "chilaquiles_verdes", "verde")
                )
            ),
            onProductoClick = {},
            onVerHistorial = {},
            onReintentar = {}
        )
    }
}
