package com.example.tiendacomic.data.local.usuario

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val rut: String,
    val correo: String,
    val contrasena: String,
    val rol: String = "usuario"
    //Puede ser usuario premium o admin
)
