package org.iesharia.seguimientodehabitos.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegistroDTOEnvio(
    @SerialName("habito_id") val habitoId: Int,
    val fecha: String,
    val cantidad: Int,
    val notas: String? = null
)