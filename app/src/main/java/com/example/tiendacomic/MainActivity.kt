package com.example.tiendacomic


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults.containerColor
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
import com.example.tiendacomic.ui.screen.CatalogoScreen
import com.example.tiendacomic.ui.screen.LoginScreen
import com.example.tiendacomic.ui.screen.RegistroScreen
import com.example.tiendacomic.ui.theme.TiendaComicTheme

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
                                LoginScreen(
                                    onLoginExitoso = { mostrarCatalogo = true },
                                    onIrRegistro = { mostrarLogin = false }
                                )
                            }
                            else -> {
                                RegistroScreen(
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