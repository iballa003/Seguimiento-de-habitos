package org.iesharia.seguimientodehabitos.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.iesharia.seguimientodehabitos.data.api.HabitoService
import org.iesharia.seguimientodehabitos.data.model.Habito
import org.iesharia.seguimientodehabitos.data.session.UserSessionManager
import org.iesharia.seguimientodehabitos.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    onGoToHistorial: () -> Unit,
    onGoToRecompensas: () -> Unit,
    onGoToConfiguracion: () -> Unit,
    onRegistrarProgreso: () -> Unit
) {
    val context = LocalContext.current
    var listaHabitos by remember { mutableStateOf<List<Habito>>(emptyList()) }
    val sessionManager = remember { UserSessionManager(context) }
    val userId by sessionManager.userIdFlow.collectAsState(initial = null)
    val currentUserId = userId
    LaunchedEffect(currentUserId) {
        Log.i("Dam2User", userId.toString())
        if (currentUserId != null) {
            try {
                listaHabitos = HabitoService.obtenerHabitos(currentUserId)
                Log.i("Dam2","✅ Hábitos recibidos: $listaHabitos") // <--- Asegura que llegaron

            } catch (e: Exception) {
                println("Error al cargar hábitos: ${e.message}")
            }
        }
    }
    val habits : List<String> = listOf("x", "y", "z")
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onRegistrarProgreso) {
                Icon(Icons.Default.Add, contentDescription = "Registrar progreso")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
//            Text(
//                text = "Hola, $userName 👋",
//                style = MaterialTheme.typography.headlineMedium
//            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Hábitos activos hoy:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))
            if (listaHabitos.isEmpty()) {
                Text("No hay hábitos aún", style = MaterialTheme.typography.bodyMedium)
            }else{}
            LazyColumn {
                items(listaHabitos) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = habit.nombre,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Triple(Icons.Default.History, "Historial", onGoToHistorial),
                    Triple(Icons.Default.EmojiEvents, "Recompensas", onGoToRecompensas),
                    Triple(Icons.Default.Settings, "Ajustes", onGoToConfiguracion)
                ).forEach { (icon, label, onClick) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        IconButton(onClick = onClick, modifier = Modifier.size(64.dp)) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(label, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

        }
    }
}
