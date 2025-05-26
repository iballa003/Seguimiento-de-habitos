package org.iesharia.seguimientodehabitos.data.api
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.iesharia.seguimientodehabitos.data.model.RegistroDTO
import org.iesharia.seguimientodehabitos.data.model.RegistroDTOEnvio
import java.time.LocalDate

object RegistroService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    suspend fun obtenerRegistrosPorFecha(fecha: String): List<RegistroDTO> {
        return client.get("http://10.0.2.2:8080/registro") {
            parameter("fecha", fecha)
        }.body()
    }
    suspend fun marcarCompletado(habitoId: Int, cantidad: Int = 1, nota: String? = null) {
        val registro = RegistroDTOEnvio(
            habitoId = habitoId,
            fecha = LocalDate.now().toString(),
            cantidad = cantidad,
            notas = nota
        )

        client.post("http://10.0.2.2:8080/registro") {
            contentType(ContentType.Application.Json)
            setBody(registro)
        }
    }
}
