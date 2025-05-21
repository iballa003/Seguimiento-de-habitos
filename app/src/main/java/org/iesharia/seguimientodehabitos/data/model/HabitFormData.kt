package org.iesharia.seguimientodehabitos.data.model

data class HabitFormData(
    val usuarioId: Int,
    val nombre: String,
    val descripcion: String,
    val meta: Int,
    val estado: Boolean,
    val categoriaId: Int
)
