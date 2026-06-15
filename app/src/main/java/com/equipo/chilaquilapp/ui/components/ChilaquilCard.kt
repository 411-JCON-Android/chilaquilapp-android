package com.equipo.chilaquilapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.chilaquilapp.ui.theme.ChilaquilAppTheme

/**
 * Tarjeta blanca con esquinas muy redondeadas (1.5rem), usada para tarjetas
 * de producto, resúmenes de pedido y contenedores de formulario.
 */
@Composable
fun ChilaquilCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChilaquilCardPreview() {
    ChilaquilAppTheme {
        Surface {
            ChilaquilCard(modifier = Modifier.padding(16.dp)) {
                Text(text = "Chilaquiles Rojos", style = MaterialTheme.typography.titleMedium)
                Text(text = "Crujientes totopos bañados en salsa roja", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$65.00", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
