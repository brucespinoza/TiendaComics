package com.example.tiendacomic.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendacomic.R
import com.example.tiendacomic.navigation.Route
import com.example.tiendacomic.ui.components.AppTopBar
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel  // === PASO 2: import del ViewModel ===
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

data class Comic(
    val id: Int,
    val titulo: String,
    val precio: Int,
    val imagenRes: Int,
    val descripcion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    vm: CatalogoViewModel = viewModel() // === PASO 2: Inyectamos el ViewModel ===
) {

    val listaComics = listOf(
        Comic(1, "Membresía Premium", 49990, R.drawable.vip, "Acceso ilimitado a todos los cómics digitales, descuentos exclusivos y más."),
        Comic(2, "Batman: Año Uno", 12490, R.drawable.batman, "Los primeros días de Bruce Wayne como vigilante."),
        Comic(3, "Avengers: Endgame", 18990, R.drawable.avenger, "Los Vengadores intentan revertir el chasquido de Thanos."),
        Comic(4, "X-Men: Dark Phoenix", 14990, R.drawable.men, "Jean Grey pierde el control de sus poderes."),
        Comic(5, "Iron Man: Extremis", 13490, R.drawable.ironman, "Tony Stark enfrenta un virus nanotecnológico."),
        Comic(6, "4 Fantásticos #1", 24990, R.drawable.fantasticos, "Los héroes más grandes de Marvel."),
        Comic(7, "Narnia", 16990, R.drawable.narnia, "Un mundo mágico lleno de criaturas fantásticas."),
        Comic(8, "Alicia en el país de las maravillas #1", 18990, R.drawable.alicia, "Alicia cae en un mundo surrealista."),
        Comic(9, "Power Ranger #1", 17990, R.drawable.ranger, "Un grupo de jóvenes héroes defiende la Tierra."),
        Comic(10, "Spider-Man #1", 15990, R.drawable.spiderman, "Peter Parker enfrenta su mayor desafío.")
    )

    var textoBusqueda by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val comicsFiltrados = listaComics.filter {
        it.titulo.contains(textoBusqueda, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                onCatalogo = { navController.navigate(Route.Catalogo.path) },
                onPerfil = { navController.navigate(Route.Perfil.path) },
                onLogin = { navController.navigate(Route.Login.path) },
                onRegistro = { navController.navigate(Route.Registro.path) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Catálogo de Cómics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                label = { Text("Buscar cómic...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comicsFiltrados) { comic ->
                    ComicCard(
                        comic = comic,
                        esVip = vm.esVip,  // === PASO 4: le pasamos el estado VIP ===
                        onVerMas = { /* abre el diálogo */ },
                        onComprar = {
                            // === PASO 3: detectar compra del VIP ===
                            if (comic.id == 1) {
                                vm.activarVip() // activa membresía VIP
                            }
                            scope.launch {
                                snackbarHostState.showSnackbar("✅ Compra realizada con éxito")
                            }
                        }
                    )
                }
            }

            if (comicsFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron cómics", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun ComicCard(
    comic: Comic,
    esVip: Boolean, // === PASO 4: recibimos si el usuario es VIP ===
    onVerMas: () -> Unit,
    onComprar: () -> Unit
) {
    var seleccionado by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val escala by animateFloatAsState(
        targetValue = if (seleccionado) 1.05f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    // === PASO 4: aplicar descuento si es VIP (excepto en la membresía misma) ===
    val precioFinal = if (esVip && comic.id != 1) {
        (comic.precio * 0.7).toInt() // 30% de descuento
    } else {
        comic.precio
    }

    val formatoCLP = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0
    }
    val precioFormateado = formatoCLP.format(precioFinal)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .graphicsLayer(scaleX = escala, scaleY = escala)
            .clickable {
                seleccionado = true
                mostrarDialogo = true
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = comic.imagenRes),
                contentDescription = comic.titulo,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = comic.titulo, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(text = precioFormateado, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { mostrarDialogo = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver más")
            }

            OutlinedButton(
                onClick = onComprar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text("Finalizar compra")
            }
        }
    }

    if (mostrarDialogo) {
        ComicDialog(
            comic = comic,
            onDismiss = { mostrarDialogo = false },
            onComprar = {
                onComprar()
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun ComicDialog(
    comic: Comic,
    onDismiss: () -> Unit,
    onComprar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = comic.titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Image(
                    painter = painterResource(id = comic.imagenRes),
                    contentDescription = comic.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = comic.descripcion)
            }
        },
        confirmButton = {
            Column {
                Button(onClick = onComprar, modifier = Modifier.fillMaxWidth()) {
                    Text("Finalizar compra")
                }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar")
                }
            }
        }
    )
}

