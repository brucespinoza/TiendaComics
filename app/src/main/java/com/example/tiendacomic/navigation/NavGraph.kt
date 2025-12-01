package com.example.tiendacomic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.ui.screen.*
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion

@Composable
fun NavGraph(navController: NavHostController, vm: ModeloAutenticacion, catalogoVm: CatalogoViewModel) {
    NavHost(navController = navController, startDestination = Route.Login.path) {

        composable(Route.Login.path) {
            LoginScreenVm(
                vm = vm,
                onLoginExitoso = {
                    if (vm.ultimoRol == "admin") navController.navigate(Route.Admin.path)
                    else navController.navigate(Route.Catalogo.path)
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
            CatalogoScreen(navController = navController, vm = catalogoVm, authVm = vm)
        }

        composable(Route.Perfil.path) {
            PerfilScreen(navController = navController, vm = vm)
        }

        composable(Route.Admin.path) { 
            AdminScreen(
                vm = catalogoVm,
                onCerrarSesion = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Carrito.path) {
            val comicsDemo = listOf(
                ComicEntity(1, "Batman: Año Uno", 20000, "batman", "Los primeros días de Bruce Wayne"),
                ComicEntity(2, "Narnia", 17000, "narnia", "Un mundo mágico lleno de criaturas fantásticas")
            )
            CarritoScreen(
                carrito = comicsDemo,
                onFinalizarCompra = {},
                onVaciarCarrito = {}
            )
        }

        composable(Route.Membresia.path) {
            MembresiaScreen(authVm = vm)
        }
    }
}
