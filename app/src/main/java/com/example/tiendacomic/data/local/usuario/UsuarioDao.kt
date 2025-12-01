package com.example.tiendacomic.data.local.usuario

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsuarioDao {

    // INSERTAR usuario nuevo
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: UsuarioEntity): Long

    // OBTENER usuario por correo (para login)
    @Query("SELECT * FROM users WHERE correo = :correo")
    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity?

    // OBTENER todos los usuarios (para administración)
    @Query("SELECT * FROM users")
    suspend fun obtenerTodos(): List<UsuarioEntity>

    // CONTAR cuántos usuarios existen
    @Query("SELECT COUNT(*) FROM users")
    suspend fun contar(): Int

    // 🔹 NUEVO: ACTUALIZAR un usuario existente
    @Update
    suspend fun actualizar(usuario: UsuarioEntity)
}
