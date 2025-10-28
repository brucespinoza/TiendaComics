package com.example.tiendacomic.ui.screen



import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import com.example.tiendacomic.R

@Composable
fun AdminScreen(

    vm: CatalogoViewModel
) {  // Obtenemos la lista actual de cómics desde sqlite en tiempo real
    val lista by vm.comics.collectAsState()
    // Estados locales para los campos del formulario

    var titulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        Text("ADMIN — Gestión de Cómics", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))
        // Formulario
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        //Botton crear comic validar el titulo y precio que no esten vacios

        Button(
            onClick = {
                if (titulo.isNotBlank() && precio.isNotBlank()) {
                    vm.insertarComic(
                        ComicEntity(
                            titulo = titulo,
                            precio = precio.toInt(),
                            descripcion = descripcion,
                            imagen = "batman",
                        )
                    )//aqui son el formularioi
                    titulo = ""; precio = ""; descripcion = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Cómic")
        }

        Spacer(Modifier.height(24.dp))
        //lista de comic existente
        Text("Cómics existentes:", style = MaterialTheme.typography.titleMedium)


        Spacer(Modifier.height(8.dp))
        //aqui recorremos la lista  por comic aqui tambien eleminamos

        lista.forEach { comic ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${comic.titulo} — $${comic.precio}")
                Button(onClick = { vm.eliminarComic(comic) }) {
                    Text("Eliminar")
                }
            }
        }
    }
}


