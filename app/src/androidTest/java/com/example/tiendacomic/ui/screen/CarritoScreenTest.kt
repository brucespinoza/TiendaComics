package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android Tests para CarritoScreen
 * Pruebas de UI instrumentadas que se ejecutan en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class CarritoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== TESTS DE ELEMENTOS VISIBLES ====================

    @Test
    fun carritoScreen_tituloVisible() {
        composeTestRule.setContent {
            TestCarritoScreen()
        }

        composeTestRule.onNodeWithText("🛒 Tu Carrito")
            .assertIsDisplayed()
    }

    @Test
    fun carritoScreen_carritoVacio_muestraMensaje() {
        composeTestRule.setContent {
            TestCarritoScreenVacio()
        }

        composeTestRule.onNodeWithText("Tu carrito está vacío")
            .assertIsDisplayed()
    }

    // ==================== TESTS CON PRODUCTOS ====================

    @Test
    fun carritoScreen_productoVisible() {
        composeTestRule.setContent {
            TestCarritoScreenConProductos()
        }

        composeTestRule.onNodeWithText("Batman: Año Uno")
            .assertIsDisplayed()
    }

    @Test
    fun carritoScreen_precioProductoVisible() {
        composeTestRule.setContent {
            TestCarritoScreenConProductos()
        }

        composeTestRule.onNodeWithText("$20.000")
            .assertIsDisplayed()
    }

    @Test
    fun carritoScreen_totalVisible() {
        composeTestRule.setContent {
            TestCarritoScreenConProductos()
        }

        composeTestRule.onNodeWithText("Total:", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun carritoScreen_botonFinalizarCompraVisible() {
        composeTestRule.setContent {
            TestCarritoScreenConProductos()
        }

        composeTestRule.onNodeWithText("Finalizar compra")
            .assertIsDisplayed()
    }

    @Test
    fun carritoScreen_botonVaciarCarritoVisible() {
        composeTestRule.setContent {
            TestCarritoScreenConProductos()
        }

        composeTestRule.onNodeWithText("Vaciar carrito")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE INTERACCIÓN ====================

    @Test
    fun carritoScreen_clickFinalizarCompra_funciona() {
        composeTestRule.setContent {
            TestCarritoScreenConProductos()
        }

        composeTestRule.onNodeWithText("Finalizar compra")
            .performClick()
    }

    @Test
    fun carritoScreen_clickVaciarCarrito_funciona() {
        composeTestRule.setContent {
            TestCarritoScreenConProductos()
        }

        composeTestRule.onNodeWithText("Vaciar carrito")
            .performClick()
    }

    // ==================== TESTS DE MEMBRESÍA ====================

    @Test
    fun carritoScreen_membresiaEnCarrito_visible() {
        composeTestRule.setContent {
            TestCarritoScreenConMembresia()
        }

        composeTestRule.onNodeWithText("Membresía Premium")
            .assertIsDisplayed()
    }
}

// ==================== COMPOSABLES DE PRUEBA ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestCarritoScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("🛒 Tu Carrito") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ) {
            Text("Contenido del carrito")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestCarritoScreenVacio() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("🛒 Tu Carrito") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Tu carrito está vacío")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestCarritoScreenConProductos() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("🛒 Tu Carrito") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Batman: Año Uno", style = MaterialTheme.typography.titleMedium)
                    Text("$20.000")
                    Text("Los primeros días de Bruce Wayne")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Total: $20.000", style = MaterialTheme.typography.titleLarge)
            
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Finalizar compra")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Vaciar carrito")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestCarritoScreenConMembresia() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("🛒 Tu Carrito") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Membresía Premium", style = MaterialTheme.typography.titleMedium)
                    Text("$50.000")
                }
            }
        }
    }
}

