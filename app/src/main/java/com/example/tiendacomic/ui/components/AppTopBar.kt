package com.example.tiendacomic.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable // Composable reutilizable: barra superior
fun AppTopBar(
    //onOpenDrawer: () -> Unit, // Abre el drawer (hamburguesa)
    onCatalogo: () -> Unit,       // Navega a Home
    onLogin: () -> Unit,      // Navega a Login
    onRegistro: () -> Unit,    // Navega a Registro
    onPerfil: () ->  Unit
) {

    //lo que hace es crear una variable de estado recordada que le dice a la interfaz
    // si el menú desplegable de 3 puntitos debe estar visible (true) o oculto (false).
    var showMenu by remember { mutableStateOf(false) } // Estado del menú overflow

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),title = { // Slot del título
            /*Text(
                text = "Catalogo", // Título visible
                style = MaterialTheme.typography.titleLarge, // Estilo grande
                maxLines = 1,              // asegura una sola línea Int.MAX_VALUE   // permite varias líneas
                overflow = TextOverflow.Ellipsis // agrega "..." si no cabe

            )*/
        },
        /*
        navigationIcon = { // Ícono a la izquierda (hamburguesa)
            IconButton(onClick = onOpenDrawer) { // Al presionar, abre drawer
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menú") // Ícono
            }
        },

         */
        actions = { // Acciones a la derecha (íconos + overflow)
            IconButton(onClick = onCatalogo) { // Ir a Catalogo
                Icon(Icons.Filled.Home, contentDescription = "Catalogo") // Ícono Home
            }
            IconButton(onClick = onLogin) { // Ir a Login
                Icon(Icons.Filled.AccountCircle, contentDescription = "Login") // Ícono Login
            }
            IconButton(onClick = onRegistro) { // Ir a Registro
                Icon(Icons.Filled.Person, contentDescription = "Registro") // Ícono Registro
            }
            IconButton(onClick = onPerfil) { // Ir a Registro
                Icon(Icons.Filled.Accessibility, contentDescription = "Perfil") // Ícono Registro
            }
            IconButton(onClick = { showMenu = true }) { // Abre menú overflow
                Icon(Icons.Filled.MoreVert, contentDescription = "Más") // Ícono 3 puntitos
            }
            DropdownMenu(
                expanded = showMenu, // Si está abierto
                onDismissRequest = { showMenu = false } // Cierra al tocar fuera
            ) {
                DropdownMenuItem( // Opción Catalogo
                    text = { Text("Catalogo") }, // Texto opción
                    onClick = { showMenu = false; onCatalogo() } // Navega y cierra
                )
                DropdownMenuItem( // Opción Login
                    text = { Text("Login") },
                    onClick = { showMenu = false; onLogin() }
                )
                DropdownMenuItem( // Opción Registro
                    text = { Text("Registro") },
                    onClick = { showMenu = false; onRegistro() }
                )
                DropdownMenuItem( // Opción Perfil
                    text = { Text("Perfil") },
                    onClick = { showMenu = false; onPerfil() }
                )
            }
        }
    )

}