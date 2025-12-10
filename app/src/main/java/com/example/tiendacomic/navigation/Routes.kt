package com.example.tiendacomic.navigation

// Clase sellada que define todas las rutas de la app
sealed class Route(val path: String) {

    // Pantalla de inicio de sesión
    data object Login : Route("login")

    // Registro de nuevos usuarios
    data object Registro : Route("register")

    // Catálogo de cómics
    data object Catalogo : Route("catalogo")

    // Carrito de compras
    data object Carrito : Route("carrito")

    // Perfil del usuario
    data object Perfil : Route("perfil")

    // Panel de administrador
    data object Admin : Route("admin")

    // Nueva pantalla de Membresía
    data object Membresia : Route("membresia")

    data object RecuperarContraseña : Route("RecuperarContraseña")

    // Cambiar contraseña desde perfil
    data object CambiarContraseña : Route("cambiar-contraseña")
}
