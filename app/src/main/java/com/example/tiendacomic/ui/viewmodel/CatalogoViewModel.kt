package com.example.tiendacomic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.data.remote.ApiConfig
import com.example.tiendacomic.data.repositorio.ComicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repo: ComicRepository
) : ViewModel() {

    // Lista de comic
    private val _comics = MutableStateFlow<List<ComicEntity>>(emptyList())
    val comics: StateFlow<List<ComicEntity>> = _comics

    //vip
    private val _esVip = MutableStateFlow(false)
    val esVip: StateFlow<Boolean> = _esVip

    // ====== API EXTERNA: Tipo de cambio USD ======
    private val _tasaUsd = MutableStateFlow<Double?>(null)
    val tasaUsd: StateFlow<Double?> = _tasaUsd
    
    private val _cargandoTasa = MutableStateFlow(false)
    val cargandoTasa: StateFlow<Boolean> = _cargandoTasa
    
    private val _errorTasa = MutableStateFlow<String?>(null)
    val errorTasa: StateFlow<String?> = _errorTasa
    // =============================================

    init {
        // Cargar tipo de cambio al iniciar
        cargarTipoCambio()
    }

    fun activarVip() {
        _esVip.value = true
    }

    // ====== API EXTERNA: Obtener tipo de cambio ======
    fun cargarTipoCambio() {
        viewModelScope.launch {
            _cargandoTasa.value = true
            _errorTasa.value = null
            try {
                val response = ApiConfig.exchangeRateApi.obtenerTasasCambio()
                if (response.isSuccessful && response.body() != null) {
                    _tasaUsd.value = response.body()!!.getTasaUsd()
                } else {
                    _errorTasa.value = "No se pudo obtener el tipo de cambio"
                }
            } catch (e: Exception) {
                _errorTasa.value = "Error de conexión: ${e.message}"
                // Si falla, usar tasa aproximada como fallback
                _tasaUsd.value = 0.001 // Aproximadamente 1 USD = 1000 CLP
            } finally {
                _cargandoTasa.value = false
            }
        }
    }
    
    /**
     * Convierte un precio de CLP a USD usando la tasa actual
     */
    fun convertirAUsd(precioCLP: Int): Double {
        val tasa = _tasaUsd.value ?: 0.001
        return precioCLP * tasa
    }
    // =================================================

    //crud

    fun cargarComics() {
        viewModelScope.launch {
            _comics.value = repo.obtenerTodos()
        }
    }

    fun insertarComic(comic: ComicEntity) {
        viewModelScope.launch {
            repo.insertar(comic)
            cargarComics()
        }
    }

    fun actualizarComic(comic: ComicEntity) {
        viewModelScope.launch {
            repo.actualizar(comic)
            cargarComics()
        }
    }

    fun eliminarComic(comic: ComicEntity) {
        viewModelScope.launch {
            repo.eliminar(comic)
            cargarComics()
        }
    }
}


