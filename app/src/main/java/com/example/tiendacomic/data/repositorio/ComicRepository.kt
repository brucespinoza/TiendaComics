package com.example.tiendacomic.data.repositorio

import android.content.Context
import com.example.tiendacomic.data.local.database.AppDatabase
import com.example.tiendacomic.data.local.usuario.ComicEntity

class ComicRepository(context: Context) {

    //Obtenemos el DAO real desde la base de datos de Room
    private val dao = AppDatabase.getInstance(context).comicDao()

    // 🔹 Operaciones CRUD
    suspend fun obtenerTodos() = dao.obtenerTodos()
    suspend fun insertar(comic: ComicEntity) = dao.insertar(comic)
    suspend fun actualizar(comic: ComicEntity) = dao.actualizar(comic)
    suspend fun eliminar(comic: ComicEntity) = dao.eliminar(comic)
}

