package org.iesharia.seguimientodehabitos.navigation

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.iesharia.seguimientodehabitos.ui.screen.HomeScreen
import org.iesharia.seguimientodehabitos.ui.screen.LoginScreen
import org.iesharia.seguimientodehabitos.ui.screen.RegisterScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.iesharia.seguimientodehabitos.data.api.AuthService
import org.iesharia.seguimientodehabitos.ui.screen.HabitFormScreen
import org.iesharia.seguimientodehabitos.ui.screen.HabitListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Routes.HABIT_FORM
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = AuthService.login(email, password)
                        withContext(Dispatchers.Main) {
                            if (result != null) {
                                navController.navigate(Routes.HOME)
                            } else {
                                Toast.makeText(context, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
//                onRegisterSuccess = {
//                    navController.popBackStack()
//                    navController.navigate(Routes.HOME)
//                },
//                onBackClick = {
//                    navController.popBackStack()
//                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                userName = "Iballa",
                habits = listOf("Beber agua", "Estudiar Kotlin"),
                onGoToHistorial = { navController.navigate(Routes.HISTORY) },
                onGoToRecompensas = { navController.navigate(Routes.REWARDS) },
                onGoToConfiguracion = { navController.navigate(Routes.SETTINGS) },
                onRegistrarProgreso = { navController.navigate(Routes.PROGRESS) }
            )
        }
        composable(Routes.HABIT_LIST) {
            HabitListScreen(
                habits = listOf("Beber agua", "Leer 10 páginas"),
                onCreateHabit = { navController.navigate(Routes.HABIT_FORM) },
                onDeleteHabit = { habit ->
                    println("Eliminar hábito: $habit")
                },
                onEditHabit = { habit ->
                    println("Editar hábito: $habit")
                }
            )
        }
        composable(Routes.HABIT_FORM) {
            HabitFormScreen(
                usuarioId = 1,
                categorias = listOf(
                    1 to "Salud", 2 to "Lectura", 3 to "Ejercicio"
                ),
                onSave = { habit ->
                    println("Hábito guardado: $habit")
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HISTORY) {
            Text("Pantalla de Historial")
        }
        composable(Routes.REWARDS) {
            Text("Pantalla de Recompensas")
        }
        composable(Routes.SETTINGS) {
            Text("Pantalla de Ajustes")
        }
        composable(Routes.PROGRESS) {
            Text("Pantalla de Registro de Progreso")
        }

    }
}
