package org.iesharia.seguimientodehabitos.data.model
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val success: Boolean,
    val userId: Int? = null,
    val message: String
)
