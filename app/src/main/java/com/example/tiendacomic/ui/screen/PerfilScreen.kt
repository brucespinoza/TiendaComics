package com.example.tiendacomic.ui.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tiendacomic.navigation.Route
import com.example.tiendacomic.ui.components.AppTopBar
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.FileProvider

//val de la camara
private fun createTempImageFile(context: android.content.Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = File(context.cacheDir, "images").apply { mkdirs() }
    return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)
}

private fun getImageUriForFile(context: android.content.Context, file: File): Uri {
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}

@Composable
fun PerfilScreen(
    navController: NavHostController,
    vm: ModeloAutenticacion
) {
    val state = vm.perfilUiState.collectAsState().value
    val context = LocalContext.current

    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCaptureUri by remember { mutableStateOf<Uri?>(null) }

    //camara launcher
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

    //Galeria Launcher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            photoUriString = uri.toString()
            Toast.makeText(context, "Imagen seleccionada desde galería", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                onCatalogo = { navController.navigate(Route.Catalogo.path) },
                onPerfil = { navController.navigate(Route.Perfil.path) },
                onLogin = { navController.navigate(Route.Login.path) },
                onRegistro = { navController.navigate(Route.Registro.path) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ----------------------------------------------------------
            // Informacion de usuario
            // ----------------------------------------------------------
            Text("Perfil de Usuario", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))

            Text("Nombre: ${state.nombre}", style = MaterialTheme.typography.titleMedium)
            Text("RUT: ${state.rut}", style = MaterialTheme.typography.titleMedium)
            Text("Correo: ${state.correo}", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(30.dp))


            // Foto de perfil
            Text("Foto de Perfil", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            if (photoUriString != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(Uri.parse(photoUriString))
                        .build(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(12.dp))
            } else {
                Text("Sin imagen seleccionada", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
            }

            // ----------------------------------------------------------
            //BOTONES: Camara y Galeria
            // ----------------------------------------------------------
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                //Botton de la camara
                Button(onClick = {
                    try {
                        val file = createTempImageFile(context)
                        val uri = getImageUriForFile(context, file)
                        pendingCaptureUri = uri

                        //
                        context.grantUriPermission(
                            "com.android.camera",
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        takePictureLauncher.launch(uri)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }) {
                    Text("Tomar foto")
                }

                // Botton Galeria
                Button(onClick = {
                    pickImageLauncher.launch("image/*")
                }) {
                    Text("Galeria")
                }
            }

            Spacer(Modifier.height(24.dp))

            // Compras realizadas
            Text("Compras realizadas", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (state.compras.isEmpty()) {
                Text("Aún no tienes compras", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.compras) { titulo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(titulo, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}




