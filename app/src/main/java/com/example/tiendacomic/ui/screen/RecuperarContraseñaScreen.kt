package com.example.tiendacomic.ui.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.tiendacomic.navigation.Route
import com.example.tiendacomic.data.remote.ApiConfig
import com.example.tiendacomic.data.remote.dto.ResetPasswordRequest
import com.example.tiendacomic.domain.validaciones.validarContraseña
import com.example.tiendacomic.domain.validaciones.validarConfirmarContraseña
import com.example.tiendacomic.ui.theme.PrimaryBlue
import com.example.tiendacomic.ui.theme.SoftBlue
import kotlinx.coroutines.launch

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
    var cargando by mutableStateOf(false)
    
    // Errores de validación para cada campo (mismas validaciones que el registro)
    var errorNuevaPassword by mutableStateOf<String?>(null)
    var errorConfirmarPassword by mutableStateOf<String?>(null)

    // Estados de la pantalla:
    // 1 = pedir email
    // 2 = mostrar token generado
    // 3 = pedir nueva password
    var paso by mutableStateOf(1)

    private val api = ApiConfig.usuarioApi

    fun verificarEmailYGenerarToken() {
        if (emailUsuario.isBlank()) {
            mensajeError = "El correo no puede estar vacío."
            return
        }

        cargando = true
        mensajeError = ""

        viewModelScope.launch {
            try {
                val response = api.obtenerPorCorreo(emailUsuario.trim())
                if (response.isSuccessful && response.body() != null) {
                    // El correo existe en la base de datos
                    tokenGenerado = (100000..999999).random().toString()
                    paso = 2
                    mensajeError = ""
                } else {
                    // El correo NO existe en la base de datos
                    mensajeError = "El correo no está registrado en el sistema."
                }
            } catch (e: Exception) {
                mensajeError = "Error de conexión. Verifica tu internet."
            } finally {
                cargando = false
            }
        }
    }

    fun validarToken(): Boolean {
        if (tokenIngresado != tokenGenerado) {
            mensajeError = "El token no coincide."
            return false
        }
        mensajeError = ""
        return true
    }
    
    // Validar contraseña en tiempo real (mismas reglas que el registro)
    fun alCambiarNuevaPassword(valor: String) {
        nuevaPassword = valor
        errorNuevaPassword = validarContraseña(valor)
        // También revalidar confirmación si ya tiene algo escrito
        if (confirmarPassword.isNotEmpty()) {
            errorConfirmarPassword = validarConfirmarContraseña(valor, confirmarPassword)
        }
    }
    
    // Validar confirmación en tiempo real
    fun alCambiarConfirmarPassword(valor: String) {
        confirmarPassword = valor
        errorConfirmarPassword = validarConfirmarContraseña(nuevaPassword, valor)
    }
    
    // Verificar si puede enviar el formulario
    val puedeEnviarPassword: Boolean
        get() = errorNuevaPassword == null && 
                errorConfirmarPassword == null && 
                nuevaPassword.isNotBlank() && 
                confirmarPassword.isNotBlank()

    fun cambiarPassword(onExito: () -> Unit) {
        // Validar usando las mismas funciones que el registro
        val errorPass = validarContraseña(nuevaPassword)
        val errorConfirm = validarConfirmarContraseña(nuevaPassword, confirmarPassword)
        
        errorNuevaPassword = errorPass
        errorConfirmarPassword = errorConfirm
        
        if (errorPass != null || errorConfirm != null) {
            mensajeError = "Corrige los errores antes de continuar."
            return
        }

        cargando = true
        mensajeError = ""

        viewModelScope.launch {
            try {
                // Usamos el nuevo endpoint de reset de contraseña
                val request = ResetPasswordRequest(
                    correo = emailUsuario.trim(),
                    nuevaContrasena = nuevaPassword
                )
                val response = api.resetPassword(request)
                
                if (response.isSuccessful) {
                    // Contraseña actualizada exitosamente en la base de datos
                    mensajeError = ""
                    paso = 1
                    limpiarCampos()
                    onExito()
                } else {
                    // Mostrar mensaje más específico según el código de error
                    val errorMessage = when (response.code()) {
                        400 -> "Usuario no encontrado. Verifica el correo."
                        404 -> "El endpoint no fue encontrado. Verifica la conexión."
                        500 -> "Error en el servidor. Intenta más tarde."
                        else -> "Error al actualizar la contraseña (Código: ${response.code()}). Intenta nuevamente."
                    }
                    mensajeError = errorMessage
                    cargando = false
                }
                
            } catch (e: Exception) {
                mensajeError = "Error de conexión: ${e.message ?: "No se pudo conectar con el servidor. Verifica tu internet."}"
                cargando = false
            }
        }
    }

    private fun limpiarCampos() {
        tokenGenerado = ""
        tokenIngresado = ""
        nuevaPassword = ""
        confirmarPassword = ""
        errorNuevaPassword = null
        errorConfirmarPassword = null
    }
}

// -------------------------------------------------------------
// UI DE LA PANTALLA COMPLETA — UNA SOLA SCREEN
// -------------------------------------------------------------
@Composable
fun RecuperarContraseñaScreen(
    navController: NavController,
    vm: RecuperarViewModel = remember { RecuperarViewModel() }
) {
    val fondoDegradado = Brush.verticalGradient(
        colors = listOf(Color(0xFFE3F2FD), SoftBlue, Color.White)
    )

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
                when (vm.paso) {

                    // -------------------------------------------------
                    // PASO 1 - Verificar correo en la base de datos
                    // -------------------------------------------------
                    1 -> {
                        Text(
                            "Recuperar Contraseña",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Ingresa tu correo para recuperar tu contraseña",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(24.dp))

                        // Mensaje de error general
                        if (vm.mensajeError.isNotBlank()) {
                            Text(
                                text = vm.mensajeError,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        // Campo correo
                        OutlinedTextField(
                            value = vm.emailUsuario,
                            onValueChange = { vm.emailUsuario = it },
                            label = { Text("Correo electrónico") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryBlue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !vm.cargando,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(Modifier.height(24.dp))

                        // Botón verificar
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            onClick = { vm.verificarEmailYGenerarToken() },
                            enabled = !vm.cargando,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (vm.cargando) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Verificando...", color = Color.White)
                            } else {
                                Text(
                                    "Verificar correo",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Botón volver
                        TextButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text("Volver al login", color = PrimaryBlue)
                        }
                    }

                    // -------------------------------------------------
                    // PASO 2 - Token generado (solo si el correo existe)
                    // -------------------------------------------------
                    2 -> {
                        Text(
                            "Token de Verificación",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Correo verificado: ${vm.emailUsuario}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(24.dp))

                        // Token generado destacado
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.1f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tu código de verificación",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = vm.tokenGenerado,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBlue
                                    )
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // Mensaje de error
                        if (vm.mensajeError.isNotBlank()) {
                            Text(
                                text = vm.mensajeError,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        // Campo token
                        OutlinedTextField(
                            value = vm.tokenIngresado,
                            onValueChange = { vm.tokenIngresado = it },
                            label = { Text("Ingresa el token") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(Modifier.height(24.dp))

                        // Botón validar
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            onClick = {
                                if (vm.validarToken()) {
                                    vm.paso = 3
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Validar token",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // -------------------------------------------------
                    // PASO 3 - Nueva contraseña (mismas validaciones que registro)
                    // -------------------------------------------------
                    3 -> {
                        var mostrarPass by remember { mutableStateOf(false) }
                        var mostrarConfirmar by remember { mutableStateOf(false) }

                        Text(
                            "Nueva Contraseña",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Ingresa tu nueva contraseña",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "8+ caracteres, mayúscula, minúscula, número y símbolo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp
                        )

                        Spacer(Modifier.height(24.dp))

                        // Mensaje de error general
                        if (vm.mensajeError.isNotBlank()) {
                            Text(
                                text = vm.mensajeError,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        // Campo nueva contraseña con validación
                        OutlinedTextField(
                            value = vm.nuevaPassword,
                            onValueChange = { vm.alCambiarNuevaPassword(it) },
                            label = { Text("Nueva contraseña") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !vm.cargando,
                            singleLine = true,
                            isError = vm.errorNuevaPassword != null,
                            visualTransformation = if (mostrarPass) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { mostrarPass = !mostrarPass }) {
                                    Icon(
                                        imageVector = if (mostrarPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (mostrarPass) "Ocultar" else "Mostrar",
                                        tint = PrimaryBlue
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Mostrar errores de validación de contraseña
                        vm.errorNuevaPassword?.let { error ->
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Campo confirmar contraseña con validación
                        OutlinedTextField(
                            value = vm.confirmarPassword,
                            onValueChange = { vm.alCambiarConfirmarPassword(it) },
                            label = { Text("Confirmar contraseña") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryBlue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !vm.cargando,
                            singleLine = true,
                            isError = vm.errorConfirmarPassword != null,
                            visualTransformation = if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                                    Icon(
                                        imageVector = if (mostrarConfirmar) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (mostrarConfirmar) "Ocultar" else "Mostrar",
                                        tint = PrimaryBlue
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Mostrar errores de confirmación
                        vm.errorConfirmarPassword?.let { error ->
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // Botón cambiar contraseña
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            onClick = {
                                vm.cambiarPassword {
                                    // 🔥 IR AUTOMÁTICAMENTE AL LOGIN
                                    navController.navigate(Route.Login.path) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            },
                            enabled = vm.puedeEnviarPassword && !vm.cargando,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (vm.cargando) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Guardando...", color = Color.White)
                            } else {
                                Text(
                                    "Cambiar contraseña",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

