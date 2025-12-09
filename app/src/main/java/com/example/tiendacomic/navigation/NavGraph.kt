package com.example.tiendacomic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.ui.screen.*
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import com.example.tiendacomic.navigation.Route

@Composable
fun NavGraph(
    navController: NavHostController,
    vm: ModeloAutenticacion,
    catalogoVm: CatalogoViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {

        // ---------- LOGIN ----------
        composable(Route.Login.path) {
            LoginScreenVm(
                navController = navController,   // ← AGREGADO (OBLIGATORIO)
                vm = vm,
                onLoginExitoso = {
                    if (vm.ultimoRol == "admin") {
                        navController.navigate(Route.Admin.path)
                    } else {
                        navController.navigate(Route.Catalogo.path)
                    }
                },
                onIrRegistro = {
                    navController.navigate(Route.Registro.path)
                }
            )
        }

        // ---------- REGISTRO ----------
        composable(Route.Registro.path) {
            RegistroScreenVm(
                vm = vm,
                onRegistroExitoso = { navController.navigate(Route.Login.path) },
                onIrLogin = { navController.navigate(Route.Login.path) }
            )
        }

        // ---------- CATÁLOGO ----------
        composable(Route.Catalogo.path) {
            CatalogoScreen(
                navController = navController,
                vm = catalogoVm,
                authVm = vm
            )
        }

        // ---------- PERFIL ----------
        composable(Route.Perfil.path) {
            PerfilScreen(
                navController = navController,
                vm = vm
            )
        }

        // ---------- ADMIN ----------
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

        // ---------- RECUPERAR CONTRASEÑA ----------
        composable(Route.RecuperarContraseña.path) {
            RecuperarContraseñaScreen(navController = navController)
        }


        // ---------- CARRITO ----------
        composable(Route.Carrito.path) {
            val comicsDemo = listOf(
                ComicEntity(
                    id = 1,
                    titulo = "Batman: Año Uno",
                    precio = 20000,
                    imagen = "batman",
                    descripcion = "Los primeros días de Bruce Wayne"
                ),
                ComicEntity(
                    id = 2,
                    titulo = "Narnia",
                    precio = 17000,
                    imagen = "narnia",
                    descripcion = "Un mundo mágico lleno de criaturas fantásticas"
                )
            )

            CarritoScreen(
                carrito = comicsDemo,
                onFinalizarCompra = {},
                onVaciarCarrito = {}
            )
        }

        // ---------- MEMBRESÍA ----------
        composable(Route.Membresia.path) {
            MembresiaScreen(authVm = vm)
        }
    }
}

