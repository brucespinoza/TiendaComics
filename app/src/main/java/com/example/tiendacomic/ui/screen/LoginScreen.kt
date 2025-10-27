package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

// ---------- PANTALLA CONECTADA AL VIEWMODEL ----------
@Composable
fun LoginScreenVm(
    onLoginExitoso: () -> Unit,
    onIrRegistro: () -> Unit,
    vm: ModeloAutenticacion
) {
    val state by vm.login.collectAsStateWithLifecycle()

    if (state.exito) {
        vm.limpiarResultadoLogin()
        onLoginExitoso()
    }

    LoginScreen(
        correo = state.correo,
        contrasena = state.contrasena,
        errorCorreo = state.errorCorreo,
        errorContrasena = state.errorContrasena,
        puedeEnviar = state.puedeEnviar,
        enviando = state.enviando,
        mensajeError = state.mensajeError,
        onCorreoChange = vm::alCambiarCorreoLogin,
        onContrasenaChange = vm::alCambiarContrasenaLogin,
        onSubmit = vm::enviarLogin,
        onIrRegistro = onIrRegistro
    )
}

// ---------- UI PRESENTACIONAL ----------
@Composable
private fun LoginScreen(
    correo: String,
    contrasena: String,
    errorCorreo: String?,
    errorContrasena: String?,
    puedeEnviar: Boolean,
    enviando: Boolean,
    mensajeError: String?,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onIrRegistro: () -> Unit
) {
    var mostrarPass by remember { mutableStateOf(false) }
    val fondo = MaterialTheme.colorScheme.background

    // Estados para el diálogo de recuperación
    var mostrarDialogo by remember { mutableStateOf(false) }
    var correoRecuperacion by remember { mutableStateOf("") }
    var mensajeEnviado by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Ingresa con tu correo y contraseña.",
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            // ---------- CORREO ----------
            OutlinedTextField(
                value = correo,
                onValueChange = onCorreoChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = errorCorreo != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            if (errorCorreo != null) {
                Text(
                    errorCorreo,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(8.dp))

            // ---------- CONTRASEÑA ----------
            OutlinedTextField(
                value = contrasena,
                onValueChange = onContrasenaChange,
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (mostrarPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { mostrarPass = !mostrarPass }) {
                        Icon(
                            imageVector = if (mostrarPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (mostrarPass) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                isError = errorContrasena != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorContrasena != null) {
                Text(
                    errorContrasena,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(16.dp))

            // ---------- BOTÓN ENTRAR ----------
            Button(
                onClick = onSubmit,
                enabled = puedeEnviar && !enviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (enviando) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Validando...")
                } else {
                    Text("Entrar")
                }
            }

            if (mensajeError != null) {
                Spacer(Modifier.height(8.dp))
                Text(mensajeError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))

            // ---------- OLVIDÉ MI CONTRASEÑA ----------
            TextButton(onClick = { mostrarDialogo = true }) {
                Text("¿Olvidaste tu contraseña?")
            }

            Spacer(Modifier.height(12.dp))

            // ---------- IR A REGISTRO ----------
            OutlinedButton(onClick = onIrRegistro, modifier = Modifier.fillMaxWidth()) {
                Text("Crear cuenta")
            }
        }

        // ---------- DIALOGO DE RECUPERACIÓN ----------
        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("Recuperar contraseña") },
                text = {
                    Column {
                        Text("Introduce tu correo para recuperar tu contraseña:")
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = correoRecuperacion,
                            onValueChange = { correoRecuperacion = it },
                            label = { Text("Correo electrónico") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        if (mensajeEnviado) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Se envió un enlace de recuperación al correo ingresado.",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (correoRecuperacion.isNotBlank()) {
                            mensajeEnviado = true
                        }
                    }) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false; mensajeEnviado = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}
