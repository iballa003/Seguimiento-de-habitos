package org.iesharia.seguimientodehabitos

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.iesharia.seguimientodehabitos.data.viewmodel.HabitosViewModel
import org.iesharia.seguimientodehabitos.data.viewmodel.ThemeViewModel
import org.iesharia.seguimientodehabitos.navigation.AppNavigation
import org.iesharia.seguimientodehabitos.ui.theme.SeguimientoDeHabitosTheme
import org.iesharia.seguimientodehabitos.ui.screen.LoginScreen
import org.iesharia.seguimientodehabitos.utils.ReminderService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
            val habitosViewModel: HabitosViewModel = viewModel()
            val themeViewModel: ThemeViewModel = viewModel()
            val isDark by themeViewModel.isDarkMode.collectAsState()
            ReminderService.programarRecordatorio(this)
            SeguimientoDeHabitosTheme(darkTheme = isDark) {
                AppNavigation(themeViewModel, habitosViewModel)
            }
        }
    }
}
