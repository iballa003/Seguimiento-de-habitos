package org.iesharia.seguimientodehabitos.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.iesharia.seguimientodehabitos.data.model.Habito

object HabitoService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun obtenerHabitos(usuarioId: Int): List<Habito> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/habitos") {
            parameter("usuarioId", usuarioId)
            accept(ContentType.Application.Json)
        }
        return response.body()
    }
}
