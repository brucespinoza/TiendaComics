package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController          // <-- IMPORT NECESARIO
import com.example.tiendacomic.navigation.Route // <-- IMPORT NECESARIO

// -------------------------------------------------------------
// VIEWMODEL COMPLETO (INCLUIDO EN EL MISMO ARCHIVO)
// -------------------------------------------------------------
class RecuperarViewModel : ViewModel() {

    var emailUsuario by mutableStateOf("")
    var tokenGenerado by mutableStateOf("")
    var tokenIngresado by mutableStateOf("")
    var nuevaPassword by mutableStateOf("")
    var confirmarPassword by mutableStateOf("")
    var mensajeError by mutableStateOf("")

    // Estados de la pantalla:
    // 1 = pedir email
    // 2 = mostrar token generado
    // 3 = pedir nueva password
    var paso by mutableStateOf(1)

    fun verificarEmail(): Boolean {
        if (emailUsuario.isBlank()) {
            mensajeError = "El correo no puede estar vacío."
            return false
        }
        mensajeError = ""
        return true
    }

    fun generarToken() {
        tokenGenerado = (100000..999999).random().toString()
        paso = 2
    }

    fun validarToken(): Boolean {
        if (tokenIngresado != tokenGenerado) {
            mensajeError = "El token no coincide."
            return false
        }
        mensajeError = ""
        return true
    }

    fun cambiarPassword(): Boolean {
        if (nuevaPassword != confirmarPassword) {
            mensajeError = "Las contraseñas no coinciden."
            return false
        }

        mensajeError = ""
        paso = 1
        limpiarCampos()
        return true
    }

    private fun limpiarCampos() {
        tokenGenerado = ""
        tokenIngresado = ""
        nuevaPassword = ""
        confirmarPassword = ""
    }
}

// -------------------------------------------------------------
// UI DE LA PANTALLA COMPLETA — UNA SOLA SCREEN
// -------------------------------------------------------------
@Composable
fun RecuperarContraseñaScreen(
    navController: NavController,                         // <-- AGREGADO
    vm: RecuperarViewModel = remember { RecuperarViewModel() }
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (vm.mensajeError.isNotBlank()) {
            Text(
                text = vm.mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        when (vm.paso) {

            // -------------------------------------------------
            // PASO 1
            // -------------------------------------------------
            1 -> {
                Text("Recuperar contraseña", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(25.dp))

                OutlinedTextField(
                    value = vm.emailUsuario,
                    onValueChange = { vm.emailUsuario = it },
                    label = { Text("Correo del usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (vm.verificarEmail()) {
                            vm.generarToken()
                        }
                    }
                ) {
                    Text("Generar token")
                }
            }

            // -------------------------------------------------
            // PASO 2
            // -------------------------------------------------
            2 -> {
                Text("Token generado", style = MaterialTheme.typography.headlineSmall)

                Spacer(Modifier.height(16.dp))

                Text(
                    text = vm.tokenGenerado,
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(Modifier.height(25.dp))

                OutlinedTextField(
                    value = vm.tokenIngresado,
                    onValueChange = { vm.tokenIngresado = it },
                    label = { Text("Ingresa el token") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (vm.validarToken()) {
                            vm.paso = 3
                        }
                    }
                ) {
                    Text("Validar token")
                }
            }

            // -------------------------------------------------
            // PASO 3
            // -------------------------------------------------
            3 -> {
                Text("Nueva contraseña", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = vm.nuevaPassword,
                    onValueChange = { vm.nuevaPassword = it },
                    label = { Text("Nueva contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = vm.confirmarPassword,
                    onValueChange = { vm.confirmarPassword = it },
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(25.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (vm.cambiarPassword()) {

                            // 🔥 IR AUTOMÁTICAMENTE AL LOGIN
                            navController.navigate(Route.Login.path) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Text("Cambiar contraseña")
                }
            }
        }
    }
}

