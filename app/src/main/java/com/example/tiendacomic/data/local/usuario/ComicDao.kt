package com.example.tiendacomic.data.local.usuario

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ComicDao {

    @Query("SELECT * FROM comic")
    suspend fun obtenerTodos(): List<ComicEntity>

    @Insert
    suspend fun insertar(comic: ComicEntity)

    @Update
    suspend fun actualizar(comic: ComicEntity)

    @Delete
    suspend fun eliminar(comic: ComicEntity)
}
