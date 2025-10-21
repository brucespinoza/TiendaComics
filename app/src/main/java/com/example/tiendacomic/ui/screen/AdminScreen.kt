package com.example.tiendacomic.ui.screen



import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Aquí va el CRUD de cómics (crear / editar / eliminar)")
        }
    }
}
