package org.iesharia.seguimientodehabitos.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.iesharia.seguimientodehabitos.data.model.LoginRequest
import org.iesharia.seguimientodehabitos.data.model.LoginResponse

object AuthService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun login(email: String, password: String): LoginResponse? {
        return try {
            val response = client.post("https://tubackend.com/api/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            response.body()
        } catch (e: Exception) {
            println("Error de login: ${e.message}")
            null
        }
    }
}
