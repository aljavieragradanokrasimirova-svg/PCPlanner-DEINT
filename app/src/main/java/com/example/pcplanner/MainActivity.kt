package com.example.pcplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.pcplanner.ui.equipo.EquipoScreen
import com.example.pcplanner.ui.listado.ListadoScreen
import com.example.pcplanner.ui.plan.PlanScreen
import com.example.pcplanner.ui.theme.PlannerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Para ejecutar cada pantalla de esta fase, cambia "listado" por "equipo" o "plan".
        val pantalla = intent.getStringExtra("pantalla") ?: "listado"
        setContent {
            PlannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = { Text("PC Planner") })
                }) { innerPadding ->
                    val modifier = Modifier.padding(innerPadding)
                    when (pantalla) {
                        "equipo" -> EquipoScreen(modifier)
                        "plan" -> PlanScreen(modifier)
                        else -> ListadoScreen(modifier)
                    }
                }
            }
        }
    }
}
