package org.iesharia.seguimientodehabitos.ui.screen


import androidx.compose.material3.Switch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.iesharia.seguimientodehabitos.utils.ReminderService
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onLogout: () -> Unit = {},
    isDark2: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
) {
    var checked by remember { mutableStateOf(isDark2) }
    val context = LocalContext.current
    LaunchedEffect(isDark2) {
        checked = isDark2
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
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            RowSwitchItem(
                title = "Activar notificaciones",
                initial = true,
                onToggle = { activado ->
                    if (activado) {
                        ReminderService.programarRecordatorio(context)
                    } else {
                        ReminderService.cancelarRecordatorio(context)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            RowSwitchItem(
                title = "Modo oscuro",
                initial = isDark2, // puede venir de ViewModel o DataStore
                onToggle = { onToggleDarkMode(it)
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión", color = Color.White)
            }

        }
    }
}

@Composable
fun RowSwitchItem(
    title: String,
    initial: Boolean,
    onToggle: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(initial) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { checked = !checked; onToggle(checked) }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Switch(
            checked = checked,
            onCheckedChange = {
            checked = it
            onToggle(it)
        },
            colors = SwitchDefaults.colors(
            checkedThumbColor = androidx.compose.ui.graphics.Color.White,
            checkedTrackColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),   // cambia el fondo azul por verde, por ejemplo
            uncheckedThumbColor = androidx.compose.ui.graphics.Color.Gray,
            uncheckedTrackColor = androidx.compose.ui.graphics.Color.LightGray
        )
        )
    }
}
