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
import androidx.navigation.NavController
import com.example.tiendacomic.R
import com.example.tiendacomic.navigation.Route
import com.example.tiendacomic.ui.components.AppTopBar
import java.text.NumberFormat
import java.util.*

/*───────────────────────────────────────────────
 MODELO DE DATOS - Representa un cómic
───────────────────────────────────────────────*/
data class Comic(
    val id: Int,
    val titulo: String,
    val precio: Int,
    val imagenRes: Int,
    val descripcion: String
)

/*───────────────────────────────────────────────
 PANTALLA PRINCIPAL DEL CATÁLOGO
───────────────────────────────────────────────*/
@Composable
fun CatalogoScreen(
    navController: NavController,
    onVerMas: (Comic) -> Unit = {}
) {
    // Lista de cómics (mock de ejemplo)
    val listaComics = listOf(
        Comic(1, "Spider-Man #1", 15990, R.drawable.spiderman, "Peter Parker enfrenta su mayor desafío al equilibrar su vida como estudiante y superhéroe."),
        Comic(2, "Batman: Año Uno", 12490, R.drawable.batman, "La historia de los primeros días de Bruce Wayne como el vigilante de Gotham."),
        Comic(3, "Avengers: Endgame", 18990, R.drawable.avenger, "Los Vengadores intentan revertir las consecuencias del chasquido de Thanos."),
        Comic(4, "X-Men: Dark Phoenix", 14990, R.drawable.men, "Jean Grey pierde el control de sus poderes y pone en peligro a toda la humanidad."),
        Comic(5, "Iron Man: Extremis", 13490, R.drawable.ironman, "Tony Stark debe enfrentarse a un virus nanotecnológico que lo cambiará para siempre."),
        Comic(6, "4 Fantásticos #1", 24990, R.drawable.fantasticos, "Los héroes más grandes de Marvel descubren sus nuevos poderes."),
        Comic(7, "Narnia", 16990, R.drawable.narnia, "Un mundo mágico lleno de criaturas fantásticas y una eterna batalla entre el bien y el mal."),
        Comic(8, "Alicia en el país de las maravillas #1", 18990, R.drawable.alicia, "Alicia cae en un mundo surrealista lleno de personajes excéntricos."),
        Comic(9, "Power Ranger #1", 17990, R.drawable.ranger, "Un grupo de jóvenes héroes defiende la Tierra con sus trajes y zords."),
        Comic(10, "Shrek #1", 19990, R.drawable.shrek, "El ogro más querido del cine se embarca en una aventura inesperada.")
    )

    // Estado del texto de búsqueda
    var textoBusqueda by remember { mutableStateOf("") }

    // Filtra los cómics según el texto ingresado
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
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            // Título
            Text(
                text = "Catálogo de Cómics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            // Barra de búsqueda
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

            // Lista de cómics (grid)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comicsFiltrados) { comic ->
                    ComicCard(comic = comic)
                }
            }

            // Mensaje si no se encuentra ningún cómic
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

/*───────────────────────────────────────────────
 TARJETA INDIVIDUAL DE CADA CÓMIC
───────────────────────────────────────────────*/
@Composable
fun ComicCard(comic: Comic) {
    // Estados para la animación y el diálogo
    var seleccionado by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // Animación de escala (zoom in)
    val escala by animateFloatAsState(
        targetValue = if (seleccionado) 1.05f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    // Formatear precio
    val formatoCLP = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0
    }
    val precioFormateado = formatoCLP.format(comic.precio)

    // Tarjeta visual
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
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
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = comic.titulo, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            Text(
                text = precioFormateado,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    seleccionado = true
                    mostrarDialogo = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver más")
            }
        }
    }

    // Mostrar el diálogo si se presiona
    if (mostrarDialogo) {
        ComicDialog(comic = comic, onDismiss = { mostrarDialogo = false })
    }
}

/*───────────────────────────────────────────────
 DIÁLOGO DEL CÓMIC (sin voz)
───────────────────────────────────────────────*/
@Composable
fun ComicDialog(comic: Comic, onDismiss: () -> Unit) {
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
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
