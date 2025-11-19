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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tiendacomic.R
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.navigation.Route
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import com.example.tiendacomic.data.storage.UserPreferences
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    vm: CatalogoViewModel,
    authVm: ModeloAutenticacion
) {
    val context = LocalContext.current
    val comicsBD by vm.comics.collectAsStateWithLifecycle()
    val esVip by vm.esVip.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var textoBusqueda by remember { mutableStateOf("") }

    // Lista base
    val listaBase = listOf(
        ComicEntity(1, "Membresía Premium", 50000, "vip", "Acceso ilimitado a todos los cómics digitales, descuentos exclusivos y más."),
        ComicEntity(2, "Batman: Año Uno", 20000, "batman", "Los primeros días de Bruce Wayne como vigilante."),
        ComicEntity(3, "Avengers: Endgame", 20000, "avenger", "Los Vengadores intentan revertir el chasquido de Thanos."),
        ComicEntity(4, "X-Men: Dark Phoenix", 15000, "men", "Jean Grey pierde el control de sus poderes."),
        ComicEntity(5, "Iron Man: Extremis", 15000, "ironman", "Tony Stark enfrenta un virus nanotecnológico."),
        ComicEntity(6, "4 Fantásticos #1", 25000, "fantasticos", "Los héroes más grandes de Marvel."),
        ComicEntity(7, "Narnia", 17000, "narnia", "Un mundo mágico lleno de criaturas fantásticas."),
        ComicEntity(8, "Alicia en el país de las maravillas #1", 19000, "aliciaa", "Alicia cae en un mundo surrealista."),
        ComicEntity(9, "Power Ranger #1", 18000, "ranger", "Un grupo de jóvenes héroes defiende la Tierra."),
        ComicEntity(10, "Spider-Man #1", 16000, "spiderman", "Peter Parker enfrenta su mayor desafío.")
    )

    val comicsCombinados = remember(comicsBD) {
        val existentes = listaBase.map { it.titulo }
        val nuevos = comicsBD.filter { it.titulo !in existentes }
        listaBase + nuevos
    }

    val comicsFiltrados = comicsCombinados
        .filter { it.titulo.contains(textoBusqueda, ignoreCase = true) }
        .filter { comic ->
            if (esVip && (comic.titulo.contains("Membresía", true) || comic.titulo.contains("Premium", true))) {
                false
            } else true
        }

    val formatoCLP = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    // ====== NUEVO: instancia de UserPreferences para marcar loggedIn = false al cerrar sesión ======
    val userPrefs = remember { UserPreferences(context) }
    // =============================================================================================

    Scaffold(
        topBar = {
            CatalogTopBar(
                onCatalogo = { navController.navigate(Route.Catalogo.path) },
                onPerfil = { navController.navigate(Route.Perfil.path) },
                onLogout = {
                    // Lógica de cierre de sesión: ejecutar la llamada suspend dentro de una corrutina
                    scope.launch {
                        authVm.cerrarSesion()
                        userPrefs.setLoggedIn(false) // <-- llamada suspend, ahora segura dentro de scope.launch

                        // Navegar a Login y limpiar backstack
                        navController.navigate(Route.Login.path) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3F2FD)) // Fondo celeste suave
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Catálogo de Cómics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0),
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = if (esVip) "🌟 Usuario VIP (50% aplicado)" else "👤 Usuario normal",
                color = if (esVip) Color(0xFF1565C0) else Color.Gray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
            )

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                label = { Text("Buscar cómic...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF64B5F6),
                    focusedLabelColor = Color(0xFF1976D2)
                )
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
                        esVip = esVip,
                        formatoCLP = formatoCLP,
                        onComprar = {
                            if (comic.titulo.contains("Membresía", true) || comic.titulo.contains("Premium", true)) {
                                vm.activarVip()
                                scope.launch {
                                    snackbarHostState.showSnackbar("🎉 ¡Ahora eres usuario VIP!")
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Compra realizada con éxito")
                                }
                            }
                            authVm.agregarCompra(comic.titulo)
                        }
                    )
                }
            }
        }
    }
}

// ---------- TOP BAR SUAVE ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogTopBar(onCatalogo: () -> Unit, onPerfil: () -> Unit, onLogout: () -> Unit = {}) {
    TopAppBar(
        title = {
            Text(
                "Tienda de Comic",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF64B5F6) // Azul suave que no cansa
        ),
        actions = {
            TextButton(onClick = onCatalogo) { Text("Catálogo", color = Color.White) }
            TextButton(onClick = onPerfil) { Text("Perfil", color = Color.White) }

            // ======== Botón Cerrar sesión EN DOS LÍNEAS ========
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onLogout() }
            ) {
                Text(text = "Cerrar", color = Color.White, fontSize = 12.sp)
                Text(text = "Sesión", color = Color.White, fontSize = 12.sp)
            }
            // ===================================================
        }
    )
}

// ---------- TARJETA DE CÓMIC ----------
@Composable
fun ComicCard(
    comic: ComicEntity,
    esVip: Boolean,
    formatoCLP: NumberFormat,
    onComprar: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    val escala by animateFloatAsState(
        targetValue = if (mostrarDialogo) 1.05f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    val context = LocalContext.current
    val imageRes = remember(comic.imagen) {
        context.resources.getIdentifier(comic.imagen, "drawable", context.packageName)
    }

    val precioOriginal = comic.precio
    val precioDescuento =
        if (esVip && !comic.titulo.contains("VIP", true) && !comic.titulo.contains("Membresía", true))
            (precioOriginal * 0.5).toInt()
        else precioOriginal

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = escala, scaleY = escala),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable { mostrarDialogo = true },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = comic.titulo,
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = comic.titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )

            if (esVip && !comic.titulo.contains("VIP", true) && !comic.titulo.contains("Membresía", true)) {
                Text(
                    text = formatoCLP.format(precioOriginal),
                    style = TextStyle(
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = formatoCLP.format(precioDescuento) + " (-50%)",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = formatoCLP.format(precioOriginal),
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { mostrarDialogo = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF64B5F6), Color(0xFF1976D2))
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Text("Ver más", color = Color.White)
                }
            }
        }
    }

    if (mostrarDialogo) {
        ComicDialog(
            comic = comic,
            formatoCLP = formatoCLP,
            esVip = esVip,
            onDismiss = { mostrarDialogo = false },
            onComprar = {
                onComprar()
                mostrarDialogo = false
            }
        )
    }
}

// ---------- DIÁLOGO DETALLES ----------
@Composable
fun ComicDialog(
    comic: ComicEntity,
    formatoCLP: NumberFormat,
    esVip: Boolean,
    onDismiss: () -> Unit,
    onComprar: () -> Unit
) {
    val context = LocalContext.current
    val imageRes = remember(comic.imagen) {
        context.resources.getIdentifier(comic.imagen, "drawable", context.packageName)
    }

    val precioOriginal = comic.precio
    val precioDescuento =
        if (esVip && !comic.titulo.contains("VIP", true) && !comic.titulo.contains("Membresía", true))
            (precioOriginal * 0.5).toInt()
        else precioOriginal

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(comic.titulo, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0)) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (imageRes != 0) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = comic.titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(comic.descripcion)
                Spacer(Modifier.height(8.dp))

                if (esVip && !comic.titulo.contains("VIP", true) && !comic.titulo.contains("Membresía", true)) {
                    Text(
                        text = formatoCLP.format(precioOriginal),
                        style = TextStyle(
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = formatoCLP.format(precioDescuento) + " (-50%)",
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text("Precio: ${formatoCLP.format(precioOriginal)}", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Column {
                Button(
                    onClick = onComprar,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF64B5F6), Color(0xFF1976D2))
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    ) {
                        Text("Finalizar compra", color = Color.White)
                    }
                }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar", color = Color(0xFF1976D2))
                }
            }
        }
    )
}
