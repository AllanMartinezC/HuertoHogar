package com.example.huerto_hogar.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.huerto_hogar.model.Usuario
import com.example.huerto_hogar.ui.components.BottomBar
import com.example.huerto_hogar.ui.components.BottomNavItem
import com.example.huerto_hogar.ui.components.DrawerItem
import com.example.huerto_hogar.ui.components.TopBar
import com.example.huerto_hogar.ui.screen.catalogo.CatalogoScreen
import com.example.huerto_hogar.ui.screen.inicio.InicioScreen
import com.example.huerto_hogar.ui.screen.login.LoginScreen
import com.example.huerto_hogar.ui.screen.perfil.PerfilScreen
import com.example.huerto_hogar.ui.screen.registro.RegistroScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

// 🔹 Rutas de la app
sealed class AppScreens(val route: String) {
    object Inicio : AppScreens("inicio")
    object Login : AppScreens("login")
    object Registro : AppScreens("registro")
    object Catalogo : AppScreens("catalogo")
    object Perfil : AppScreens("perfil")
    object Carrito : AppScreens("carrito")
}

// 🔹 Items para la barra inferior (global)
val bottomNavItems = listOf(
    BottomNavItem("Catálogo", Icons.Filled.Home, AppScreens.Catalogo.route),
    BottomNavItem("Perfil", Icons.Filled.Person, AppScreens.Perfil.route),
    BottomNavItem("Carrito", Icons.Filled.ShoppingCart, AppScreens.Carrito.route)
)

// 🔹 Pantallas que muestran TopBar/BottomBar
val bottomNavScreens = listOf(
    AppScreens.Catalogo.route,
    AppScreens.Perfil.route,
    AppScreens.Carrito.route
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var usuarioLogueado by remember { mutableStateOf<Usuario?>(null) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val colorVerde = Color(0xFF4CAF50)

    SideEffect {
        systemUiController.setStatusBarColor(color = colorVerde, darkIcons = false)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        if (drawerState.isOpen) drawerState.close()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú Huerto 🍎",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()

                DrawerItem("Catálogo") {
                    scope.launch { drawerState.close() }
                    navController.navigate(AppScreens.Catalogo.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                }
                DrawerItem("Perfil") {
                    scope.launch { drawerState.close() }
                    navController.navigate(AppScreens.Perfil.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                }
                DrawerItem("Carrito") {
                    scope.launch { drawerState.close() }
                    navController.navigate(AppScreens.Carrito.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                }
                DrawerItem("Cerrar sesión") {
                    scope.launch { drawerState.close() }
                    navController.navigate(AppScreens.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (currentRoute in bottomNavScreens) {
                    TopBar(
                        titulo = when (currentRoute) {
                            AppScreens.Catalogo.route -> "Catálogo 🍎"
                            AppScreens.Perfil.route -> "Perfil 👤"
                            AppScreens.Carrito.route -> "Carrito 🛒"
                            else -> ""
                        },
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onSearchClick = {}
                    )
                }
            },
            bottomBar = {
                if (currentRoute in bottomNavScreens) {
                    BottomBar(navController = navController, items = bottomNavItems)
                }
            }
        ) { paddingValues ->
            AnimatedNavHost(
                navController = navController,
                startDestination = AppScreens.Inicio.route,
                modifier = Modifier.padding(paddingValues),
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
            ) {
                composable(AppScreens.Inicio.route) { InicioScreen(navController) }
                composable(AppScreens.Login.route) {
                    LoginScreen(navController) { usuario ->
                        usuarioLogueado = usuario
                        navController.navigate(AppScreens.Catalogo.route) {
                            popUpTo(AppScreens.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
                composable(AppScreens.Registro.route) { RegistroScreen(navController) }
                composable(AppScreens.Catalogo.route) { usuarioLogueado?.let { CatalogoScreen() } }
                composable(AppScreens.Perfil.route) {
                    usuarioLogueado?.let { user ->
                        PerfilScreen(
                            usuarioLogueado = user,
                            onCerrarSesion = {
                                usuarioLogueado = null
                                navController.navigate(AppScreens.Login.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay usuario logueado")
                    }
                }
                composable(AppScreens.Carrito.route) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Carrito - En construcción")
                    }
                }
            }
        }
    }
}
