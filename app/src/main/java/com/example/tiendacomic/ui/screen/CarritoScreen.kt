package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiendacomic.data.local.usuario.ComicEntity
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carrito: List<ComicEntity> = emptyList(),     //  Lista de cómics agregados al carrito
    onFinalizarCompra: () -> Unit = {},           //  Callback para finalizar compra
    onVaciarCarrito: () -> Unit = {}              //  Callback para limpiar el carrito
) {
    // 🔹 Snackbar para mostrar mensajes
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 🔹 Formato de moneda CLP (pesos chilenos)
    val formatoCLP = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    // 🔹 Calcular total del carrito
    val total = carrito.sumOf { it.precio }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("🛒 Tu Carrito") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // --- Si el carrito está vacío ---
            if (carrito.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                // --- Mostrar lista de cómics ---
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(carrito) { comic ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = comic.titulo,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = formatoCLP.format(comic.precio),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (comic.descripcion.isNotBlank()) {
                                    Text(
                                        text = comic.descripcion,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // --- Total ---
                Text(
                    text = "Total: ${formatoCLP.format(total)}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(24.dp))

                // --- Botón Finalizar compra ---
                Button(
                    onClick = {
                        onFinalizarCompra()
                        scope.launch {
                            snackbarHostState.showSnackbar("Compra finalizada con éxito")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar compra")
                }

                Spacer(Modifier.height(8.dp))

                // --- Botón Vaciar carrito ---
                OutlinedButton(
                    onClick = {
                        onVaciarCarrito()
                        scope.launch {
                            snackbarHostState.showSnackbar("Carrito vaciado correctamente")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Vaciar carrito")
                }
            }
        }
    }
}

