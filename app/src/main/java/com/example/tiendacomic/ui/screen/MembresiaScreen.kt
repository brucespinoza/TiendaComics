package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tiendacomic.R
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembresiaScreen() {
    val formatoCLP = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val precio = 4990

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

            Button(
                onClick = { /* TODO: comprar membresía */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comprar membresía")
            }
        }
    }
}
