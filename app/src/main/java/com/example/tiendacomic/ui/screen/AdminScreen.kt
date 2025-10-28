package com.example.tiendacomic.ui.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel

@Composable
fun AdminScreen(
    vm: CatalogoViewModel
) {
    val lista by vm.comics.collectAsState()

    // Campos del formulario
    var titulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Estado para saber si estamos editando o creando
    var editandoComic by remember { mutableStateOf<ComicEntity?>(null) }

    Column(Modifier.padding(16.dp)) {

        Text("ADMIN — Gestión de Cómics", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // === FORMULARIO ===
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

        // === BOTÓN DE GUARDAR (crear o editar) ===
        Button(
            onClick = {
                if (titulo.isNotBlank() && precio.isNotBlank()) {
                    if (editandoComic == null) {
                        // Crear nuevo cómic
                        vm.insertarComic(
                            ComicEntity(
                                titulo = titulo,
                                precio = precio.toInt(),
                                descripcion = descripcion,
                                imagen = "batman"
                            )
                        )
                    } else {
                        // Actualizar cómic existente
                        vm.actualizarComic(
                            editandoComic!!.copy(
                                titulo = titulo,
                                precio = precio.toInt(),
                                descripcion = descripcion
                            )
                        )
                        editandoComic = null
                    }

                    // Limpiar campos
                    titulo = ""
                    precio = ""
                    descripcion = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editandoComic == null) "Crear Cómic" else "Guardar Cambios")
        }

        // === BOTÓN CANCELAR EDICIÓN ===
        if (editandoComic != null) {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    editandoComic = null
                    titulo = ""
                    precio = ""
                    descripcion = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar Edición")
            }
        }

        Spacer(Modifier.height(24.dp))

        // === LISTA DE CÓMICS EXISTENTES ===
        Text("Cómics existentes:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        lista.forEach { comic ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${comic.titulo} — $${comic.precio}")

                Row {
                    // Botón editar
                    OutlinedButton(onClick = {
                        editandoComic = comic
                        titulo = comic.titulo
                        precio = comic.precio.toString()
                        descripcion = comic.descripcion
                    }) {
                        Text("Editar")
                    }

                    Spacer(Modifier.width(8.dp))

                    // Botón eliminar
                    Button(onClick = { vm.eliminarComic(comic) }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}


