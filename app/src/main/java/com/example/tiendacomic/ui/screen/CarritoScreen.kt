package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class) // para usar Scaffold experimental
@Composable
fun CarritoScreen() {

    // 🔹 Simulamos el carrito con algunos productos
    val carrito = remember {
        mutableStateListOf(
            Comic(1, "Spider-Man #1", 15990, 0, ""),
            Comic(10, "Membresía Premium", 4990, 0, "")
        )
    }

    // 🔹 Formato de moneda CLP
    val formatoCLP = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    // 🔹 Calcular total sumando los precios
    val total = carrito.sumOf { it.precio }

    // 🔹 Estructura general de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tu Carrito") })
        }
    ) { innerPadding ->

        // 🔹 Contenedor principal de la pantalla
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // 🔹 Mostrar mensaje si el carrito está vacío
            if (carrito.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío.")
                }
            } else {

                // 🔹 Mostrar lista de cómics en el carrito
                LazyColumn {
                    items(carrito) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = item.titulo,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(formatoCLP.format(item.precio))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🔹 Mostrar total del carrito
                Text(
                    text = "Total: ${formatoCLP.format(total)}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Botón para finalizar la compra
                Button(
                    onClick = {
                        // Aquí puedes agregar la lógica para finalizar compra
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar compra")
                }
            } // ← cierre del if/else
        } // ← cierre del Column
    } // ← cierre del Scaffold
}
