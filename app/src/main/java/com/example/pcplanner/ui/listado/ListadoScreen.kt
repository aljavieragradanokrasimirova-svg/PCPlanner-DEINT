package com.example.pcplanner.ui.listado

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pcplanner.model.*
import com.example.pcplanner.ui.components.*
import com.example.pcplanner.ui.theme.PlannerTheme

data class ListadoState(val equipos: List<Equipo> = equiposEjemplo)

/** Prepara la colección ficticia del listado.
 * @param modifier Espacio disponible para la pantalla.
 */
@Composable
fun ListadoScreen(modifier: Modifier = Modifier) {
    val state by remember { mutableStateOf(ListadoState()) }
    ListadoContent(state, modifier)
}

/** Representa una colección de equipos y el caso vacío.
 * @param state Datos que debe mostrar.
 * @param modifier Ajustes del contenedor.
 */
@Composable
fun ListadoContent(state: ListadoState, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { ScreenHeader("Mis equipos", "03 / Tu inventario de hardware", Icons.Default.Devices) }
        item { Text("${state.equipos.size} equipos registrados", style = MaterialTheme.typography.titleMedium) }
        if (state.equipos.isEmpty()) {
            item { OutlinedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.Computer, null, Modifier.size(48.dp))
                Text("Todavía no hay equipos", style = MaterialTheme.typography.titleLarge)
                Text("El inventario mostrará aquí los equipos que registres.")
            } } }
        }
        items(state.equipos, key = { it.id }) { EquipoCard(it) }
    }
}

/** Tarjeta reutilizable de un equipo.
 * @param equipo Registro con nombre, configuración, fecha y usos.
 */
@Composable
fun EquipoCard(equipo: Equipo) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(52.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Computer, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }
                Column(Modifier.weight(1f)) {
                    Text(equipo.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(if (equipo.activo) "Activo" else "En reserva", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                }
            }
            Text(equipo.observaciones, style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, null, Modifier.size(18.dp))
                Text(fechaTexto(equipo.fechaRegistro), style = MaterialTheme.typography.bodySmall)
            }
            Text(equipo.usos.joinToString(" · "), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 900)
@Preview(showBackground = true, widthDp = 390, heightDp = 900, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListadoPreview() { PlannerTheme { Surface { ListadoContent(ListadoState()) } } }

@Preview(showBackground = true, widthDp = 320)
@Preview(showBackground = true, widthDp = 420, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EquipoCardPreview() { PlannerTheme { Surface { EquipoCard(equiposEjemplo.first()) } } }

@Preview(showBackground = true, widthDp = 360, heightDp = 500)
@Composable
fun ListadoVacioPreview() { PlannerTheme { Surface { ListadoContent(ListadoState(emptyList())) } } }
