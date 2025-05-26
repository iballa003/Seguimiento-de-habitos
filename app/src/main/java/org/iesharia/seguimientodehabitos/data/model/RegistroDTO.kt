package org.iesharia.seguimientodehabitos.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistroDTO(
    val habito: String,
    val cantidad: Int,
    val nota: String? = null,
    val fecha: String
)