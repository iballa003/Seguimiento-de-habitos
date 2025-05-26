package org.iesharia.seguimientodehabitos.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.iesharia.seguimientodehabitos.data.model.RecompensaDTO

object RecompensaService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    suspend fun sumarPuntos(usuarioId: Int, puntos: Int) {
        client.post("http://10.0.2.2:8080/recompensas/sumar") {
            contentType(ContentType.Application.Json)
            setBody(RecompensaDTO(usuarioId, puntos))
        }
    }

    suspend fun obtenerPuntos(usuarioId: Int): Int {
        return client.get("http://10.0.2.2:8080/recompensas") {
            parameter("usuarioId", usuarioId)
        }.body()
    }
}


