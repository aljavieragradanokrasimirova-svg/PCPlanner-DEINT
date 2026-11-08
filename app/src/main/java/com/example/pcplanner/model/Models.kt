package com.example.pcplanner.model

data class Equipo(
    val id: Int, val nombre: String, val fechaRegistro: Long,
    val observaciones: String, val activo: Boolean, val usos: Set<String>
)

data class PlanMejora(
    val id: Int, val equipoId: Int, val nombre: String, val objetivo: String,
    val presupuesto: String, val fechaCreacion: Long, val estado: String,
    val completado: Boolean, val categorias: Set<String>
)

val equiposEjemplo = listOf(
    Equipo(1, "PC de escritorio", 1793491200000, "Ryzen 5 · 16 GB RAM · SSD 1 TB", true, setOf("Estudio", "Programación")),
    Equipo(2, "Portátil de clase", 1793577600000, "Intel Core i5 · 8 GB RAM · SSD 512 GB", true, setOf("Estudio")),
    Equipo(3, "Equipo de juegos", 1793664000000, "Ryzen 7 · RTX 3060 · 32 GB RAM", false, setOf("Gaming", "Diseño"))
)

val planEjemplo = PlanMejora(1, 1, "Más memoria y almacenamiento", "Mejorar el rendimiento de Android Studio y ampliar el espacio para proyectos.", "250.00", 1793836800000, "Pendiente", false, setOf("RAM", "Almacenamiento"))
