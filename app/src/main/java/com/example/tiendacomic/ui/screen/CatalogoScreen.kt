package com.example.tiendacomic.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

data class Comic(
    val id: Int,
    val titulo: String,
    val precio: Int,
    val imagenRes: Int
)

@Composable
fun CatalogoScreen(
    onVerMas: (Comic) -> Unit = {}
) {
    val listaComics = listOf(
        Comic(1, "Spider-Man #1", 19990, R.drawable.spiderman),
        Comic(2, "Batman: Año Uno", 14990, R.drawable.batman),
        Comic(3, "Avengers: Endgame", 24990, R.drawable.avenger),
        Comic(4, "X-Men: Dark Phoenix", 14990, R.drawable.men),
        Comic(5, "Iron Man: Extremis", 19990, R.drawable.ironman),
    )

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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaComics) { comic ->
                ComicCard(comic = comic, onVerMas = { onVerMas(comic) })
            }
        }
    }
}

@Composable
fun ComicCard(
    comic: Comic,
    onVerMas: () -> Unit
) {
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
