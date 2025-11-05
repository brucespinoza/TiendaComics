package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(vm: CatalogoViewModel) {

    //  Carga inicial de cómics
    LaunchedEffect(Unit) { vm.cargarComics() }

    val context = LocalContext.current
    val lista by vm.comics.collectAsState()

    // ---------- VARIABLES DEL FORMULARIO ----------
    var titulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // ---------- ERRORES DE VALIDACIÓN ----------
    var tituloError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }

    // ---------- IMÁGENES DISPONIBLES ----------
    val imagenesDisponibles = listOf(
        "batman", "avenger", "spiderman", "ironman", "narnia",
        "ranger", "aliciaa", "fantasticos", "men",
    )
    var imagenSeleccionada by remember { mutableStateOf(imagenesDisponibles.first()) }
    var menuAbierto by remember { mutableStateOf(false) }

    // ---------- MODO EDICIÓN ----------
    var editandoComic by remember { mutableStateOf<ComicEntity?>(null) }

    // ---------- FUNCIÓN PARA LIMPIAR FORMULARIO ----------
    fun limpiarFormulario() {
        titulo = ""
        precio = ""
        descripcion = ""
        imagenSeleccionada = imagenesDisponibles.first()
        tituloError = null
        precioError = null
        descripcionError = null
        editandoComic = null
    }

    // ---------- VALIDACIÓN DE CAMPOS ----------
    fun validar(): Boolean {
        // Titulo
        tituloError = when {
            titulo.isBlank() -> "Ingresa un título"
            !titulo.any { it.isLetter() } -> "Debe contener letras"
            else -> null
        }

        // Precio: número, mayor o igual a 1000
        val p = precio.toIntOrNull()
        precioError = when {
            precio.isBlank() -> "Ingresa un precio"
            p == null -> "Debe ser numérico"
            p < 1000 -> "Debe ser al menos $1000"
            else -> null
        }

        //  Descripcion obligatoria, sin solo números
        descripcionError = when {
            descripcion.isBlank() -> "Ingresa una descripción"
            descripcion.all { it.isDigit() } -> "No puede ser solo números"
            else -> null
        }

        return tituloError == null && precioError == null && descripcionError == null
    }

    // ---------- FORMATO DE MONEDA ----------
    val formatoCLP = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    // ---------- SCROLL PRINCIPAL ----------
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ---------- FORMULARIO ----------
        item {
            Text("ADMIN — Gestión de Cómics", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))

            // Campo TÍTULO
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    tituloError = null
                },
                label = { Text("Título") },
                isError = tituloError != null,
                supportingText = { if (tituloError != null) Text(tituloError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Campo PRECIO (solo números)
            OutlinedTextField(
                value = precio,
                onValueChange = {
                    val soloDigitos = it.filter { ch -> ch.isDigit() }.take(7)
                    precio = soloDigitos
                    precioError = null
                },
                label = { Text("Precio (número entero)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = precioError != null,
                supportingText = { if (precioError != null) Text(precioError!!) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            // Campo DESCRIPCIÓN
            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    descripcionError = null
                },
                label = { Text("Descripción") },
                isError = descripcionError != null,
                supportingText = { if (descripcionError != null) Text(descripcionError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Selector de IMAGEN
            ExposedDropdownMenuBox(
                expanded = menuAbierto,
                onExpandedChange = { menuAbierto = !menuAbierto }
            ) {
                OutlinedTextField(
                    value = imagenSeleccionada,
                    onValueChange = {},
                    label = { Text("Imagen (drawable)") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }
                ) {
                    imagenesDisponibles.forEach { nombre ->
                        DropdownMenuItem(
                            text = { Text(nombre) },
                            onClick = {
                                imagenSeleccionada = nombre
                                menuAbierto = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Botón GUARDAR / CREAR
            Button(
                onClick = {
                    if (!validar()) return@Button
                    val precioNum = precio.toInt()

                    if (editandoComic == null) {
                        vm.insertarComic(
                            ComicEntity(
                                titulo = titulo.trim(),
                                precio = precioNum,
                                descripcion = descripcion.trim(),
                                imagen = imagenSeleccionada
                            )
                        )
                    } else {
                        vm.actualizarComic(
                            editandoComic!!.copy(
                                titulo = titulo.trim(),
                                precio = precioNum,
                                descripcion = descripcion.trim(),
                                imagen = imagenSeleccionada
                            )
                        )
                    }
                    limpiarFormulario()
                },
                enabled = titulo.isNotBlank() && precio.isNotBlank() && descripcion.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editandoComic == null) "Crear cómic" else "Guardar cambios")
            }

            // Botón CANCELAR (solo si estás editando)
            if (editandoComic != null) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { limpiarFormulario() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Cancelar edición") }
            }

            Spacer(Modifier.height(16.dp))
            Text("Cómics existentes", style = MaterialTheme.typography.titleMedium)
        }

        // ---------- LISTA DE CÓMICS ----------
        items(lista) { comic ->
            val imageRes = remember(comic.imagen) {
                context.resources.getIdentifier(comic.imagen, "drawable", context.packageName)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 🖼️ IMAGEN + DETALLES
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (imageRes != 0) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = comic.titulo,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                        Column {
                            Text(comic.titulo, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Precio: ${formatoCLP.format(comic.precio)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (comic.descripcion.isNotBlank()) {
                                Text(comic.descripcion, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    // 🧩 BOTONES (Editar / Eliminar en columna)
                    val actionWidth = 140.dp
                    Column(
                        modifier = Modifier.width(actionWidth),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        //  EDITAR
                        OutlinedButton(
                            onClick = {
                                editandoComic = comic
                                titulo = comic.titulo
                                precio = comic.precio.toString()
                                descripcion = comic.descripcion
                                imagenSeleccionada = comic.imagen
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Editar", maxLines = 1) }

                        // ELIMINAR
                        Button(
                            onClick = { vm.eliminarComic(comic) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) { Text("Eliminar", maxLines = 1) }
                    }
                }
            }
        }


        item { Spacer(Modifier.height(72.dp)) }
    }
}







