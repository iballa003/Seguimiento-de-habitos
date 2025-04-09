package org.iesharia.seguimientodehabitos.navigation

import androidx.compose.runtime.Composable
import org.iesharia.seguimientodehabitos.ui.screen.HomeScreen
import org.iesharia.seguimientodehabitos.ui.screen.LoginScreen
import org.iesharia.seguimientodehabitos.ui.screen.RegisterScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password ->
                    if (email == "test@email.com" && password == "1234") {
                        navController.navigate(Routes.HOME)
                    } else {
                        // Aquí podrías mostrar un mensaje de error con un Snackbar
                        println("Login fallido: datos incorrectos")
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
                onGoogleLoginClick = { /* TODO */ }
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
