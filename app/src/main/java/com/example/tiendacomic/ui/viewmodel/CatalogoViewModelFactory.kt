package com.example.tiendacomic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import com.example.tiendacomic.data.repositorio.ComicRepository

class CatalogoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogoViewModel::class.java)) {
            val repo = ComicRepository(context) // Se crea el repositorio con contexto real
            return CatalogoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

