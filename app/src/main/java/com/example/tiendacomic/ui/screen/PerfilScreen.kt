package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tiendacomic.navigation.Route
import com.example.tiendacomic.ui.components.AppTopBar
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion

@Composable
fun PerfilScreen(
    navController: NavHostController,
    vm: ModeloAutenticacion //base de datos
) {
    // Obtener el estado actual del perfil desde el ViewModel
    val state = vm.perfilUiState.collectAsState().value //base de datos
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                onCatalogo = { navController.navigate(Route.Catalogo.path) },
                onPerfil = { navController.navigate(Route.Perfil.path) },
                onLogin = { navController.navigate(Route.Login.path) },
                onRegistro = { navController.navigate(Route.Registro.path) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Perfil de Usuario",style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Nombre: ${state.nombre}", style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "RUT: ${state.rut}", style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Correo: ${state.correo}", style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))


                }
            }
        }



