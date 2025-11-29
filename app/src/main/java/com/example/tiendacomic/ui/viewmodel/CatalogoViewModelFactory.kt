package com.example.tiendacomic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendacomic.data.repositorio.ComicRepository

/**
 * Factory para crear CatalogoViewModel
 * Ahora usa la API REST, no necesita Context de Room
 */
class CatalogoViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogoViewModel::class.java)) {
            // Creamos el repositorio que usa la API REST
            val repo = ComicRepository()
            return CatalogoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
