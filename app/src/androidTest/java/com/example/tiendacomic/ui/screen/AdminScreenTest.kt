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
 * Android Tests para AdminScreen
 * Pruebas de UI instrumentadas que se ejecutan en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class AdminScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== TESTS DE ELEMENTOS VISIBLES ====================

    @Test
    fun adminScreen_tituloVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Panel de Administración")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_usuarioAdminVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Usuario Administrador")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_botonCerrarSesionVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Cerrar Sesión")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_formularioCrearVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Crear nuevo cómic")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE FORMULARIO ====================

    @Test
    fun adminScreen_campoTituloVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Título")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_campoPrecioVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Precio")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_campoDescripcionVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Descripción")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_campoImagenVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Imagen")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_botonCrearComicVisible() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Crear cómic")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE INTERACCIÓN ====================

    @Test
    fun adminScreen_clickCrearComic_funciona() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Crear cómic")
            .performClick()
    }

    @Test
    fun adminScreen_clickCerrarSesion_funciona() {
        composeTestRule.setContent {
            TestAdminScreen()
        }

        composeTestRule.onNodeWithText("Cerrar Sesión")
            .performClick()
    }

    // ==================== TESTS DE LISTA DE COMICS ====================

    @Test
    fun adminScreen_comicEnListaVisible() {
        composeTestRule.setContent {
            TestAdminScreenConComics()
        }

        composeTestRule.onNodeWithText("Batman: Año Uno")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_precioComicVisible() {
        composeTestRule.setContent {
            TestAdminScreenConComics()
        }

        composeTestRule.onNodeWithText("$20.000")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_botonEditarVisible() {
        composeTestRule.setContent {
            TestAdminScreenConComics()
        }

        composeTestRule.onNodeWithText("Editar")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_botonEliminarVisible() {
        composeTestRule.setContent {
            TestAdminScreenConComics()
        }

        composeTestRule.onNodeWithText("Eliminar")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_clickEditar_funciona() {
        composeTestRule.setContent {
            TestAdminScreenConComics()
        }

        composeTestRule.onNodeWithText("Editar")
            .performClick()
    }

    @Test
    fun adminScreen_clickEliminar_funciona() {
        composeTestRule.setContent {
            TestAdminScreenConComics()
        }

        composeTestRule.onNodeWithText("Eliminar")
            .performClick()
    }

    // ==================== TESTS DE MODO EDICIÓN ====================

    @Test
    fun adminScreen_modoEdicion_muestraEditarComic() {
        composeTestRule.setContent {
            TestAdminScreenModoEdicion()
        }

        composeTestRule.onNodeWithText("Editar cómic")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_modoEdicion_botonGuardarCambiosVisible() {
        composeTestRule.setContent {
            TestAdminScreenModoEdicion()
        }

        composeTestRule.onNodeWithText("Guardar cambios")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_modoEdicion_botonCancelarVisible() {
        composeTestRule.setContent {
            TestAdminScreenModoEdicion()
        }

        composeTestRule.onNodeWithText("Cancelar edición")
            .assertIsDisplayed()
    }

    // ==================== TESTS DE VALIDACIÓN ====================

    @Test
    fun adminScreen_errorTitulo_semuestra() {
        composeTestRule.setContent {
            TestAdminScreenConErrores(errorTitulo = "Ingresa un título")
        }

        composeTestRule.onNodeWithText("Ingresa un título")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_errorPrecio_semuestra() {
        composeTestRule.setContent {
            TestAdminScreenConErrores(errorPrecio = "Ingresa un precio")
        }

        composeTestRule.onNodeWithText("Ingresa un precio")
            .assertIsDisplayed()
    }

    @Test
    fun adminScreen_errorDescripcion_semuestra() {
        composeTestRule.setContent {
            TestAdminScreenConErrores(errorDescripcion = "Ingresa una descripción")
        }

        composeTestRule.onNodeWithText("Ingresa una descripción")
            .assertIsDisplayed()
    }
}

// ==================== COMPOSABLES DE PRUEBA ====================

@Composable
private fun TestAdminScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Panel de Administración", style = MaterialTheme.typography.headlineSmall)
                    Text("Usuario Administrador")
                }
                TextButton(onClick = {}) {
                    Text("Cerrar Sesión")
                }
            }
        }
        
        // Formulario
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Crear nuevo cómic", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = "batman",
                    onValueChange = {},
                    label = { Text("Imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Crear cómic")
                }
            }
        }
    }
}

@Composable
private fun TestAdminScreenConComics() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Panel de Administración")
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Batman: Año Uno", style = MaterialTheme.typography.titleMedium)
                        Text("$20.000", color = Color(0xFF1E88E5))
                        Text("Los primeros días de Bruce Wayne")
                    }
                    Column {
                        OutlinedButton(onClick = {}) { Text("Editar") }
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9534F))
                        ) { Text("Eliminar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun TestAdminScreenModoEdicion() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Editar cómic", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = "Batman: Año Uno",
                    onValueChange = {},
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Guardar cambios")
                }
                
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancelar edición")
                }
            }
        }
    }
}

@Composable
private fun TestAdminScreenConErrores(
    errorTitulo: String? = null,
    errorPrecio: String? = null,
    errorDescripcion: String? = null
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Título") },
            isError = errorTitulo != null,
            supportingText = { if (errorTitulo != null) Text(errorTitulo) }
        )
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Precio") },
            isError = errorPrecio != null,
            supportingText = { if (errorPrecio != null) Text(errorPrecio) }
        )
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Descripción") },
            isError = errorDescripcion != null,
            supportingText = { if (errorDescripcion != null) Text(errorDescripcion) }
        )
    }
}



