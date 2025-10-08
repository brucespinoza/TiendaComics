package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrRegistro: () -> Unit,
    modifier: Modifier = Modifier
){

    val bg = MaterialTheme.colorScheme.background

    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var contraseñaVisible by remember { mutableStateOf(false) }

    fun gmailValido(gmail: String): Boolean {
        val gmailValido = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$")
        return gmailValido.matches(gmail)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "Inicio de seccion",
                style = MaterialTheme.typography.headlineSmall,

            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("ejemplo@gmail.com") },
                isError = correo.isNotEmpty() && !gmailValido(correo)
            )

            if (correo.isNotEmpty() && !gmailValido(correo)) {
                Text(
                    text = "Solo se permiten correos Gmail válidos",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Ingrese su contraseña") },
                visualTransformation = if (contraseñaVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (contraseñaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { contraseñaVisible = !contraseñaVisible }) {
                        Icon(imageVector = image, contentDescription = "Mostrar/ocultar contraseña")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

                Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {onLoginExitoso()},
                modifier = Modifier.fillMaxWidth(),
                enabled = correo.isNotEmpty() && gmailValido(correo) && contraseña.isNotEmpty()

            ) { Text("Inicia Seccion")}

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "no tienes una cuenta? clickea aqui,",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onIrRegistro() }
            )
        }



    }
}