package org.iesharia.seguimientodehabitos.ui.screen

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
import org.iesharia.seguimientodehabitos.data.session.UserSessionManager
import org.iesharia.seguimientodehabitos.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userName: String = "Usuario",
    habits: List<String> = listOf("Beber agua", "Leer 10 páginas", "Caminar 30 min"),
    onGoToHistorial: () -> Unit,
    onGoToRecompensas: () -> Unit,
    onGoToConfiguracion: () -> Unit,
    onRegistrarProgreso: () -> Unit,
    onGoToLogin: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { UserSessionManager(context) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inicio") },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            sessionManager.clearSession()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                //navController.navigate(Routes.CREATE_HABIT)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Crear hábito")
            }
        }
    )
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Hábitos activos hoy:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            HabitListScreen(
                habits = listaHabitos,
                onCreateHabit = { navController.navigate(Routes.HABIT_FORM) },
                onEditHabit = { /* navegar a editar */ },
                onDeleteHabit = { /* mostrar confirmación y eliminar */ }
            )


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
