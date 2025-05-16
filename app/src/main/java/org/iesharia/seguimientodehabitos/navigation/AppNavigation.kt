package org.iesharia.seguimientodehabitos.navigation

import android.widget.Toast
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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
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
//                onLogout = {
//                    navController.navigate(Routes.LOGIN) {
//                        popUpTo(Routes.LOGIN) { inclusive = true }
//                    }
//                }
            )
        }
    }
}
