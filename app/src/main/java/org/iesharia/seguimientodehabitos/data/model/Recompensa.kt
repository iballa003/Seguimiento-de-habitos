package org.iesharia.seguimientodehabitos.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecompensaDTO(val usuarioId: Int, val puntos: Int)