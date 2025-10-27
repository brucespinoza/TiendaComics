package com.example.tiendacomic.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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

// ---------- Helpers ----------
private fun createTempImageFile(context: android.content.Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = File(context.cacheDir, "images").apply {
        if (!exists()) mkdirs()
    }
    return File(storageDir, "IMG_${timeStamp}.jpg")
}

private fun getImageUriForFile(context: android.content.Context, file: File): Uri {
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}

// ---------- Pantalla Perfil ----------
@Composable
fun PerfilScreen(
    navController: NavHostController,
    vm: ModeloAutenticacion
) {
    val state = vm.perfilUiState.collectAsState().value
    val context = LocalContext.current

    var photoUriString by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCaptureUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUriString = pendingCaptureUri?.toString()
            Toast.makeText(context, "Foto guardada", Toast.LENGTH_SHORT).show()
        } else {
            pendingCaptureUri = null
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

            Text("Perfil de Usuario", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))

            Text("Nombre: ${state.nombre}", style = MaterialTheme.typography.titleMedium)
            Text("RUT: ${state.rut}", style = MaterialTheme.typography.titleMedium)
            Text("Correo: ${state.correo}", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(30.dp))

            // ---------- FOTO PERFIL ----------
            Text("Foto de Perfil", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            if (photoUriString != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(Uri.parse(photoUriString))
                        .build(),
                    contentDescription = "Foto perfil",
                    modifier = Modifier.size(160.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            Button(onClick = {
                val file = createTempImageFile(context)
                val uri = getImageUriForFile(context, file)
                pendingCaptureUri = uri
                takePictureLauncher.launch(uri)
            }) {
                Text(if (photoUriString == null) "Tomar foto" else "Re-tomar foto")
            }
        }
    }
}




