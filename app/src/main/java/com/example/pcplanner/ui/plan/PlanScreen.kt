package com.example.pcplanner.ui.plan

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pcplanner.model.*
import com.example.pcplanner.ui.components.*
import com.example.pcplanner.ui.theme.PlannerTheme

data class PlanState(val plan: PlanMejora = planEjemplo, val errorNombre: String? = null,
    val errorObjetivo: String? = null, val errorPresupuesto: String? = null,
    val errorCategorias: String? = null, val guardado: Boolean = false)
data class PlanEvents(val onNombre: (String) -> Unit, val onObjetivo: (String) -> Unit,
    val onPresupuesto: (String) -> Unit, val onCompletado: (Boolean) -> Unit,
    val onCategoria: (String) -> Unit, val onEstado: (String) -> Unit)
data class PlanActions(val onFecha: () -> Unit, val onGuardar: () -> Unit)

private fun presupuestoError(text: String): String? {
    val amount = text.replace(',', '.').toBigDecimalOrNull()
    return if (amount == null || amount.signum() <= 0 || amount.scale() > 2) "Introduce un importe positivo con hasta 2 decimales" else null
}

/** Controla la edición local de un plan y su validación.
 * @param modifier Ajustes de presentación.
 * @param initial Datos precargados que se quieren editar.
 * @param onGuardar Recibe una copia válida del plan.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(modifier: Modifier = Modifier, initial: PlanMejora = planEjemplo, onGuardar: (PlanMejora) -> Unit = {}) {
    var state by remember(initial) { mutableStateOf(PlanState(initial)) }
    var mostrarFecha by remember { mutableStateOf(false) }
    val events = PlanEvents(
        onNombre = { state = state.copy(plan = state.plan.copy(nombre = it), errorNombre = if (it.isBlank()) "El nombre es obligatorio" else null, guardado = false) },
        onObjetivo = { state = state.copy(plan = state.plan.copy(objetivo = it), errorObjetivo = if (it.isBlank()) "Describe el objetivo" else null, guardado = false) },
        onPresupuesto = { state = state.copy(plan = state.plan.copy(presupuesto = it), errorPresupuesto = presupuestoError(it), guardado = false) },
        onCompletado = { state = state.copy(plan = state.plan.copy(completado = it, estado = if (it) "Completado" else "Pendiente"), guardado = false) },
        onCategoria = { option ->
            val selected = if (option in state.plan.categorias) state.plan.categorias - option else state.plan.categorias + option
            state = state.copy(plan = state.plan.copy(categorias = selected), errorCategorias = if (selected.isEmpty()) "Elige al menos una categoría" else null, guardado = false)
        },
        onEstado = { state = state.copy(plan = state.plan.copy(estado = it, completado = it == "Completado"), guardado = false) }
    )
    val actions = PlanActions(onFecha = { mostrarFecha = true }, onGuardar = {
        state = state.copy(errorNombre = if (state.plan.nombre.isBlank()) "El nombre es obligatorio" else null,
            errorObjetivo = if (state.plan.objetivo.isBlank()) "Describe el objetivo" else null,
            errorPresupuesto = presupuestoError(state.plan.presupuesto), errorCategorias = if (state.plan.categorias.isEmpty()) "Elige al menos una categoría" else null)
        if (state.errorNombre == null && state.errorObjetivo == null && state.errorPresupuesto == null && state.errorCategorias == null) {
            onGuardar(state.plan.copy(nombre = state.plan.nombre.trim(), presupuesto = state.plan.presupuesto.replace(',', '.')))
            state = state.copy(guardado = true)
        }
    })
    PlanContent(state, events, actions, modifier)
    if (mostrarFecha) {
        val picker = rememberDatePickerState(initialSelectedDateMillis = state.plan.fechaCreacion)
        DatePickerDialog(onDismissRequest = { mostrarFecha = false },
            confirmButton = { TextButton(onClick = {
                picker.selectedDateMillis?.let { state = state.copy(plan = state.plan.copy(fechaCreacion = it), guardado = false) }
                mostrarFecha = false
            }, enabled = picker.selectedDateMillis != null) { Text("Aceptar") } },
            dismissButton = { TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar") } }) { DatePicker(picker) }
    }
}

/** Contenido visual de edición. Todos los cambios se delegan a callbacks.
 * @param state Plan actual y mensajes de validación.
 * @param events Cambios de atributos.
 * @param actions Apertura de fecha y confirmación.
 * @param modifier Ajustes externos del formulario.
 */
@Composable
fun PlanContent(state: PlanState, events: PlanEvents, actions: PlanActions, modifier: Modifier = Modifier) {
    val plan = state.plan
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ScreenHeader("Editar plan", "02 / Plan de mejora #${plan.id}", Icons.Default.Build)
        Text("Equipo asociado · PC de escritorio (#${plan.equipoId})", style = MaterialTheme.typography.titleSmall)
        FormField("Nombre del plan", plan.nombre, events.onNombre, Icons.Default.Edit, state.errorNombre, "Identifica esta mejora")
        FormField("Objetivo", plan.objetivo, events.onObjetivo, Icons.Default.Flag, state.errorObjetivo, "Qué quieres mejorar y para qué", multiline = true)
        FormField("Presupuesto (€)", plan.presupuesto, events.onPresupuesto, Icons.Default.Euro, state.errorPresupuesto, "Importe máximo previsto", keyboard = KeyboardType.Decimal)
        DateField(plan.fechaCreacion, actions.onFecha, label = "Fecha de creación")
        MultiSelection("Componentes que quieres mejorar", listOf("CPU", "GPU", "RAM", "Almacenamiento", "Fuente"), plan.categorias, events.onCategoria)
        state.errorCategorias?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        MultiSelection("Estado del plan", listOf("Pendiente", "En curso", "Completado"), setOf(plan.estado), events.onEstado, multiple = false)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(plan.completado, events.onCompletado)
            Text("Mejora completada")
        }
        Button(onClick = actions.onGuardar, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Icon(Icons.Default.Save, null); Spacer(Modifier.width(8.dp)); Text("Guardar cambios")
        }
        if (state.guardado) Text("Cambios validados en esta demostración.", color = MaterialTheme.colorScheme.primary)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1150)
@Preview(showBackground = true, widthDp = 390, heightDp = 1150, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlanPreview() { PlannerTheme { Surface { PlanContent(PlanState(), PlanEvents({}, {}, {}, {}, {}, {}), PlanActions({}, {})) } } }
