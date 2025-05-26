package org.iesharia.seguimientodehabitos.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import org.iesharia.seguimientodehabitos.ui.screen.HomeScreen
import org.iesharia.seguimientodehabitos.ui.screen.LoginScreen
import org.iesharia.seguimientodehabitos.ui.screen.RegisterScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.iesharia.seguimientodehabitos.data.session.UserSessionManager
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.iesharia.seguimientodehabitos.data.api.AuthService
import org.iesharia.seguimientodehabitos.data.api.HabitoService
import org.iesharia.seguimientodehabitos.data.model.Habito
import org.iesharia.seguimientodehabitos.data.viewmodel.HabitosViewModel
import org.iesharia.seguimientodehabitos.data.viewmodel.ThemeViewModel
import org.iesharia.seguimientodehabitos.ui.screen.EditHabitScreen
import org.iesharia.seguimientodehabitos.ui.screen.HabitFormScreen
import org.iesharia.seguimientodehabitos.ui.screen.HabitListScreen
import org.iesharia.seguimientodehabitos.ui.screen.LogbookScreen
import org.iesharia.seguimientodehabitos.ui.screen.SettingsScreen
import org.iesharia.seguimientodehabitos.ui.screen.SplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(themeViewModel: ThemeViewModel, habitosViewModel: HabitosViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { UserSessionManager(context) }
    val userId by sessionManager.userIdFlow.collectAsState(initial = null)
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
                onAddHabit = {navController.navigate(Routes.HABIT_FORM)},
                onOpenSettings = {navController.navigate(Routes.SETTINGS)},
                onOpenLogbook = {navController.navigate(Routes.LOGBOOK)},
                onEditar = {},
                viewModel = habitosViewModel
                )
        }
        composable(Routes.LOGBOOK) {
            LogbookScreen(
            onBack = {navController.popBackStack()}
            )
        }
        composable("editarHabito/{id}") { backStackEntry ->
            val habitoId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (habitoId == null) return@composable
            val habito = habitosViewModel.habitos.value.find { it.id == habitoId }
            val scope = rememberCoroutineScope()
            if (habito == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                EditHabitScreen(
                    habito = habito,
                    onSave = { actualizado ->
                        scope.launch {
                            HabitoService.updateHabito(actualizado)
                            habitosViewModel.recargarHabitos()
                            navController.popBackStack()
                        }
                    },
                    onDelete = { habitoAEliminar ->
                        scope.launch {
                            HabitoService.deleteHabito(habitoAEliminar.id)
                            habitosViewModel.recargarHabitos()
                            navController.popBackStack()
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
        composable(Routes.HABIT_FORM) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val session = remember { UserSessionManager(context) }

            val userId by session.userIdFlow.collectAsState(initial = null)

            userId?.let { uid ->
                HabitFormScreen(
                    usuarioId = uid,
                    categorias = emptyList(), // si aún no las pasas
                    onSave = { formData ->
                        scope.launch {
                            val habito = Habito(
                                id = 0, // el backend lo asignará
                                nombre = formData.nombre,
                                descripcion = formData.descripcion,
                                metaDiaria = formData.meta,
                                estado = formData.estado,
                                categoriaId = formData.categoriaId,
                                usuarioId = formData.usuarioId
                            )
                            HabitoService.createHabito(habito)
                            habitosViewModel.recargarHabitos()
                            navController.popBackStack()
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
        composable(Routes.HISTORY) {
            Text("Pantalla de Historial")
        }
        composable(Routes.REWARDS) {
            Text("Pantalla de Recompensas")
        }
        composable(Routes.SETTINGS) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val sessionManager = remember { UserSessionManager(context) }
            val isDark by themeViewModel.isDarkMode.collectAsState()
            SettingsScreen(
                onLogout = {scope.launch {
                    sessionManager.clearSession()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }},
                isDark2 = isDark,
                onToggleDarkMode = themeViewModel::toggleDarkMode,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROGRESS) {
            Text("Pantalla de Registro de Progreso")
        }

    }
}
