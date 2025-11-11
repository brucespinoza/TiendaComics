package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tiendacomic.R
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembresiaScreen(authVm: ModeloAutenticacion) {
    val formatoCLP = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val precio = 49990

    val esVip = authVm.verificarVip()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Membresía Premium") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono),
                contentDescription = "Membresía Premium",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Accede a todos los cómics digitales, descuentos exclusivos y contenido ilimitado.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Precio: ${formatoCLP.format(precio)}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (esVip) {
                Text(
                    text = "🎉 Ya eres miembro VIP. Tu membresía vence el: ${
                        authVm.perfilUiState.value.vipExpiracion?.let {
                            java.text.SimpleDateFormat("dd/MM/yyyy").format(it)
                        } ?: "desconocida"
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Button(
                    onClick = {
                        authVm.activarVip()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Comprar membresía")
                }
            }
        }
    }
}
