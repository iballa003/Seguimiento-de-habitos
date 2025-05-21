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
import org.iesharia.seguimientodehabitos.data.model.Categoria

object CategoriaService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun obtenerCategorias(): List<Categoria> {
        return try {
            val response = client.get("http://10.0.2.2:8080/categorias")
            Log.i("Respuesta cruda", "Respuesta cruda: ${response.bodyAsText()}")
            return response.body()

        } catch (e: Exception) {
            Log.i("Respuesta","Error al obtener categorías: ${e.message}")
            emptyList()
        }
    }
}
