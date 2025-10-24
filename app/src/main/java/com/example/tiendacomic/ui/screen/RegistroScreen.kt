package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion



//Conectamos la pantalla al ViewModel
@Composable
fun RegistroScreenVm(

    onRegistroExitoso: () -> Unit,  // Navegar a Login después de registrar
    onIrLogin: () -> Unit,           // Botón Ya tengo cuenta
    vm: ModeloAutenticacion //base de datos

) {
    //val vm: ModeloAutenticacion = viewModel()
    val estado by vm.registro.collectAsStateWithLifecycle() //base de datos

    if (estado.exito) {
        vm.limpiarResultadoRegistro()
        onRegistroExitoso() //base de datos
    }

    RegistroScreen(
        nombre = estado.nombre,
        rut = estado.rut,
        correo = estado.correo,
        contrasena = estado.contrasena,
        confirmar = estado.confirmar,

        nombreError = estado.errorNombre,
        rutError = estado.errorRut,
        correoError = estado.errorCorreo,
        contrasenaError = estado.errorContrasena,
        confirmarError = estado.errorConfirmar,

        puedeEnviar = estado.puedeEnviar,
        enviando = estado.enviando,
        mensajeError = estado.mensajeError,

        onNombreChange = vm::alCambiarNombre,
        onRutChange = vm::alCambiarRut,
        onCorreoChange = vm::alCambiarCorreoRegistro,
        onContrasenaChange = vm::alCambiarContrasenaRegistro,
        onConfirmarChange = vm::alCambiarConfirmar,

        onRegistrar = vm::enviarRegistro,
        onIrLogin = onIrLogin
    )
}


@Composable
private fun RegistroScreen(
    nombre: String,
    rut: String,
    correo: String,
    contrasena: String,
    confirmar: String,

    nombreError: String?,
    rutError: String?,
    correoError: String?,
    contrasenaError: String?,
    confirmarError: String?,

    puedeEnviar: Boolean,
    enviando: Boolean,
    mensajeError: String?,

    onNombreChange: (String) -> Unit,
    onRutChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onConfirmarChange: (String) -> Unit,

    onRegistrar: () -> Unit,
    onIrLogin: () -> Unit
) {
    val fondo = MaterialTheme.colorScheme.background //fondo
    var mostrarPass by remember { mutableStateOf(false) }
    var mostrarConfirmar by remember { mutableStateOf(false) }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Registro", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))

            //formulario del nombre de usuario
            OutlinedTextField(
                value = nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre completo") },
                singleLine = true,
                isError = nombreError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            nombreError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    softWrap = true
                )
            }


            Spacer(Modifier.height(8.dp))

            //formulario del rut
            OutlinedTextField(
                value = rut,
                onValueChange = onRutChange,
                label = { Text("RUT") },
                singleLine = true,
                isError = rutError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            rutError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    softWrap = true
                )
            }

            Spacer(Modifier.height(8.dp))

            //formulario del correo
            OutlinedTextField(
                value = correo,
                onValueChange = onCorreoChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = correoError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            correoError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    softWrap = true
                )
            }

            Spacer(Modifier.height(8.dp))

            //formulario de la contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = onContrasenaChange,
                label = { Text("Contraseña") },
                singleLine = true,
                isError = contrasenaError != null,
                visualTransformation = if (mostrarPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { mostrarPass = !mostrarPass }) {
                        Icon(
                            imageVector = if (mostrarPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (mostrarPass) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            contrasenaError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    softWrap = true
                )
            }


            Spacer(Modifier.height(8.dp))

            // formulario de copnfirmar
            OutlinedTextField(
                value = confirmar,
                onValueChange = onConfirmarChange,
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                isError = confirmarError != null,
                visualTransformation = if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                        Icon(
                            imageVector = if (mostrarConfirmar) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (mostrarConfirmar) "Ocultar confirmación" else "Mostrar confirmación"

                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            confirmarError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    softWrap = true
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botton registrar
            Button(
                onClick = onRegistrar,
                enabled = puedeEnviar && !enviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (enviando) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Creando cuenta...")
                } else {
                    Text("Registrar")
                }
            }

            mensajeError?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))

            //boton depara que vallas al login
            OutlinedButton(onClick = onIrLogin, modifier = Modifier.fillMaxWidth()) {
                Text("Ya tengo una cuenta")
            }
        }
    }
}


