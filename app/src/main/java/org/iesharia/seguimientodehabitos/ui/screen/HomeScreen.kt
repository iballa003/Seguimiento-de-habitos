package org.iesharia.seguimientodehabitos.ui.screen


import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.iesharia.seguimientodehabitos.data.api.RecompensaService
import org.iesharia.seguimientodehabitos.data.api.RegistroService
import org.iesharia.seguimientodehabitos.data.session.UserSessionManager
import org.iesharia.seguimientodehabitos.data.viewmodel.HabitosViewModel
import org.iesharia.seguimientodehabitos.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onAddHabit: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenLogbook: () -> Unit = {},
    onEditar: () -> Unit,
    viewModel: HabitosViewModel = viewModel()
) {
    val habitos by viewModel.habitos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val puntos = remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val userId = UserSessionManager(context).userIdFlow.firstOrNull()
        if (userId != null) {
            puntos.value = RecompensaService.obtenerPuntos(userId)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50), // verde
                    titleContentColor = androidx.compose.ui.graphics.Color.White,
                    navigationIconContentColor = androidx.compose.ui.graphics.Color.White,
                    actionIconContentColor = androidx.compose.ui.graphics.Color.White
                ),
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Hábitos", style = MaterialTheme.typography.headlineMedium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenLogbook) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Logbook")
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabit) {
                Icon(Icons.Default.Add, contentDescription = "Agregar hábito")
            }
        }
    ) { padding ->
        when {
            loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            habitos.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay hábitos aún", style = MaterialTheme.typography.bodyLarge)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    // Tarjeta de recompensas
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Recompensas", style = MaterialTheme.typography.titleMedium)
                            Text("${puntos.value} pts", style = MaterialTheme.typography.headlineSmall)
                        }
                    }

                    // Lista de hábitos
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(habitos) { habito ->
                            HabitCard(
                                title = habito.nombre,
                                description = habito.descripcion,
                                meta = "Meta: ${habito.metaDiaria} min",
                                onEditar = { navController.navigate(Routes.editarHabitoConId(habito.id)) },
                                onMarcarCompletado = {
                                    scope.launch {
                                        try {
                                            RegistroService.marcarCompletado(
                                                habitoId = habito.id,
                                                cantidad = habito.metaDiaria
                                            )
                                            val userId = UserSessionManager(context).userIdFlow.firstOrNull()
                                            if (userId != null) {
                                                RecompensaService.sumarPuntos(userId, habito.metaDiaria)
                                                puntos.value = RecompensaService.obtenerPuntos(userId)
                                            }
                                            Toast.makeText(context, "Hábito registrado", Toast.LENGTH_SHORT).show()
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Error al registrar hábito", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

        }

    }
}


@Composable
fun HabitCard(
    title: String,
    description: String,
    meta: String = "Meta: 30 min",
    onMarcarCompletado: () -> Unit = {},
    onEditar: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // Azul claro como ejemplo
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontSize = 24.sp)

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = description, style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = meta, style = MaterialTheme.typography.bodySmall, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(onClick = onMarcarCompletado) {
                        Icon(Icons.Default.Check, contentDescription = "Completado")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Completado", fontSize = 20.sp)
                    }

                    OutlinedButton(onClick = onEditar) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}
