package org.iesharia.seguimientodehabitos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import org.iesharia.seguimientodehabitos.data.model.Habito

@Composable
fun HabitListScreen(
    habits: List<Habito>,
    onCreateHabit: () -> Unit,
    onDeleteHabit: (Habito) -> Unit,
    onEditHabit: (Habito) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateHabit) {
                Icon(Icons.Default.Add, contentDescription = "Crear hábito")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {

            Text("Mis Hábitos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))


            LazyColumn {
                items(habits) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(habit.nombre, style = MaterialTheme.typography.bodyLarge)
                                Text("Meta diaria: ${habit.metaDiaria}", style = MaterialTheme.typography.bodySmall)
                            }

                            Row {
                                IconButton(onClick = { onEditHabit(habit) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = { onDeleteHabit(habit) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

