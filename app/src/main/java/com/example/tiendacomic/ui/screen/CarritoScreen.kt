package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen() {
    // 🔹 Carrito vacío por defecto
    val carrito = remember { mutableStateListOf<Comic>() }

    val formatoCLP = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val total = carrito.sumOf { it.precio }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tu Carrito") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (carrito.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("🛒 Tu carrito está vacío.")
                }
            } else {
                LazyColumn {
                    items(carrito) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                                Text(formatoCLP.format(item.precio))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Total: ${formatoCLP.format(total)}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        carrito.clear()
                        scope.launch {
                            snackbarHostState.showSnackbar("✅ Su compra ha sido finalizada con éxito")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar compra")
                }
            }
        }
    }
}
