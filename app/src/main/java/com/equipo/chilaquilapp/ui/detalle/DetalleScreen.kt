package com.equipo.chilaquilapp.ui.detalle

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.equipo.chilaquilapp.domain.model.Extra
import com.equipo.chilaquilapp.domain.model.ProductoDetalle
import com.equipo.chilaquilapp.domain.model.Proteina
import com.equipo.chilaquilapp.ui.components.PillButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    productoId: String,
    onConfirmar: () -> Unit,
    onVolver: () -> Unit,
    viewModel: DetalleViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ChilaquilApp",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.85f)
                )
            )
        },
        bottomBar = {
            if (state.producto != null && !state.cargando) {
                BottomPedirBar(
                    total = viewModel.calcularTotal(),
                    onClick = { viewModel.crearPedido(onExito = onConfirmar) },
                    enabled = !state.cargando
                )
            }
        }
    ) { innerPadding ->
        when {
            state.cargando && state.producto == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            state.error != null && state.producto == null -> {
                ErrorContent(
                    error = state.error!!,
                    onReintentar = viewModel::reintentar,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            state.producto != null -> {
                DetalleContent(
                    producto = state.producto!!,
                    proteinasSeleccionadas = state.proteinasSeleccionadas,
                    extrasSeleccionados = state.extrasSeleccionados,
                    cantidad = state.cantidad,
                    onToggleProteina = viewModel::toggleProteina,
                    onToggleExtra = viewModel::toggleExtra,
                    onIncrementar = viewModel::incrementarCantidad,
                    onDecrementar = viewModel::decrementarCantidad,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun DetalleContent(
    producto: ProductoDetalle,
    proteinasSeleccionadas: Set<Int>,
    extrasSeleccionados: Set<Int>,
    cantidad: Int,
    onToggleProteina: (Int) -> Unit,
    onToggleExtra: (Int) -> Unit,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Hero Image con badge de precio ──
        HeroImageSection(
            imageName = producto.imagen,
            precio = producto.precio
        )

        // ── Info del producto ──
        ProductInfoCard(
            nombre = producto.nombre,
            descripcion = producto.descripcion
        )

        Spacer(Modifier.height(16.dp))

        // ── Personalización ──
        PersonalizacionSection(
            proteinas = producto.proteinas,
            extras = producto.extras,
            proteinasSeleccionadas = proteinasSeleccionadas,
            extrasSeleccionados = extrasSeleccionados,
            onToggleProteina = onToggleProteina,
            onToggleExtra = onToggleExtra
        )

        Spacer(Modifier.height(8.dp))

        // ── Selector de cantidad ──
        CantidadSelector(
            cantidad = cantidad,
            onIncrementar = onIncrementar,
            onDecrementar = onDecrementar
        )

        // Espacio para el botón fijo inferior
        Spacer(Modifier.height(100.dp))
    }
}

@Composable
private fun HeroImageSection(imageName: String, precio: Double) {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
    val painter = if (resId != 0) {
        painterResource(id = resId)
    } else {
        painterResource(id = android.R.drawable.ic_menu_gallery)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        // Imagen
        androidx.compose.foundation.Image(
            painter = painter,
            contentDescription = "Foto del producto",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradiente oscuro inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
        )

        // Badge de precio
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp)
                .shadow(12.dp, CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = CircleShape
                )
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Text(
                text = "$${java.lang.String.format("%.2f", precio)}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProductInfoCard(nombre: String, descripcion: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 0.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PersonalizacionSection(
    proteinas: List<Proteina>,
    extras: List<Extra>,
    proteinasSeleccionadas: Set<Int>,
    extrasSeleccionados: Set<Int>,
    onToggleProteina: (Int) -> Unit,
    onToggleExtra: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Personaliza tu plato",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            )
            Spacer(Modifier.height(20.dp))

            // ── Proteínas ──
            if (proteinas.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Proteínas (Selección múltiple)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Opcional",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                proteinas.forEach { proteina ->
                    val isSelected = proteina.id in proteinasSeleccionadas
                    ProteinaItem(
                        proteina = proteina,
                        isSelected = isSelected,
                        onToggle = { onToggleProteina(proteina.id) }
                    )
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(20.dp))
            }

            // ── Extras / Toppings ──
            if (extras.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Toppings Incluidos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Gratis",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    extras.forEach { extra ->
                        val isSelected = extra.id in extrasSeleccionados
                        ExtraChip(
                            extra = extra,
                            isSelected = isSelected,
                            onToggle = { onToggleExtra(extra.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProteinaItem(
    proteina: Proteina,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(200),
        label = "proteinaBg"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                uncheckedColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = proteina.nombre,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ExtraChip(
    extra: Extra,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(200),
        label = "extraBg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(200),
        label = "extraBorder"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                uncheckedColor = MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = extra.nombre,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun CantidadSelector(
    cantidad: Int,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(150),
        label = "qtyScale"
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        // Botón menos
        IconButton(
            onClick = onDecrementar,
            modifier = Modifier
                .size(52.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Reducir cantidad",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.width(32.dp))

        // Cantidad
        Text(
            text = "$cantidad",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.scale(scale)
        )

        Spacer(Modifier.width(32.dp))

        // Botón más
        IconButton(
            onClick = onIncrementar,
            modifier = Modifier
                .size(52.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Aumentar cantidad",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun BottomPedirBar(
    total: Double,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.95f)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        PillButton(
            text = "Pedir — $${ java.lang.String.format("%.2f", total) }",
            onClick = onClick,
            enabled = enabled,
            icon = Icons.Default.ShoppingBag
        )
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "😕",
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        PillButton(
            text = "Reintentar",
            onClick = onReintentar,
            icon = null
        )
    }
}
