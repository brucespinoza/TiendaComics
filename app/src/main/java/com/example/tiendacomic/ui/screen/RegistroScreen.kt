package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion


// =============================
//   VIEWMODEL CONEXIÓN
// =============================
@Composable
fun RegistroScreenVm(
    onRegistroExitoso: () -> Unit,
    onIrLogin: () -> Unit,
    vm: ModeloAutenticacion
) {
    val estado by vm.registro.collectAsStateWithLifecycle()

    if (estado.exito) {
        vm.limpiarResultadoRegistro()
        onRegistroExitoso()
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


// =============================
//   DISEÑO COMPLETAMENTE NUEVO
// =============================
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
    var mostrarPass by remember { mutableStateOf(false) }
    var mostrarConfirmar by remember { mutableStateOf(false) }

    // FONDO GENERAL CELESTE
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8EFF7))
    ) {

        // =============================
        //  BARRA SUPERIOR AZUL
        // =============================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4AA3DF))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Crear Cuenta",
                    color = Color.White,
                    fontSize = 26.sp,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Regístrate para continuar",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }

        // =============================
        //     FORMULARIO EN CARD
        // =============================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "Registro de usuario",
                        fontSize = 20.sp,
                        color = Color(0xFF1E88E5),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(14.dp))

                    // =============================
                    //        NOMBRE
                    // =============================
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = onNombreChange,
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        isError = nombreError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                    )
                    nombreError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(Modifier.height(10.dp))

                    // =============================
                    //        RUT
                    // =============================
                    OutlinedTextField(
                        value = rut,
                        onValueChange = onRutChange,
                        label = { Text("RUT") },
                        singleLine = true,
                        isError = rutError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                    )
                    rutError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(Modifier.height(10.dp))

                    // =============================
                    //        CORREO
                    // =============================
                    OutlinedTextField(
                        value = correo,
                        onValueChange = onCorreoChange,
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        isError = correoError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                    )
                    correoError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(Modifier.height(10.dp))

                    // =============================
                    //        CONTRASEÑA
                    // =============================
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = onContrasenaChange,
                        label = { Text("Contraseña") },
                        singleLine = true,
                        isError = contrasenaError != null,
                        visualTransformation =
                            if (mostrarPass) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarPass = !mostrarPass }) {
                                Icon(
                                    imageVector =
                                        if (mostrarPass) Icons.Filled.VisibilityOff
                                        else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                    )
                    contrasenaError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(Modifier.height(10.dp))

                    // =============================
                    //        CONFIRMAR PASS
                    // =============================
                    OutlinedTextField(
                        value = confirmar,
                        onValueChange = onConfirmarChange,
                        label = { Text("Confirmar contraseña") },
                        singleLine = true,
                        isError = confirmarError != null,
                        visualTransformation =
                            if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                                Icon(
                                    imageVector =
                                        if (mostrarConfirmar) Icons.Filled.VisibilityOff
                                        else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                    )
                    confirmarError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(Modifier.height(18.dp))

                    // =============================
                    //       BOTÓN REGISTRAR
                    // =============================
                    Button(
                        onClick = onRegistrar,
                        enabled = puedeEnviar && !enviando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (enviando) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
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

                    Spacer(Modifier.height(14.dp))

                    // =============================
                    //       BOTÓN LOGIN
                    // =============================
                    OutlinedButton(
                        onClick = onIrLogin,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Ya tengo una cuenta")
                    }
                }
            }
        }
    }
}
