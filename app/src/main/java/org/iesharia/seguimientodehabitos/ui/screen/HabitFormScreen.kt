package org.iesharia.seguimientodehabitos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import org.iesharia.seguimientodehabitos.data.api.CategoriaService
import org.iesharia.seguimientodehabitos.data.model.Categoria
import org.iesharia.seguimientodehabitos.data.model.HabitFormData
import org.iesharia.seguimientodehabitos.ui.components.DropdownMenuWithLabel

@Composable
fun HabitFormScreen(
    usuarioId: Int,
    categorias: List<Pair<Int, String>>,
    onSave: (HabitFormData) -> Unit,
    onCancel: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var metaTexto by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf(true) }

    var categoriaSeleccionada by remember { mutableStateOf(categorias.firstOrNull()?.first ?: 1) }
    val categorias2 = remember { mutableStateOf<List<Categoria>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            categorias2.value = CategoriaService.obtenerCategorias()
        } catch (e: Exception) {
            println("Error al cargar categorías: ${e.message}")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Nuevo Hábito", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del hábito") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = metaTexto,
            onValueChange = { metaTexto = it },
            label = { Text("Meta numérica") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿Activo?")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = estado, onCheckedChange = { estado = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Categoría", style = MaterialTheme.typography.titleSmall)

        DropdownMenuWithLabel(
            categorias = categorias,
            selectedId = categoriaSeleccionada,
            onSelected = { categoriaSeleccionada = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onCancel) {
                Text("Cancelar")
            }

            Button(onClick = {
                val meta = metaTexto.toIntOrNull() ?: 1
                onSave(
                    HabitFormData(
                        usuarioId = usuarioId,
                        nombre = nombre,
                        descripcion = descripcion,
                        meta = meta,
                        estado = estado,
                        categoriaId = categoriaSeleccionada
                    )
                )
            }) {
                Text("Guardar")
            }

        }


        LazyColumn {
            items(categorias2.value) { cat ->
                Text("Categoría: ${cat.nombre}")
            }
        }
    }
}
