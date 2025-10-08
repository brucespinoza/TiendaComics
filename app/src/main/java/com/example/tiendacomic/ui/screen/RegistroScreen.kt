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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp
//importaciones nuevas
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button


@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onIrLogin: ()-> Unit,
    modifier: Modifier = Modifier){

    //color en color.kt y theme.kt
    val bg = MaterialTheme.colorScheme.background

    //Estado para los campo
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var confirmarContraseña by remember { mutableStateOf("") }
    var contraseñaVisible by remember { mutableStateOf(false) }
    var confirmarContraseñaVisible by remember { mutableStateOf(false) }

    //aqui estan la validacion para el campo de contraseña
        fun validarContraseña(contraseña: String): Boolean {
        val tieneMinuscula = contraseña.any { it.isLowerCase() }
        val tieneMayuscula = contraseña.any { it.isUpperCase() }
        val tieneNumero = contraseña.any { it.isDigit() }
        val tieneEspecial = contraseña.any { !it.isLetterOrDigit() }
        val longitudValida = contraseña.length >= 8

        return tieneMinuscula && tieneMayuscula && tieneNumero && tieneEspecial && longitudValida


    }

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
                text = "Registro",
                style = MaterialTheme.typography.headlineSmall, // Título
                //color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))


            //aqui esta los formularios de correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, //hace que sea todo en una linea
                placeholder = { Text("ejemplo@correo.com") }, //aqui aparece un ejemplo
                isError = correo.isNotEmpty() && !gmailValido(correo) //si no puso @ sale rojo abajo esta el if

            )
            if (correo.isNotEmpty() && !gmailValido(correo)) {
                Text(
                    text = "Solo se permiten gmail válidos (ejemplo@gmail.com)",
                    color = Color.Red
                )
        }
            Spacer(modifier = Modifier.height(16.dp))

            //formulario de contraseña
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = contraseña.isNotEmpty() && !validarContraseña(contraseña)
            )

            if (contraseña.isNotEmpty() && !validarContraseña(contraseña)) {
                Text(
                    text = "La contraseña debe contener al menos una minúscula, una mayúscula, un número, un caracter especial y mínimo 8 caracteres",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                    )
            }
            Spacer(modifier = Modifier.height(16.dp))
            //campo de confirmar contraseña
            OutlinedTextField(
                value = confirmarContraseña,
                onValueChange =  {confirmarContraseña = it },
                label = {Text("Confirmar contraseña")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("repita su contraseña") },
                visualTransformation = if (confirmarContraseñaVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmarContraseñaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmarContraseñaVisible = !confirmarContraseñaVisible }) {
                        Icon(imageVector = image, contentDescription = "Mostrar/ocultar contraseña")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmarContraseña.isNotEmpty() && confirmarContraseña != contraseña


            )
            if (confirmarContraseña.isNotEmpty() && confirmarContraseña != contraseña) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {onRegistroExitoso()},
                modifier = Modifier.fillMaxWidth(),

                enabled = correo.isNotEmpty() && correo.contains("@") &&
                validarContraseña(contraseña) &&
                contraseña == confirmarContraseña
            ) { Text("Registrate")

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ya tienes una cuenta registrada? click aqui",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                .clickable{onIrLogin()}
            )




        }
}
}