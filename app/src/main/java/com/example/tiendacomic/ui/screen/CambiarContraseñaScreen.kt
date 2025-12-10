package com.example.tiendacomic.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tiendacomic.ui.theme.PrimaryBlue
import com.example.tiendacomic.domain.validaciones.validarContraseña
import com.example.tiendacomic.domain.validaciones.validarConfirmarContraseña
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion

@Composable
fun CambiarContraseñaScreen(
    navController: NavHostController,
    vm: ModeloAutenticacion
) {
    val context = LocalContext.current
    val estado = vm.perfilUiState.collectAsState().value

    var contrasenaActual by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var mostrarActual by remember { mutableStateOf(false) }
    var mostrarNueva by remember { mutableStateOf(false) }
    var mostrarConfirmar by remember { mutableStateOf(false) }

    var errorActual by remember { mutableStateOf<String?>(null) }
    var errorNueva by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }

    var cargando by remember { mutableStateOf(false) }
    var mensajeErrorGeneral by remember { mutableStateOf("") }

    // Validación en tiempo real
    LaunchedEffect(nuevaContrasena) {
        errorNueva = validarContraseña(nuevaContrasena)
    }

    LaunchedEffect(confirmarContrasena, nuevaContrasena) {
        errorConfirmar = validarConfirmarContraseña(confirmarContrasena, nuevaContrasena)
    }

    val puedeEnviar = contrasenaActual.isNotBlank() &&
            nuevaContrasena.isNotBlank() &&
            confirmarContrasena.isNotBlank() &&
            errorNueva == null &&
            errorConfirmar == null &&
            !cargando

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color.White)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Barra superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4AA3DF), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Cambiar Contraseña",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Actualiza tu contraseña de acceso",
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }

            // Texto informativo sobre requisitos (igual que RecuperarContraseñaScreen)
            Text(
                text = "8+ caracteres, mayúscula, minúscula, número y símbolo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Card con formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(8.dp))

                    // Campo: Contraseña actual
                    OutlinedTextField(
                        value = contrasenaActual,
                        onValueChange = {
                            contrasenaActual = it
                            errorActual = null
                        },
                        label = { Text("Contraseña actual") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (mostrarActual) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { mostrarActual = !mostrarActual }) {
                                Icon(
                                    imageVector = if (mostrarActual) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (mostrarActual) "Ocultar" else "Mostrar",
                                    tint = PrimaryBlue
                                )
                            }
                        },
                        isError = errorActual != null,
                        enabled = !cargando,
                        singleLine = true
                    )
                    if (errorActual != null) {
                        Text(
                            text = errorActual!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Campo: Nueva contraseña
                    OutlinedTextField(
                        value = nuevaContrasena,
                        onValueChange = {
                            nuevaContrasena = it
                        },
                        label = { Text("Nueva contraseña") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (mostrarNueva) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { mostrarNueva = !mostrarNueva }) {
                                Icon(
                                    imageVector = if (mostrarNueva) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (mostrarNueva) "Ocultar" else "Mostrar",
                                    tint = PrimaryBlue
                                )
                            }
                        },
                        isError = errorNueva != null,
                        enabled = !cargando,
                        singleLine = true
                    )
                    if (errorNueva != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = errorNueva!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Campo: Confirmar nueva contraseña
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                        },
                        label = { Text("Confirmar nueva contraseña") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                                Icon(
                                    imageVector = if (mostrarConfirmar) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (mostrarConfirmar) "Ocultar" else "Mostrar",
                                    tint = PrimaryBlue
                                )
                            }
                        },
                        isError = errorConfirmar != null,
                        enabled = !cargando,
                        singleLine = true
                    )
                    if (errorConfirmar != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = errorConfirmar!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // Mensaje de error general (si existe)
                    if (mensajeErrorGeneral.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = mensajeErrorGeneral,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Botón: Actualizar
                    Button(
                        onClick = {
                            // Validaciones antes de enviar (mismas que RecuperarContraseñaScreen)
                            val errorPass = validarContraseña(nuevaContrasena)
                            val errorConfirm = validarConfirmarContraseña(nuevaContrasena, confirmarContrasena)
                            
                            if (errorPass != null) {
                                errorNueva = errorPass
                                mensajeErrorGeneral = "Corrige los errores antes de continuar."
                                return@Button
                            }
                            
                            if (errorConfirm != null) {
                                errorConfirmar = errorConfirm
                                mensajeErrorGeneral = "Corrige los errores antes de continuar."
                                return@Button
                            }
                            
                            if (contrasenaActual.isBlank()) {
                                errorActual = "La contraseña actual es obligatoria"
                                mensajeErrorGeneral = "Ingresa tu contraseña actual."
                                return@Button
                            }

                            cargando = true
                            mensajeErrorGeneral = ""
                            
                            vm.cambiarContrasena(
                                actual = contrasenaActual,
                                nueva = nuevaContrasena,
                                confirmar = confirmarContrasena,
                                onExito = {
                                    // Callback de éxito
                                    cargando = false
                                    Toast.makeText(context, "✅ Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onError = { error ->
                                    // Callback de error
                                    cargando = false
                                    if (error.contains("actual incorrecta")) {
                                        errorActual = "Contraseña actual incorrecta"
                                    } else {
                                        mensajeErrorGeneral = error
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = puedeEnviar && !cargando,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            disabledContainerColor = Color.Gray
                        )
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Actualizando...", color = Color.White)
                        } else {
                            Text(
                                "Actualizar contraseña",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Botón: Cancelar
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4AA3DF)
                        )
                    ) {
                        Text(
                            "Cancelar",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

