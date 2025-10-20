package com.example.huerto_hogar.ui.theme.utils

// navigation/AppNavigation.kt
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val CATALOGO = "catalogo"
    const val PERFIL = "perfil"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.REGISTRO) {
            RegistroScreen(navController)
        }
        composable(Routes.CATALOGO) {
            CatalogoScreen(navController)
        }
        composable(Routes.PERFIL) {
            PerfilScreen(navController)
        }
    }
}
