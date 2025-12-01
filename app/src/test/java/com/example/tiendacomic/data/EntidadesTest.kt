package com.example.tiendacomic.data

import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.data.local.usuario.UsuarioEntity
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests para las entidades ComicEntity y UsuarioEntity
 * Siguiendo el mismo patrón que ValidadorTest
 */
class EntidadesTest {

    // ==================== TESTS DE COMIC ENTITY ====================

    @Test
    fun comicEntity_creacion_propiedadesCorrectas() {
        val comic = ComicEntity(
            id = 1,
            titulo = "Batman: Año Uno",
            precio = 20000,
            imagen = "batman",
            descripcion = "Los primeros días de Bruce Wayne"
        )
        
        assertEquals(1, comic.id)
        assertEquals("Batman: Año Uno", comic.titulo)
        assertEquals(20000, comic.precio)
        assertEquals("batman", comic.imagen)
        assertEquals("Los primeros días de Bruce Wayne", comic.descripcion)
    }

    @Test
    fun comicEntity_idPorDefecto_esCero() {
        val comic = ComicEntity(
            titulo = "Spiderman",
            precio = 15000,
            imagen = "spiderman",
            descripcion = "El hombre araña"
        )
        
        assertEquals(0, comic.id)
    }

    @Test
    fun comicEntity_precioPositivo_esValido() {
        val comic = ComicEntity(
            id = 1,
            titulo = "Test",
            precio = 25000,
            imagen = "test",
            descripcion = "Test"
        )
        
        assertTrue(comic.precio > 0)
    }

    @Test
    fun comicEntity_tituloNoVacio_esValido() {
        val comic = ComicEntity(
            id = 1,
            titulo = "Avengers",
            precio = 20000,
            imagen = "avengers",
            descripcion = "Los vengadores"
        )
        
        assertTrue(comic.titulo.isNotEmpty())
    }

    @Test
    fun comicEntity_imagenNoVacia_esValida() {
        val comic = ComicEntity(
            id = 1,
            titulo = "Test",
            precio = 10000,
            imagen = "test_image",
            descripcion = "Test"
        )
        
        assertTrue(comic.imagen.isNotEmpty())
    }

    @Test
    fun comicEntity_descripcionPuedeSerVacia() {
        val comic = ComicEntity(
            id = 1,
            titulo = "Test",
            precio = 10000,
            imagen = "test",
            descripcion = ""
        )
        
        assertTrue(comic.descripcion.isEmpty())
    }

    @Test
    fun comicEntity_dosComicsIguales_sonIguales() {
        val comic1 = ComicEntity(1, "Batman", 20000, "batman", "desc")
        val comic2 = ComicEntity(1, "Batman", 20000, "batman", "desc")
        
        assertEquals(comic1, comic2)
    }

    @Test
    fun comicEntity_dosComicsDiferentes_noSonIguales() {
        val comic1 = ComicEntity(1, "Batman", 20000, "batman", "desc")
        val comic2 = ComicEntity(2, "Spiderman", 15000, "spiderman", "desc")
        
        assertNotEquals(comic1, comic2)
    }

    @Test
    fun comicEntity_copy_creaCopiaCambiada() {
        val original = ComicEntity(1, "Batman", 20000, "batman", "desc")
        val copia = original.copy(precio = 25000)
        
        assertEquals(25000, copia.precio)
        assertEquals("Batman", copia.titulo) // No cambió
    }

    @Test
    fun comicEntity_precioMembresia_esCorrecto() {
        val membresia = ComicEntity(
            id = 1,
            titulo = "Membresía Premium",
            precio = 50000,
            imagen = "vip",
            descripcion = "Acceso ilimitado"
        )
        
        assertEquals(50000, membresia.precio)
        assertTrue(membresia.titulo.contains("Membresía"))
    }

    // ==================== TESTS DE USUARIO ENTITY ====================

    @Test
    fun usuarioEntity_creacion_propiedadesCorrectas() {
        val usuario = UsuarioEntity(
            id = 1L,
            nombre = "Juan Perez",
            rut = "12.345.678-9",
            correo = "juan@gmail.com",
            contrasena = "Pass123@",
            rol = "usuario"
        )
        
        assertEquals(1L, usuario.id)
        assertEquals("Juan Perez", usuario.nombre)
        assertEquals("12.345.678-9", usuario.rut)
        assertEquals("juan@gmail.com", usuario.correo)
        assertEquals("Pass123@", usuario.contrasena)
        assertEquals("usuario", usuario.rol)
    }

    @Test
    fun usuarioEntity_rolPorDefecto_esUsuario() {
        val usuario = UsuarioEntity(
            id = 1L,
            nombre = "Test",
            rut = "11.111.111-1",
            correo = "test@gmail.com",
            contrasena = "Test123@"
        )
        
        assertEquals("usuario", usuario.rol)
    }

    @Test
    fun usuarioEntity_idPorDefecto_esCero() {
        val usuario = UsuarioEntity(
            nombre = "Test",
            rut = "11.111.111-1",
            correo = "test@gmail.com",
            contrasena = "Test123@"
        )
        
        assertEquals(0L, usuario.id)
    }

    @Test
    fun usuarioEntity_rolAdmin_esAdmin() {
        val admin = UsuarioEntity(
            id = 1L,
            nombre = "Admin",
            rut = "99.999.999-9",
            correo = "admin@gmail.com",
            contrasena = "Admin123@",
            rol = "admin"
        )
        
        assertEquals("admin", admin.rol)
    }

    @Test
    fun usuarioEntity_rolPremium_esPremium() {
        val premium = UsuarioEntity(
            id = 1L,
            nombre = "Premium User",
            rut = "88.888.888-8",
            correo = "premium@gmail.com",
            contrasena = "Prem123@",
            rol = "premium"
        )
        
        assertEquals("premium", premium.rol)
    }

    @Test
    fun usuarioEntity_correoValido_tieneArroba() {
        val usuario = UsuarioEntity(
            id = 1L,
            nombre = "Test",
            rut = "11.111.111-1",
            correo = "test@gmail.com",
            contrasena = "Test123@"
        )
        
        assertTrue(usuario.correo.contains("@"))
    }

    @Test
    fun usuarioEntity_rutValido_tieneGuion() {
        val usuario = UsuarioEntity(
            id = 1L,
            nombre = "Test",
            rut = "12.345.678-K",
            correo = "test@gmail.com",
            contrasena = "Test123@"
        )
        
        assertTrue(usuario.rut.contains("-"))
    }

    @Test
    fun usuarioEntity_dosUsuariosIguales_sonIguales() {
        val usuario1 = UsuarioEntity(1L, "Juan", "12.345.678-9", "juan@gmail.com", "Pass123@", "usuario")
        val usuario2 = UsuarioEntity(1L, "Juan", "12.345.678-9", "juan@gmail.com", "Pass123@", "usuario")
        
        assertEquals(usuario1, usuario2)
    }

    @Test
    fun usuarioEntity_dosUsuariosDiferentes_noSonIguales() {
        val usuario1 = UsuarioEntity(1L, "Juan", "12.345.678-9", "juan@gmail.com", "Pass123@", "usuario")
        val usuario2 = UsuarioEntity(2L, "Pedro", "98.765.432-1", "pedro@gmail.com", "Pass456@", "usuario")
        
        assertNotEquals(usuario1, usuario2)
    }

    @Test
    fun usuarioEntity_copy_creaCopiaCambiada() {
        val original = UsuarioEntity(1L, "Juan", "12.345.678-9", "juan@gmail.com", "Pass123@", "usuario")
        val copia = original.copy(rol = "admin")
        
        assertEquals("admin", copia.rol)
        assertEquals("Juan", copia.nombre) // No cambió
    }

    @Test
    fun usuarioEntity_cambiarContrasena_creaCopiaNueva() {
        val original = UsuarioEntity(1L, "Juan", "12.345.678-9", "juan@gmail.com", "Pass123@", "usuario")
        val actualizado = original.copy(contrasena = "NewPass123@")
        
        assertEquals("NewPass123@", actualizado.contrasena)
        assertNotEquals(original.contrasena, actualizado.contrasena)
    }

    // ==================== TESTS DE LISTAS ====================

    @Test
    fun listaComics_filtrarPorPrecio_funciona() {
        val comics = listOf(
            ComicEntity(1, "Barato", 10000, "img", "desc"),
            ComicEntity(2, "Normal", 20000, "img", "desc"),
            ComicEntity(3, "Caro", 30000, "img", "desc")
        )
        
        val baratos = comics.filter { it.precio < 15000 }
        
        assertEquals(1, baratos.size)
        assertEquals("Barato", baratos[0].titulo)
    }

    @Test
    fun listaComics_ordenarPorPrecio_funciona() {
        val comics = listOf(
            ComicEntity(1, "Caro", 30000, "img", "desc"),
            ComicEntity(2, "Barato", 10000, "img", "desc"),
            ComicEntity(3, "Normal", 20000, "img", "desc")
        )
        
        val ordenados = comics.sortedBy { it.precio }
        
        assertEquals("Barato", ordenados[0].titulo)
        assertEquals("Normal", ordenados[1].titulo)
        assertEquals("Caro", ordenados[2].titulo)
    }

    @Test
    fun listaUsuarios_filtrarPorRol_funciona() {
        val usuarios = listOf(
            UsuarioEntity(1L, "Admin1", "11.111.111-1", "admin1@gmail.com", "Pass@123", "admin"),
            UsuarioEntity(2L, "User1", "22.222.222-2", "user1@gmail.com", "Pass@123", "usuario"),
            UsuarioEntity(3L, "Admin2", "33.333.333-3", "admin2@gmail.com", "Pass@123", "admin")
        )
        
        val admins = usuarios.filter { it.rol == "admin" }
        
        assertEquals(2, admins.size)
    }

    @Test
    fun listaUsuarios_buscarPorCorreo_encuentra() {
        val usuarios = listOf(
            UsuarioEntity(1L, "Juan", "11.111.111-1", "juan@gmail.com", "Pass@123", "usuario"),
            UsuarioEntity(2L, "Pedro", "22.222.222-2", "pedro@gmail.com", "Pass@123", "usuario")
        )
        
        val encontrado = usuarios.find { it.correo == "juan@gmail.com" }
        
        assertNotNull(encontrado)
        assertEquals("Juan", encontrado?.nombre)
    }

    @Test
    fun listaUsuarios_buscarPorCorreoInexistente_retornaNull() {
        val usuarios = listOf(
            UsuarioEntity(1L, "Juan", "11.111.111-1", "juan@gmail.com", "Pass@123", "usuario")
        )
        
        val encontrado = usuarios.find { it.correo == "noexiste@gmail.com" }
        
        assertNull(encontrado)
    }
}

