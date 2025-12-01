package com.example.tiendacomic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendacomic.data.repositorio.UsuarioRepository

/**
 * Factory para crear ModeloAutenticacion
 * Ahora usa la API REST, no necesita DAO de Room
 */
class ModeloAutenticacionFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModeloAutenticacion::class.java)) {
            // Creamos el repositorio que usa la API REST
            val repository = UsuarioRepository()
            return ModeloAutenticacion(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
