package com.example.tiendacomic.navigation

sealed class Route(val path: String){


    data object Login : Route("login")

    data object Register : Route("register")

    data object Catalogo : Route("catalogo")

    data object Carrito : Route("carrito")

    data object Perfil : Route("perfil")

    data object Admin : Route("admin")

    data object Membresia : Route("membresia")


}