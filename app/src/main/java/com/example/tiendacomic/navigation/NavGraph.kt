package com.example.tiendacomic.navigation



import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiendacomic.ui.screen.*
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
//
import androidx.lifecycle.viewmodel.compose.viewModel



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
        startDestination = Route.Catalogo.path //aqui modificamos la destinaciojn cuando ejecutamos

    ) {
        composable(Route.Catalogo.path) {
            CatalogoScreen(
                navController = navController,//añadido el Tobar el controlador
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
        }//

        //aqui añadimos elo admin en modeloAutenticacion por rut
        composable(Route.Login.path) {
            val vm: ModeloAutenticacion = viewModel()

            LoginScreenVm(
                onLoginExitoso = {
                    if (vm.ultimoRol == "admin") {
                        navController.navigate(Route.Admin.path)
                    } else {
                        navController.navigate(Route.Catalogo.path)
                    }
                },
                onIrRegistro = { navController.navigate(Route.Registro.path) }
            )
        }

        //admin
        composable(Route.Admin.path) {
            AdminScreen()
        }


    }
}


