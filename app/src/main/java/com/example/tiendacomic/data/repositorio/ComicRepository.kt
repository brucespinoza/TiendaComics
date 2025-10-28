package com.example.tiendacomic.data.repositorio

import com.example.tiendacomic.data.local.usuario.ComicDao
import com.example.tiendacomic.data.local.usuario.ComicEntity

class ComicRepository(private val dao: ComicDao) {
//insertar comic
    suspend fun obtenerTodos() = dao.obtenerTodos()
    suspend fun insertar(c: ComicEntity) = dao.insertar(c)
    suspend fun actualizar(c: ComicEntity) = dao.actualizar(c)
    suspend fun eliminar(c: ComicEntity) = dao.eliminar(c)
}
