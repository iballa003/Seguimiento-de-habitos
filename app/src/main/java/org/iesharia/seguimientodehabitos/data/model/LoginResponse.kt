package org.iesharia.seguimientodehabitos.data.model
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String, // o lo que te devuelva tu backend
    val userId: Int
)
