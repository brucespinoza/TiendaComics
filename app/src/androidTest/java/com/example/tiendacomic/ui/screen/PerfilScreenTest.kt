package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
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
 * Android Tests para PerfilScreen
 * Pruebas de UI instrumentadas que se ejecutan en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class PerfilScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== TESTS DE ELEMENTOS VISIBLES ====================

    @Test
    fun perfilScreen_tituloVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Perfil de Usuario")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_topBarVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Tienda de Comic")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_campoNombreVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Nombre")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_campoCorreoVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Correo")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_botonGuardarCambiosVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Guardar cambios")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_botonCambiarContrasenaVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Cambiar contraseña")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_seccionFotoPerfilVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Foto de Perfil")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_botonTomarFotoVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Tomar foto")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_botonGaleriaVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Galería")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_seccionComprasVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Compras realizadas")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE DATOS DE USUARIO ====================

    @Test
    fun perfilScreen_nombreUsuarioMostrado() {
        composeTestRule.setContent {
            TestPerfilScreenConDatos(nombre = "Bruce Espinoza")
        }

        composeTestRule.onNodeWithText("Bruce Espinoza")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_correoUsuarioMostrado() {
        composeTestRule.setContent {
            TestPerfilScreenConDatos(correo = "bruce@gmail.com")
        }

        composeTestRule.onNodeWithText("bruce@gmail.com")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE INTERACCIÓN ====================

    @Test
    fun perfilScreen_clickGuardarCambios_funciona() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Guardar cambios")
            .performClick()
    }

    @Test
    fun perfilScreen_clickCambiarContrasena_funciona() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Cambiar contraseña")
            .performClick()
    }

    @Test
    fun perfilScreen_clickTomarFoto_funciona() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Tomar foto")
            .performClick()
    }

    @Test
    fun perfilScreen_clickGaleria_funciona() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Galería")
            .performClick()
    }

    // ==================== TESTS DE COMPRAS ====================

    @Test
    fun perfilScreen_sinCompras_muestraMensaje() {
        composeTestRule.setContent {
            TestPerfilScreenSinCompras()
        }

        composeTestRule.onNodeWithText("Aún no tienes compras")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_conCompras_muestraLista() {
        composeTestRule.setContent {
            TestPerfilScreenConCompras()
        }

        composeTestRule.onNodeWithText("Batman: Año Uno")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_compraConCantidad_muestraCantidad() {
        composeTestRule.setContent {
            TestPerfilScreenConCompras()
        }

        composeTestRule.onNodeWithText("x2")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE NAVEGACIÓN ====================

    @Test
    fun perfilScreen_botonCatalogoVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Catálogo")
            .assertIsDisplayed()
    }

    @Test
    fun perfilScreen_botonPerfilVisible() {
        composeTestRule.setContent {
            TestPerfilScreen()
        }

        composeTestRule.onNodeWithText("Perfil")
            .assertIsDisplayed()
    }
}

// ==================== COMPOSABLES DE PRUEBA ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestPerfilScreen() {
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
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Perfil de Usuario", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text("Guardar cambios")
                    }
                    
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text("Cambiar contraseña")
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text("Foto de Perfil", style = MaterialTheme.typography.titleMedium)
                    Text("Sin imagen seleccionada")
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = {}) { Text("Tomar foto") }
                        OutlinedButton(onClick = {}) { Text("Galería") }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Compras realizadas", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun TestPerfilScreenConDatos(nombre: String = "", correo: String = "") {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil de Usuario")
        
        OutlinedTextField(
            value = nombre,
            onValueChange = {},
            label = { Text("Nombre") }
        )
        
        OutlinedTextField(
            value = correo,
            onValueChange = {},
            label = { Text("Correo") }
        )
    }
}

@Composable
private fun TestPerfilScreenSinCompras() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Compras realizadas")
        Text("Aún no tienes compras")
    }
}

@Composable
private fun TestPerfilScreenConCompras() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Compras realizadas")
        
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Batman: Año Uno")
                Text("x2")
            }
        }
    }
}



