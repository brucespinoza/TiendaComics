package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(vm: CatalogoViewModel, onCerrarSesion: () -> Unit) {

    LaunchedEffect(Unit) { vm.cargarComics() }

    val context = LocalContext.current
    val lista by vm.comics.collectAsState()

    var titulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    var tituloError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }

    // Imagen por defecto para nuevos cómics
    val imagenPorDefecto = "comic"
    
    var imagenSeleccionada by remember { mutableStateOf(imagenPorDefecto) }

    var editandoComic by remember { mutableStateOf<ComicEntity?>(null) }

    fun limpiar() {
        titulo = ""
        precio = ""
        descripcion = ""
        imagenSeleccionada = imagenPorDefecto
        tituloError = null
        precioError = null
        descripcionError = null
        editandoComic = null
    }

    fun validar(): Boolean {
        tituloError = if (titulo.isBlank()) "Ingresa un título" else null
        val p = precio.toIntOrNull()
        precioError = when {
            precio.isBlank() -> "Ingresa un precio"
            p == null -> "Debe ser numérico"
            p < 1000 -> "Desde $1000000"
            else -> null
        }
        descripcionError = if (descripcion.isBlank()) "Ingresa una descripción" else null

        return tituloError == null && precioError == null && descripcionError == null
    }

    val formatoCLP = remember {
        NumberFormat.getCurrencyInstance(Locale("es","CL")).apply { maximumFractionDigits = 0 }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8EFF7))
    ) {

        // --------------------------------------
        // TÍTULO TIPO EL CATÁLOGO
        // --------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4AA3DF))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Panel de Administración",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Usuario Administrador", color = Color.White)
                    }
                }
                
                // Botón Cerrar Sesión
                TextButton(onClick = onCerrarSesion) {
                    Text("Cerrar Sesión", color = Color.White)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // --------------------------------------
            // FORMULARIO CON DISEÑO ESTILO CATÁLOGO
            // --------------------------------------
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = if (editandoComic == null)
                                "Crear nuevo cómic"
                            else
                                "Editar cómic",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF1E88E5)
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = titulo,
                            onValueChange = { titulo = it; tituloError = null },
                            label = { Text("Título") },
                            isError = tituloError != null,
                            supportingText = { tituloError?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(
                            value = precio,
                            onValueChange = {
                                precio = it.filter { ch -> ch.isDigit() }.take(8)
                                precioError = null
                            },
                            label = { Text("Precio") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = precioError != null,
                            supportingText = { precioError?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it; descripcionError = null },
                            label = { Text("Descripción") },
                            isError = descripcionError != null,
                            supportingText = { descripcionError?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        Spacer(Modifier.height(10.dp))

                        // Imagen - Solo lectura, muestra "comic" por defecto para nuevos
                        OutlinedTextField(
                            value = if (editandoComic == null) "comic (imagen por defecto)" else imagenSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            label = { Text("Imagen") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = Color(0xFF1E88E5),
                                disabledBorderColor = Color(0xFF90CAF9),
                                disabledLabelColor = Color.Gray
                            )
                        )
                        
                        // Mostrar vista previa de la imagen
                        if (editandoComic == null) {
                            Spacer(Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val previewRes = context.resources.getIdentifier(
                                    "comic", "drawable", context.packageName
                                )
                                if (previewRes != 0) {
                                    Image(
                                        painter = painterResource(id = previewRes),
                                        contentDescription = "Vista previa",
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Vista previa de la imagen",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (!validar()) return@Button

                                if (editandoComic == null) {
                                    // Para nuevos cómics, siempre usa la imagen por defecto "comic"
                                    vm.insertarComic(
                                        ComicEntity(
                                            titulo = titulo.trim(),
                                            precio = precio.toInt(),
                                            descripcion = descripcion.trim(),
                                            imagen = imagenPorDefecto
                                        )
                                    )
                                } else {
                                    vm.actualizarComic(
                                        editandoComic!!.copy(
                                            titulo = titulo.trim(),
                                            precio = precio.toInt(),
                                            descripcion = descripcion.trim(),
                                            imagen = imagenSeleccionada
                                        )
                                    )
                                }
                                limpiar()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(if (editandoComic == null) "Crear cómic" else "Guardar cambios")
                        }

                        if (editandoComic != null) {
                            Spacer(Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { limpiar() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Cancelar edición")
                            }
                        }
                    }
                }
            }

            // --------------------------------------
            // LISTA DE CÓMICS — ESTILO TARJETAS BONITAS
            // --------------------------------------
            items(lista) { comic ->

                val imageRes = context.resources.getIdentifier(
                    comic.imagen, "drawable", context.packageName
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth()
                    ) {
                        // Fila superior: Imagen + Información del cómic
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (imageRes != 0) {
                                Image(
                                    painter = painterResource(id = imageRes),
                                    contentDescription = comic.titulo,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    comic.titulo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    maxLines = 2
                                )
                                Text(
                                    formatoCLP.format(comic.precio),
                                    color = Color(0xFF1E88E5),
                                    fontSize = 16.sp
                                )
                                Text(
                                    comic.descripcion,
                                    fontSize = 13.sp,
                                    color = Color.DarkGray,
                                    maxLines = 2
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Fila inferior: Botones Editar y Eliminar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    editandoComic = comic
                                    titulo = comic.titulo
                                    precio = comic.precio.toString()
                                    descripcion = comic.descripcion
                                    imagenSeleccionada = comic.imagen
                                },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Editar")
                            }

                            Button(
                                onClick = { vm.eliminarComic(comic) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD9534F),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}






