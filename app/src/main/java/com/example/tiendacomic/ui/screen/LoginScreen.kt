package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tiendacomic.R
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import com.example.tiendacomic.data.storage.UserPreferences
import com.example.tiendacomic.ui.theme.PrimaryBlue
import com.example.tiendacomic.ui.theme.SoftBlue
import androidx.navigation.NavController
import com.example.tiendacomic.navigation.Route



// ---------- PANTALLA CONECTADA AL VIEWMODEL ----------
@Composable
fun LoginScreenVm(
    navController: NavController,        // <--- AGREGADO
    onLoginExitoso: () -> Unit,
    onIrRegistro: () -> Unit,
    vm: ModeloAutenticacion
) {
    val state by vm.login.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    LaunchedEffect(state.exito) {
        if (state.exito) {
            userPrefs.setLoggedIn(true)
            vm.limpiarResultadoLogin()
            onLoginExitoso()
        }
    }

    LoginScreen(
        navController = navController,     // <--- AGREGADO
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
    navController: NavController,      // <--- AGREGADO
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

    val fondoDegradado = Brush.verticalGradient(
        colors = listOf(Color(0xFFE3F2FD), SoftBlue, Color.White)
    )

    var mostrarDialogo by remember { mutableStateOf(false) }
    var correoRecuperacion by remember { mutableStateOf("") }
    var mensajeEnviado by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoDegradado)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = "Logo de tienda de cómics",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 12.dp)
                )

                Text(
                    "Bienvenidos A La Tienda De Comic",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Inicia sesión para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                Spacer(Modifier.height(24.dp))


                // ---------- CORREO ----------
                OutlinedTextField(
                    value = correo,
                    onValueChange = onCorreoChange,
                    label = { Text("Correo electrónico") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryBlue)
                    },
                    singleLine = true,
                    isError = errorCorreo != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorCorreo != null) {
                    Text(errorCorreo, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(Modifier.height(12.dp))


                // ---------- CONTRASEÑA ----------
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = onContrasenaChange,
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                    },
                    singleLine = true,
                    visualTransformation =
                        if (mostrarPass) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { mostrarPass = !mostrarPass }) {
                            Icon(
                                if (mostrarPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        }
                    },
                    isError = errorContrasena != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorContrasena != null) {
                    Text(errorContrasena, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(Modifier.height(24.dp))


                // ---------- BOTÓN ENTRAR ----------
                Button(
                    onClick = onSubmit,
                    enabled = puedeEnviar && !enviando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (enviando) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Validando...")
                    } else {
                        Text(
                            "Entrar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                if (mensajeError != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(mensajeError, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(16.dp))


                // ---------- OLVIDÉ CONTRASEÑA ----------
                TextButton(
                    onClick = {
                        navController.navigate(Route.RecuperarContraseña.path)
                    }
                ) {
                    Text("¿Olvidaste tu contraseña?", color = PrimaryBlue)
                }


                Spacer(Modifier.height(12.dp))


                // ---------- CREAR CUENTA ----------
                OutlinedButton(
                    onClick = onIrRegistro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue)
                ) {
                    Text("Crear cuenta", fontWeight = FontWeight.SemiBold)
                }

            }
        }
    }
}

