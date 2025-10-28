package com.example.tiendacomic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendacomic.data.local.usuario.ComicEntity
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

    fun activarVip() {
        _esVip.value = true
    }

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


