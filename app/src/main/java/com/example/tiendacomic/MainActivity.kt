package com.example.tiendacomic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tiendacomic.data.local.database.AppDatabase
import com.example.tiendacomic.data.repositorio.UsuarioRepository
import com.example.tiendacomic.data.repositorio.ComicRepository
import com.example.tiendacomic.navigation.NavGraph
import com.example.tiendacomic.ui.theme.TiendaComicTheme
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacionFactory
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current.applicationContext

    // --- BD E INSTANCIAS DE ROOM ---
    val db = AppDatabase.getInstance(context)

    // ====== AUTENTICACION ======
    val userDao = db.usuarioDao()
    val userRepo = UsuarioRepository(userDao)
    val authViewModel: ModeloAutenticacion = viewModel(
        factory = ModeloAutenticacionFactory(userRepo)
    )

    // ====== CATÁLOGO ======
    val catalogoVm: CatalogoViewModel = viewModel(
        factory = CatalogoViewModelFactory(context)
    )

    val navController = rememberNavController()

    TiendaComicTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavGraph(
                navController = navController,
                vm = authViewModel,
                catalogoVm = catalogoVm // Ahora pasa sin errores
            )
        }
    }
}


