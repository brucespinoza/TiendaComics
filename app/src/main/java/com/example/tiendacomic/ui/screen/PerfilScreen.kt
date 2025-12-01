package com.example.tiendacomic.ui.screen

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tiendacomic.data.storage.UserPreferences
import com.example.tiendacomic.navigation.Route
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.FileProvider

private fun createTempImageFile(context: android.content.Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = File(context.cacheDir, "images").apply { mkdirs() }
    return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)
}

private fun getImageUriForFile(context: android.content.Context, file: File): Uri {
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextTopBar(
    onCatalogo: () -> Unit,
    onLogout: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Tienda de Comic",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            // Solo mostramos "Catálogo" porque ya estamos en Perfil
            TextButton(onClick = onCatalogo) {
                Text("Catálogo", color = Color.White)
            }
            // Botón Cerrar Sesión
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                modifier = androidx.compose.ui.Modifier
                    .padding(end = 8.dp)
                    .clickable { onLogout() }
            ) {
                Text(text = "Cerrar", color = Color.White, fontSize = 12.sp)
                Text(text = "Sesión", color = Color.White, fontSize = 12.sp)
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF64B5F6) // Azul similar al de la captura
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavHostController,
    vm: ModeloAutenticacion
) {
    val state = vm.perfilUiState.collectAsState().value
    val context = LocalContext.current

    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCaptureUri by remember { mutableStateOf<Uri?>(null) }

    var nombre by rememberSaveable { mutableStateOf(state.nombre) }
    var correo by rememberSaveable { mutableStateOf(state.correo) }
    var mostrandoDialogoContrasena by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Se necesita permiso de cámara para tomar fotos", Toast.LENGTH_LONG).show()
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUriString = pendingCaptureUri?.toString()
            Toast.makeText(context, "Foto guardada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            pendingCaptureUri = null
            Toast.makeText(context, "No se tomó ninguna foto", Toast.LENGTH_SHORT).show()
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            photoUriString = uri.toString()
            Toast.makeText(context, "Imagen seleccionada desde galería", Toast.LENGTH_SHORT).show()
        }
    }

    val scope = rememberCoroutineScope()
    val userPrefs = remember { UserPreferences(context) }

    Scaffold(
        topBar = {
            TextTopBar(
                onCatalogo = { navController.navigate(Route.Catalogo.path) },
                onLogout = {
                    scope.launch {
                        vm.cerrarSesion()
                        userPrefs.setLoggedIn(false)
                        navController.navigate(Route.Login.path) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color.White)
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tarjeta blanca principal
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Perfil de Usuario",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF1565C0)
                        )
                        Spacer(Modifier.height(24.dp))

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                vm.actualizarPerfil(nombre, correo)
                                Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Guardar cambios") }

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { mostrandoDialogoContrasena = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1565C0))
                        ) { Text("Cambiar contraseña") }

                        Spacer(Modifier.height(24.dp))

                        Text("Foto de Perfil", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))

                        if (photoUriString != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(Uri.parse(photoUriString))
                                    .build(),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.size(160.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                        } else {
                            Text("Sin imagen seleccionada", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(12.dp))
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(
                                onClick = {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    try {
                                        val file = createTempImageFile(context)
                                        val uri = getImageUriForFile(context, file)
                                        pendingCaptureUri = uri
                                        takePictureLauncher.launch(uri)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
                                        e.printStackTrace()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                            ) { Text("Tomar foto") }

                            OutlinedButton(
                                onClick = { pickImageLauncher.launch("image/*") },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1565C0))
                            ) { Text("Galería") }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Compras
                Text("Compras realizadas", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                if (state.compras.isEmpty()) {
                    Text("Aún no tienes compras", style = MaterialTheme.typography.bodyMedium)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(state.compras.toList()) { (titulo, cantidad) ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(titulo, style = MaterialTheme.typography.bodyLarge)
                                    if (cantidad > 1) {
                                        Text("x$cantidad", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de cambio de contraseña
    if (mostrandoDialogoContrasena) {
        Dialog(onDismissRequest = { mostrandoDialogoContrasena = false }) {
            Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 8.dp) {
                var actual by remember { mutableStateOf("") }
                var nueva by remember { mutableStateOf("") }
                var confirmar by remember { mutableStateOf("") }
                var mostrarActual by remember { mutableStateOf(false) }
                var mostrarNueva by remember { mutableStateOf(false) }
                var mostrarConfirmar by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Cambiar Contraseña", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = actual,
                        onValueChange = { actual = it },
                        label = { Text("Contraseña actual") },
                        visualTransformation = if (mostrarActual) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarActual = !mostrarActual }) {
                                Icon(if (mostrarActual) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                            }
                        }
                    )

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nueva,
                        onValueChange = { nueva = it },
                        label = { Text("Nueva contraseña") },
                        visualTransformation = if (mostrarNueva) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarNueva = !mostrarNueva }) {
                                Icon(if (mostrarNueva) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                            }
                        }
                    )

                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmar,
                        onValueChange = { confirmar = it },
                        label = { Text("Confirmar nueva") },
                        visualTransformation = if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                                Icon(if (mostrarConfirmar) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                            }
                        }
                    )

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val resultado = vm.cambiarContrasena(actual, nueva, confirmar)
                            Toast.makeText(context, resultado, Toast.LENGTH_LONG).show()
                            if (resultado.startsWith("✅")) mostrandoDialogoContrasena = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Actualizar") }

                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { mostrandoDialogoContrasena = false },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1565C0)),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Cancelar") }
                }
            }
        }
    }
}
