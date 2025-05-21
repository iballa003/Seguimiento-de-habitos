package org.iesharia.seguimientodehabitos.data.model
import kotlinx.serialization.Serializable

@Serializable
data class Categoria(
    val id: Int,
    val nombre: String
)
