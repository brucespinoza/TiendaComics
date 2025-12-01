package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android Tests para CatalogoScreen
 * Pruebas de UI instrumentadas que se ejecutan en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class CatalogoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== TESTS DE ELEMENTOS VISIBLES ====================

    @Test
    fun catalogoScreen_tituloVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Catálogo de Cómics")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_topBarVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Tienda de Comic")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_botonCatalogoVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Catálogo")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_botonPerfilVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Perfil")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_campoBusquedaVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Buscar cómic...")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_usuarioNormalVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen(esVip = false)
        }

        composeTestRule.onNodeWithText("👤 Usuario normal")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_usuarioVipVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen(esVip = true)
        }

        composeTestRule.onNodeWithText("🌟 Usuario VIP (50% aplicado)")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE COMICS ====================

    @Test
    fun catalogoScreen_comicBatmanVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Batman: Año Uno")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_comicSpidermanVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Spider-Man #1")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_precioComicVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("$20.000")
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_botonVerMasVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onAllNodesWithText("Ver más")
            .onFirst()
            .assertIsDisplayed()
    }

    // ==================== TESTS DE INTERACCIÓN ====================

    @Test
    fun catalogoScreen_clickBotonCatalogo_funciona() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Catálogo")
            .performClick()
    }

    @Test
    fun catalogoScreen_clickBotonPerfil_funciona() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Perfil")
            .performClick()
    }

    @Test
    fun catalogoScreen_clickVerMas_funciona() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onAllNodesWithText("Ver más")
            .onFirst()
            .performClick()
    }

    // ==================== TESTS DE API EXTERNA ====================

    @Test
    fun catalogoScreen_tipoCambioVisible() {
        composeTestRule.setContent {
            TestCatalogoScreenConTipoCambio()
        }

        composeTestRule.onNodeWithText("💱 API Externa: 1 USD = \$950 CLP", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun catalogoScreen_precioUsdVisible() {
        composeTestRule.setContent {
            TestCatalogoScreenConTipoCambio()
        }

        composeTestRule.onNodeWithText("≈ \$21", substring = true)
            .assertIsDisplayed()
    }

    // ==================== TESTS DE MEMBRESÍA ====================

    @Test
    fun catalogoScreen_membresiaVisible() {
        composeTestRule.setContent {
            TestCatalogoScreen()
        }

        composeTestRule.onNodeWithText("Membresía Premium")
            .assertIsDisplayed()
    }
}

// ==================== COMPOSABLES DE PRUEBA ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestCatalogoScreen(esVip: Boolean = false) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tienda de Comic", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF64B5F6)),
                actions = {
                    TextButton(onClick = {}) { Text("Catálogo", color = Color.White) }
                    TextButton(onClick = {}) { Text("Perfil", color = Color.White) }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(8.dp)
        ) {
            Text("Catálogo de Cómics", style = MaterialTheme.typography.headlineSmall)
            
            Text(
                if (esVip) "🌟 Usuario VIP (50% aplicado)" else "👤 Usuario normal",
                color = if (esVip) Color(0xFF1565C0) else Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar cómic...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Lista de comics de prueba
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    TestComicCard("Membresía Premium", "$50.000")
                }
                item {
                    TestComicCard("Batman: Año Uno", "$20.000")
                }
                item {
                    TestComicCard("Spider-Man #1", "$16.000")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestCatalogoScreenConTipoCambio() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tienda de Comic") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(8.dp)
        ) {
            Text("Catálogo de Cómics")
            Text("👤 Usuario normal")
            Text("💱 API Externa: 1 USD = \$950 CLP", color = Color(0xFF4CAF50))
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Batman: Año Uno")
                    Text("$20.000")
                    Text("≈ \$21.05 USD", color = Color(0xFF4CAF50))
                    Button(onClick = {}) { Text("Ver más") }
                }
            }
        }
    }
}

@Composable
private fun TestComicCard(titulo: String, precio: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(precio, color = Color(0xFF1976D2))
            Button(onClick = {}) {
                Text("Ver más")
            }
        }
    }
}

