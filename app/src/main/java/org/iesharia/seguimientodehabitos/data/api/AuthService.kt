package org.iesharia.seguimientodehabitos.data.api

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.iesharia.seguimientodehabitos.data.model.LoginRequest
import org.iesharia.seguimientodehabitos.data.model.LoginResponse
import org.iesharia.seguimientodehabitos.data.model.RegisterRequest

object AuthService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun login(email: String, password: String): LoginResponse? {
        return try {

            val response = client.post("http://10.0.2.2:8080/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            //Log.i("DAM",response.body())
            response.body()

        } catch (e: Exception) {
            println("Error de login: ${e.message}")
            null
        }
    }

    suspend fun register(request: RegisterRequest): String {
        val response = client.post("http://10.0.2.2:8080/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }

}
