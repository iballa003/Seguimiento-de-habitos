package org.iesharia.seguimientodehabitos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun DropdownMenuWithLabel(
    categorias: List<Pair<Int, String>>,
    selectedId: Int,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = categorias.firstOrNull { it.first == selectedId }?.second ?: ""

    Column {
        TextButton(onClick = { expanded = true }) {
            Text(text = if (selectedName.isNotBlank()) selectedName else "Selecciona una categoría")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categorias.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}