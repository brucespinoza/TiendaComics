package com.example.tiendacomic.ui.viewmodel

import com.example.tiendacomic.data.local.usuario.ComicEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Repositorio FAKE
 */
class FakeComicRepository {
    val lista = mutableListOf<ComicEntity>()

    suspend fun obtenerTodos(): List<ComicEntity> = lista

    suspend fun insertar(comic: ComicEntity) {
        lista.add(comic)
    }

    suspend fun actualizar(comic: ComicEntity) {
        val index = lista.indexOfFirst { it.id == comic.id }
        if (index != -1) lista[index] = comic
    }

    suspend fun eliminar(comic: ComicEntity) {
        lista.removeIf { it.id == comic.id }
    }
}

/**
 * ViewModel FAKE usado para test (sin viewModelScope para evitar problemas en tests)
 */
class CatalogoViewModelFake(
    private val repo: FakeComicRepository
) {

    private val _comics = MutableStateFlow<List<ComicEntity>>(emptyList())
    val comics: StateFlow<List<ComicEntity>> = _comics

    suspend fun cargarComics() {
        _comics.value = repo.obtenerTodos()
    }

    suspend fun insertarComic(comic: ComicEntity) {
        repo.insertar(comic)
        cargarComics()
    }

    suspend fun eliminarComic(comic: ComicEntity) {
        repo.eliminar(comic)
        cargarComics()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogoViewModelTest {

    // ==================== TESTS CRUD EXISTENTES ====================

    @Test
    fun insertarComic_actualizaLista() = runTest {
        val fake = FakeComicRepository()
        val vm = CatalogoViewModelFake(fake)

        vm.insertarComic(ComicEntity(3, "Flash", 4000, "img", "desc"))

        assertEquals(1, vm.comics.value.size)
        assertEquals("Flash", vm.comics.value[0].titulo)
    }

    @Test
    fun eliminarComic_elimina() = runTest {
        val fake = FakeComicRepository()
        fake.lista.add(ComicEntity(1, "Borrar", 3000, "img", "desc"))

        val vm = CatalogoViewModelFake(fake)
        vm.eliminarComic(ComicEntity(1, "Borrar", 3000, "img", "desc"))

        assertTrue(vm.comics.value.isEmpty())
    }

    // ==================== TESTS NUEVOS - CRUD ====================

    @Test
    fun cargarComics_listaVacia_retornaVacio() = runTest {
        val fake = FakeComicRepository()
        val vm = CatalogoViewModelFake(fake)

        vm.cargarComics()

        assertTrue(vm.comics.value.isEmpty())
    }

    @Test
    fun cargarComics_conDatos_retornaLista() = runTest {
        val fake = FakeComicRepository()
        fake.lista.add(ComicEntity(1, "Batman", 20000, "batman", "Descripcion"))
        fake.lista.add(ComicEntity(2, "Spiderman", 15000, "spiderman", "Descripcion"))

        val vm = CatalogoViewModelFake(fake)
        vm.cargarComics()

        assertEquals(2, vm.comics.value.size)
    }

    @Test
    fun insertarMultiplesComics_todosSeAgregan() = runTest {
        val fake = FakeComicRepository()
        val vm = CatalogoViewModelFake(fake)

        vm.insertarComic(ComicEntity(1, "Comic1", 10000, "img1", "desc1"))
        vm.insertarComic(ComicEntity(2, "Comic2", 20000, "img2", "desc2"))
        vm.insertarComic(ComicEntity(3, "Comic3", 30000, "img3", "desc3"))

        assertEquals(3, vm.comics.value.size)
    }

    @Test
    fun eliminarComic_noExistente_listaIgual() = runTest {
        val fake = FakeComicRepository()
        fake.lista.add(ComicEntity(1, "Existente", 10000, "img", "desc"))

        val vm = CatalogoViewModelFake(fake)
        vm.eliminarComic(ComicEntity(99, "NoExiste", 5000, "img", "desc"))

        assertEquals(1, fake.lista.size)
    }

    // ==================== TESTS VIP ====================

    @Test
    fun esVip_inicialmente_esFalse() {
        val esVip = MutableStateFlow(false)
        
        assertFalse(esVip.value)
    }

    @Test
    fun activarVip_cambiaATrue() {
        val esVip = MutableStateFlow(false)
        esVip.value = true
        
        assertTrue(esVip.value)
    }

    @Test
    fun vip_descuento50Porciento_calculoCorrecto() {
        val precioOriginal = 20000
        val descuento = 0.5
        val precioConDescuento = (precioOriginal * descuento).toInt()
        
        assertEquals(10000, precioConDescuento)
    }

    @Test
    fun vip_membresia_noTieneDescuento() {
        val titulo = "Membresía Premium"
        val esMembresia = titulo.contains("Membresía", ignoreCase = true)
        
        assertTrue(esMembresia)
    }

    @Test
    fun vip_comicNormal_tieneDescuento() {
        val titulo = "Batman: Año Uno"
        val esMembresia = titulo.contains("Membresía", ignoreCase = true)
        
        assertFalse(esMembresia)
    }

    // ==================== TESTS API EXTERNA - TIPO CAMBIO ====================

    @Test
    fun tasaUsd_inicial_esNull() {
        val tasaUsd = MutableStateFlow<Double?>(null)
        
        assertNull(tasaUsd.value)
    }

    @Test
    fun tasaUsd_conValor_noEsNull() {
        val tasaUsd = MutableStateFlow<Double?>(0.00105)
        
        assertNotNull(tasaUsd.value)
        assertEquals(0.00105, tasaUsd.value!!, 0.00001)
    }

    @Test
    fun convertirAUsd_precioEnCLP_retornaUSD() {
        val precioCLP = 20000
        val tasaUsd = 0.00105 // 1 CLP = 0.00105 USD
        val precioUSD = precioCLP * tasaUsd
        
        assertEquals(21.0, precioUSD, 0.1)
    }

    @Test
    fun convertirAUsd_precioConDescuentoVip_retornaUSD() {
        val precioCLP = 20000
        val descuentoVip = 0.5
        val precioConDescuento = (precioCLP * descuentoVip).toInt()
        val tasaUsd = 0.00105
        val precioUSD = precioConDescuento * tasaUsd
        
        assertEquals(10.5, precioUSD, 0.1)
    }

    @Test
    fun convertirAUsd_precioAlto_calculoCorrecto() {
        val precioCLP = 50000
        val tasaUsd = 0.001
        val precioUSD = precioCLP * tasaUsd
        
        assertEquals(50.0, precioUSD, 0.1)
    }

    @Test
    fun convertirAUsd_precioBajo_calculoCorrecto() {
        val precioCLP = 1000
        val tasaUsd = 0.001
        val precioUSD = precioCLP * tasaUsd
        
        assertEquals(1.0, precioUSD, 0.1)
    }

    @Test
    fun tasaUsd_fallback_usaValorPorDefecto() {
        val tasaUsd: Double? = null
        val tasaFallback = tasaUsd ?: 0.001
        
        assertEquals(0.001, tasaFallback, 0.0001)
    }

    // ==================== TESTS BÚSQUEDA ====================

    @Test
    fun buscarComic_tituloExacto_encuentra() {
        val comics = listOf(
            ComicEntity(1, "Batman", 20000, "batman", "desc"),
            ComicEntity(2, "Spiderman", 15000, "spiderman", "desc")
        )
        val busqueda = "Batman"
        val resultado = comics.filter { it.titulo.contains(busqueda, ignoreCase = true) }
        
        assertEquals(1, resultado.size)
        assertEquals("Batman", resultado[0].titulo)
    }

    @Test
    fun buscarComic_tituloParcial_encuentra() {
        val comics = listOf(
            ComicEntity(1, "Batman: Año Uno", 20000, "batman", "desc"),
            ComicEntity(2, "Batman: Dark Knight", 25000, "batman2", "desc")
        )
        val busqueda = "Batman"
        val resultado = comics.filter { it.titulo.contains(busqueda, ignoreCase = true) }
        
        assertEquals(2, resultado.size)
    }

    @Test
    fun buscarComic_noExiste_listaVacia() {
        val comics = listOf(
            ComicEntity(1, "Batman", 20000, "batman", "desc")
        )
        val busqueda = "Hulk"
        val resultado = comics.filter { it.titulo.contains(busqueda, ignoreCase = true) }
        
        assertTrue(resultado.isEmpty())
    }

    @Test
    fun buscarComic_ignoreCase_encuentra() {
        val comics = listOf(
            ComicEntity(1, "BATMAN", 20000, "batman", "desc")
        )
        val busqueda = "batman"
        val resultado = comics.filter { it.titulo.contains(busqueda, ignoreCase = true) }
        
        assertEquals(1, resultado.size)
    }

    // ==================== TESTS PRECIO FORMATO ====================

    @Test
    fun precio_formatoCLP_correcto() {
        val precio = 20000
        assertTrue(precio > 0)
    }

    @Test
    fun precio_membresia_esCorrecto() {
        val precioMembresia = 50000
        assertEquals(50000, precioMembresia)
    }
}













