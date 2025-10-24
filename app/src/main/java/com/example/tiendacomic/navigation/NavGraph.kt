package com.example.tiendacomic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiendacomic.ui.screen.*
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion

@Composable
fun NavGraph(
    navController: NavHostController,
    vm: ModeloAutenticacion
) {
    NavHost(
        navController = navController,
        startDestination = Route.Catalogo.path
    ) {

        composable(Route.Login.path) {
            LoginScreenVm(
                vm = vm,
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

        composable(Route.Registro.path) {
            RegistroScreenVm(
                vm = vm,
                onRegistroExitoso = { navController.navigate(Route.Login.path) },
                onIrLogin = { navController.navigate(Route.Login.path) }
            )
        }

        composable(Route.Catalogo.path) {
            CatalogoScreen(navController)
        }

        composable(Route.Perfil.path) {
            PerfilScreen(
                navController = navController,
                vm = vm
            )
        }


        composable(Route.Admin.path) {
            AdminScreen()
        }
    }
}


