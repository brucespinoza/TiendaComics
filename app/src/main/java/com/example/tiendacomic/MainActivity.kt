package com.example.tiendacomic


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tiendacomic.navigation.NavGraph
import com.example.tiendacomic.ui.screen.CatalogoScreen
import com.example.tiendacomic.ui.screen.LoginScreenVm
//import com.example.tiendacomic.ui.screen.LoginScreenVm
import com.example.tiendacomic.ui.screen.RegistroScreenVm
import com.example.tiendacomic.ui.theme.TiendaComicTheme

//El controllador esta em NavGraph.kt si quieres añadir un TopBar puedes hacerlo
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TiendaComicTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}


/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TiendaComicTheme {

                // Estados para manejar qué pantalla mostrar
                var mostrarCatalogo by remember { mutableStateOf(false) }
                var mostrarLogin by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        when {
                            mostrarCatalogo -> {
                                CatalogoScreen() // pantalla de catálogo
                            }
                            mostrarLogin -> {
                                LoginScreenVm(
                                    onLoginExitoso = { mostrarCatalogo = true },
                                    onIrRegistro = { mostrarLogin = false }
                                )
                            }
                            else -> {
                                RegistroScreenVm(
                                    onRegistroExitoso = { mostrarCatalogo = true },
                                    onIrLogin = { mostrarLogin = true }
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}
*/




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TiendaComicTheme {
        Greeting("Android")
    }
}