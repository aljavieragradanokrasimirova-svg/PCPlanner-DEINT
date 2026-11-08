package com.example.pcplanner.ui.equipo

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pcplanner.model.Equipo
import com.example.pcplanner.ui.components.*
import com.example.pcplanner.ui.theme.PlannerTheme

data class EquipoState(
    val nombre: String = "", val fecha: Long = 1794096000000,
    val observaciones: String = "", val activo: Boolean = true,
    val usos: Set<String> = emptySet(), val errorNombre: String? = null,
    val errorUsos: String? = null, val guardado: Boolean = false
)
data class EquipoEvents(
    val onNombre: (String) -> Unit, val onObservaciones: (String) -> Unit,
    val onActivo: (Boolean) -> Unit, val onUso: (String) -> Unit
)
data class EquipoActions(val onFecha: () -> Unit, val onGuardar: () -> Unit)

/** Gestiona los datos y validaciones del alta de equipo.
 * @param modifier Ajustes del contenedor.
 * @param onGuardar Recibe el equipo válido. Por defecto permite una demostración local.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipoScreen(modifier: Modifier = Modifier, onGuardar: (Equipo) -> Unit = {}) {
    var state by remember { mutableStateOf(EquipoState()) }
    var mostrarFecha by remember { mutableStateOf(false) }
    val events = EquipoEvents(
        onNombre = { state = state.copy(nombre = it, errorNombre = if (it.isBlank()) "Escribe el nombre del equipo" else null, guardado = false) },
        onObservaciones = { state = state.copy(observaciones = it, guardado = false) },
        onActivo = { state = state.copy(activo = it, guardado = false) },
        onUso = { option ->
            val selected = if (option in state.usos) state.usos - option else state.usos + option
            state = state.copy(usos = selected, errorUsos = if (selected.isEmpty()) "Selecciona al menos un uso" else null, guardado = false)
        }
    )
    val actions = EquipoActions(
        onFecha = { mostrarFecha = true },
        onGuardar = {
            val error = if (state.nombre.isBlank()) "Escribe el nombre del equipo" else null
            val errorUsos = if (state.usos.isEmpty()) "Selecciona al menos un uso" else null
            state = state.copy(errorNombre = error, errorUsos = errorUsos)
            if (error == null && errorUsos == null) {
                onGuardar(Equipo(0, state.nombre.trim(), state.fecha, state.observaciones.trim(), state.activo, state.usos))
                state = state.copy(guardado = true)
            }
        }
    )
    EquipoContent(state, events, actions, modifier)
    if (mostrarFecha) {
        val picker = rememberDatePickerState(initialSelectedDateMillis = state.fecha)
        DatePickerDialog(onDismissRequest = { mostrarFecha = false },
            confirmButton = { TextButton(onClick = {
                picker.selectedDateMillis?.let { state = state.copy(fecha = it, guardado = false) }
                mostrarFecha = false
            }, enabled = picker.selectedDateMillis != null) { Text("Aceptar") } },
            dismissButton = { TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar") } }) {
            DatePicker(state = picker)
        }
    }
}

/** Formulario visual del equipo, con datos y callbacks recibidos.
 * @param state Fotografía de los valores y errores.
 * @param events Cambios de controles.
 * @param actions Apertura de calendario y guardado.
 * @param modifier Ajustes de tamaño y espacio.
 */
@Composable
fun EquipoContent(state: EquipoState, events: EquipoEvents, actions: EquipoActions, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ScreenHeader("Añadir equipo", "01 / Inventario personal", Icons.Default.Computer)
        Text("Datos del equipo", style = MaterialTheme.typography.titleLarge)
        FormField("Nombre del equipo", state.nombre, events.onNombre, Icons.Default.Computer, state.errorNombre, "Ejemplo: PC de escritorio")
        DateField(state.fecha, actions.onFecha)
        FormField("Observaciones y configuración", state.observaciones, events.onObservaciones, Icons.Default.Info, helper = "Procesador, memoria, almacenamiento o notas", multiline = true)
        MultiSelection("Uso del equipo", listOf("Estudio", "Programación", "Gaming", "Diseño"), state.usos, events.onUso)
        state.errorUsos?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        OutlinedCard {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("Equipo activo", style = MaterialTheme.typography.titleMedium); Text("Lo utilizo actualmente", style = MaterialTheme.typography.bodySmall) }
                Switch(state.activo, events.onActivo)
            }
        }
        Button(onClick = actions.onGuardar, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Icon(Icons.Default.Save, null); Spacer(Modifier.width(8.dp)); Text("Guardar equipo")
        }
        if (state.guardado) Text("Equipo validado correctamente en esta demostración.", color = MaterialTheme.colorScheme.primary)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 950)
@Preview(showBackground = true, widthDp = 390, heightDp = 950, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EquipoPreview() {
    PlannerTheme { Surface {
        EquipoContent(EquipoState(nombre = "PC de escritorio", observaciones = "Ryzen 5 · 16 GB de RAM · SSD 1 TB", usos = setOf("Estudio", "Programación")), EquipoEvents({}, {}, {}, {}), EquipoActions({}, {}))
    } }
}
