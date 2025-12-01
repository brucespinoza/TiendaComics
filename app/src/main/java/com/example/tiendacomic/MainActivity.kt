package com.example.tiendacomic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tiendacomic.navigation.NavGraph
import com.example.tiendacomic.ui.theme.TiendaComicTheme
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacion
import com.example.tiendacomic.ui.viewmodel.ModeloAutenticacionFactory
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModel
import com.example.tiendacomic.ui.viewmodel.CatalogoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // ✨ Crear e instalar el Splash antes del super.onCreate
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    // ====== AUTENTICACION (ahora usa API REST) ======
    val authViewModel: ModeloAutenticacion = viewModel(
        factory = ModeloAutenticacionFactory()
    )

    // ====== CATÁLOGO (ahora usa API REST) ======
    val catalogoVm: CatalogoViewModel = viewModel(
        factory = CatalogoViewModelFactory()
    )

    val navController = rememberNavController()

    TiendaComicTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            NavGraph(
                navController = navController,
                vm = authViewModel,
                catalogoVm = catalogoVm
            )
        }
    }
}
