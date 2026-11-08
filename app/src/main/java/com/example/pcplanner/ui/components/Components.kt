package com.example.pcplanner.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pcplanner.ui.theme.PlannerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun fechaTexto(value: Long): String = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-ES")).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}.format(Date(value))

/** Cabecera común de pantalla.
 * @param title Título principal.
 * @param subtitle Contexto de la operación.
 * @param icon Símbolo representativo.
 */
@Composable
fun ScreenHeader(title: String, subtitle: String, icon: ImageVector) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.primaryContainer).padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(icon, contentDescription = null, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

/** Campo reutilizable con etiqueta, icono y validación recibida del contenedor.
 * @param label Etiqueta visible.
 * @param value Valor actual.
 * @param onChange Callback que comunica el nuevo texto.
 * @param icon Icono del atributo.
 * @param error Mensaje de validación, o null si es válido.
 * @param helper Ayuda visible cuando no hay error.
 * @param multiline Permite escribir varias líneas.
 * @param keyboard Tipo de teclado.
 */
@Composable
fun FormField(label: String, value: String, onChange: (String) -> Unit, icon: ImageVector,
              error: String? = null, helper: String = "", multiline: Boolean = false,
              keyboard: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(value = value, onValueChange = onChange, label = { Text(label) },
        modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(icon, null) },
        isError = error != null, supportingText = { Text(error ?: helper) },
        singleLine = !multiline, minLines = if (multiline) 3 else 1,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard))
}

/** Selección múltiple mediante chips horizontales.
 * @param title Nombre de la colección.
 * @param options Valores disponibles.
 * @param selected Valores seleccionados.
 * @param onToggle Comunica la opción pulsada al propietario del estado.
 * @param multiple Indica si la ayuda describe selección múltiple o única.
 */
@Composable
fun MultiSelection(title: String, options: List<String>, selected: Set<String>, onToggle: (String) -> Unit, multiple: Boolean = true) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        Text(if (multiple) "Puedes elegir varias opciones" else "Elige una opción", style = MaterialTheme.typography.bodySmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(options) { option ->
                FilterChip(selected = option in selected, onClick = { onToggle(option) }, label = { Text(option) },
                    leadingIcon = { if (option in selected) Icon(Icons.Default.Check, null, Modifier.size(18.dp)) })
            }
        }
    }
}

/** Fecha de solo lectura con botón de calendario.
 * @param value Fecha actual en milisegundos UTC.
 * @param onOpen Solicita abrir el DatePicker gestionado por la pantalla stateful.
 * @param label Nombre del atributo de fecha.
 */
@Composable
fun DateField(value: Long, onOpen: () -> Unit, label: String = "Fecha de registro") {
    OutlinedTextField(value = fechaTexto(value), onValueChange = {}, readOnly = true,
        label = { Text(label) }, modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.DateRange, null) },
        trailingIcon = { IconButton(onClick = onOpen) { Icon(Icons.Default.EditCalendar, "Elegir fecha") } },
        supportingText = { Text("Pulsa el calendario para cambiar la fecha") })
}

@Preview(showBackground = true, widthDp = 360)
@Preview(showBackground = true, widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ComponentsPreview() {
    PlannerTheme {
        Surface { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ScreenHeader("Añadir equipo", "Tu inventario de ordenadores", Icons.Default.Computer)
            FormField("Nombre", "PC de estudio", {}, Icons.Default.Computer, helper = "Un nombre fácil de reconocer")
            MultiSelection("Uso del equipo", listOf("Estudio", "Gaming", "Diseño"), setOf("Estudio"), {})
        } }
    }
}
