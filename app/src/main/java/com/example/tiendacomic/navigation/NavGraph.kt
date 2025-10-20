package com.example.tiendacomic.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiendacomic.ui.screen.*



/*
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Catalogo.path
    ) {
        composable(Route.Catalogo.path) { CatalogoScreen(navController) }
        composable(Route.Perfil.path) { PerfilScreen(navController) }
        composable(Route.Login.path) { LoginScreenVm(navController) }
        composable(Route.Register.path) { RegistroScreenVm(navController) }
    }
}
*/

//LAS RUTAS del Nuevo Controllador de rutas
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {
        composable(Route.Catalogo.path) {
            CatalogoScreen(
                onVerMas = { comic ->
                    navController.navigate("detalle/${comic.id}")
                }
            )

        }
        composable(Route.Perfil.path) {
            PerfilScreen(navController)
        }
        composable(Route.Registro.path) {
            RegistroScreenVm(
                onRegistroExitoso = { navController.navigate(Route.Login.path) },
                onIrLogin = { navController.navigate(Route.Login.path) }
            )
        }
        composable(Route.Login.path) {
            LoginScreenVm(
                onLoginExitoso = { navController.navigate(Route.Catalogo.path) },
                onIrRegistro = { navController.navigate(Route.Registro.path) }
            )
        }

    }
}


