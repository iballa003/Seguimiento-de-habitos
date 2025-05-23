package org.iesharia.seguimientodehabitos.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.iesharia.seguimientodehabitos.data.session.UserSessionManager

@Composable
fun SplashScreen(
    onNavigate: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { UserSessionManager(context) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000)
        sessionManager.userIdFlow.collect { userId ->
            onNavigate(userId != null)
        }
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Cargando...", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
