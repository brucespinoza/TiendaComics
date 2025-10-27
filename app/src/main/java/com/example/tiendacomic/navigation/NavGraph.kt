package com.example.tiendacomic.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiendacomic.ui.screen.*
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion

@Composable
fun NavGraph(
    navController: NavHostController,
    vm: ModeloAutenticacion

) {
    val catalogoVm: CatalogoViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Route.Login.path // Pantalla inicial

    ) {


        // ----- LOGIN -----
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

        // ----- REGISTRO -----
        composable(Route.Registro.path) {
            RegistroScreenVm(
                vm = vm,
                onRegistroExitoso = { navController.navigate(Route.Login.path) },
                onIrLogin = { navController.navigate(Route.Login.path) }
            )
        }

        // ----- CATÁLOGO -----
        composable(Route.Catalogo.path) {
            CatalogoScreen(
                navController = navController,
                vm = catalogoVm    // <<< PASAMOS EL MISMO VM
            )
        }

        // ----- PERFIL -----
        composable(Route.Perfil.path) {
            PerfilScreen(
                navController = navController,
                vm = vm
            )
        }

        composable(Route.Admin.path) {
            AdminScreen()
        }

        composable(Route.Carrito.path) {
            CarritoScreen()
        }

        composable(Route.Membresia.path) {
            MembresiaScreen()
        }
    }
}


