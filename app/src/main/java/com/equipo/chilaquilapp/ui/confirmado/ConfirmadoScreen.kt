package com.equipo.chilaquilapp.ui.confirmado

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores del sistema de diseño
val PrimaryColor = Color(0xFF6F5D00)
val SecondaryColor = Color(0xFF8A5100)
val BackgroundColor = Color(0xFFF9F9F9)
val OnSecondaryColor = Color(0xFFFFFFFF)
val PrimaryContainerColor = Color(0xFFEDCB34)
val OnPrimaryContainerColor = Color(0xFF675600)
val OnSurfaceVariantColor = Color(0xFF4C4734)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmadoScreen(
    // ⚠️ Aquí están los dos parámetros que exige tu NavGraph
    onVerHistorial: () -> Unit,
    onVolverAlMenu: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ChilaquilApp",
                        color = PrimaryColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF121212)
                )
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icono de éxito
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(PrimaryContainerColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Pedido Recibido",
                        tint = OnPrimaryContainerColor,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Pedido recibido!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B1B1B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Tu orden ya está en camino a la cocina. Prepárate para disfrutar del auténtico sabor tradicional.",
                    fontSize = 16.sp,
                    color = OnSurfaceVariantColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Botón Principal: Volver al Menú
                Button(
                    onClick = onVolverAlMenu, // Conectado al NavGraph
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryColor,
                        contentColor = OnSecondaryColor
                    ),
                    shape = RoundedCornerShape(9999.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Volver al inicio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Secundario: Ver Historial
                OutlinedButton(
                    onClick = onVerHistorial, // Conectado al NavGraph
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SecondaryColor
                    ),
                    shape = RoundedCornerShape(9999.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Ver historial de pedidos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}