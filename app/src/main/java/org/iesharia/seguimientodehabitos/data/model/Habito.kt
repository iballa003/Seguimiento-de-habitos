package org.iesharia.seguimientodehabitos.data.model
import kotlinx.serialization.Serializable

@Serializable
data class Habito(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val meta_diaria: Int,
    val estado: Boolean,
    val categoriaId: Int
)
