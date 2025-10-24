package com.example.tiendacomic.data.local.usuario

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    //insertar datos en la tabla
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: UsuarioEntity): Long   // INSERTAR
    //buscar todos los datos de un usuario a traves de su email
    @Query("SELECT * FROM users WHERE correo = :correo")
    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity?   // LOGIN
    //buscar todos los usuarios de la tabla users
    @Query("SELECT * FROM users")
    suspend fun obtenerTodos(): List<UsuarioEntity>   // LISTA (para admin)
    //saber la cantidad de registros de la tabla users
    @Query("SELECT COUNT(*) FROM users")
    suspend fun contar(): Int   // CUÁNTOS USUARIOS HAY
}