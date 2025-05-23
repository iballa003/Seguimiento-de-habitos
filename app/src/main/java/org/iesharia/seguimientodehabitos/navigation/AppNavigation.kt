package org.iesharia.seguimientodehabitos.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
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
import org.iesharia.seguimientodehabitos.data.api.HabitoService
import org.iesharia.seguimientodehabitos.data.model.Habito
import org.iesharia.seguimientodehabitos.data.session.UserSessionManager
import org.iesharia.seguimientodehabitos.ui.screen.HabitFormScreen
import org.iesharia.seguimientodehabitos.ui.screen.HabitListScreen
import org.iesharia.seguimientodehabitos.ui.screen.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { UserSessionManager(context) }
    val userId by sessionManager.userIdFlow.collectAsState(initial = null)

    var listaHabitos by remember { mutableStateOf<List<Habito>>(emptyList()) }
    LaunchedEffect(userId){
        if (userId != null){
            try {
                listaHabitos = HabitoService.obtenerHabitos(userId!!)
            } catch (e: Exception){
                println("Error al obtener los hábitos "+e.message)
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.LOGIN) {
            val context = LocalContext.current
            val sessionManager = remember { UserSessionManager(context) }
            val coroutineScope = rememberCoroutineScope()

            LoginScreen(
                onLoginClick = { email, password ->
                    coroutineScope.launch {
                        try {
                            val result = withContext(Dispatchers.IO) {
                                AuthService.login(email, password)
                            }

                            if (result != null && result.success && result.userId != null) {
                                sessionManager.saveUserId(result.userId)
                                navController.navigate(Routes.HOME)
                            } else {
                                Toast.makeText(context, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Vuelve al login al registrarse correctamente
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onCancel = {
                    // Vuelve al login si cancela
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SPLASH) {
            SplashScreen { isLoggedIn ->
                if (isLoggedIn) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
        }
        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                userName = "Iballa",
                habits= listOf("Beber agua", "Estudiar Kotlin"),
                onGoToHistorial = { navController.navigate(Routes.HISTORY) },
                onGoToRecompensas = { navController.navigate(Routes.REWARDS) },
                onGoToConfiguracion = { navController.navigate(Routes.SETTINGS) },
                onRegistrarProgreso = { navController.navigate(Routes.PROGRESS) },
                onGoToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.HABIT_LIST) {
            HabitListScreen(
                habits = listaHabitos,
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
