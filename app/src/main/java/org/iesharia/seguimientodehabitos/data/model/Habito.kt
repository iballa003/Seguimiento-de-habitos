package org.iesharia.seguimientodehabitos.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// CLIENTE - Android
@Serializable
data class Habito(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    @SerialName("meta_diaria")
    val metaDiaria: Int,
    val estado: Boolean,
    val categoriaId: Int,
    @SerialName("usuario_id")
    val usuarioId: Int
)


