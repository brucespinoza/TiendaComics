package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendacomic.R
import java.text.NumberFormat
import java.util.*

//Modelo de datos
data class Comic(
    val id: Int,
    val titulo: String,
    val precio: Int, //en pesos chilenos
    val imagenRes: Int
)

// Pantalla principal del catálogo
@Composable
fun CatalogoScreen(
    onVerMas: (Comic) -> Unit = {}
) {
    // Lista de ejemplo (mock)
    val listaComics = listOf(
        Comic(1, "Spider-Man #1", 15990, R.drawable.spiderman),
        Comic(2, "Batman: Año Uno", 12490, R.drawable.batman),
        Comic(3, "Avengers: Endgame", 18990, R.drawable.avenger),
        Comic(4, "X-Men: Dark Phoenix", 14990, R.drawable.men),
        Comic(5, "Iron Man: Extremis", 13490, R.drawable.ironman),
        Comic(6, "4 Fantasticos #1", 14990, R.drawable.fantasticos),
        Comic(7, "Narnia", 16990, R.drawable.narnia),
        Comic(8, "Alicia en el pais de las maravillas #1", 18990, R.drawable.alicia),
        Comic(9, "Power ranger #1", 17990, R.drawable.ranger),
        Comic(10, "Shrek #1", 19990, R.drawable.shrek),

        )

    // Estado del texto de búsqueda
    var textoBusqueda by remember { mutableStateOf("") }

    // Filtramos los cómics según el texto ingresado
    val comicsFiltrados = listaComics.filter {
        it.titulo.contains(textoBusqueda, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Text(
            text = "Catálogo de Cómics",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        // 🔹 Barra de búsqueda
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

        // Lista de cómics (filtrada)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(comicsFiltrados) { comic ->
                ComicCard(comic = comic, onVerMas = { onVerMas(comic) })
            }
        }

        // Si no se encuentra ningún cómic
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

// Componente para mostrar cada cómic
@Composable
fun ComicCard(
    comic: Comic,
    onVerMas: () -> Unit
) {
    // Formatear precio en pesos chilenos
    val formatoCLP = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0
    }
    val precioFormateado = formatoCLP.format(comic.precio)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
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

            Text(
                text = comic.titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = precioFormateado,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onVerMas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver más")
            }
        }
    }
}
