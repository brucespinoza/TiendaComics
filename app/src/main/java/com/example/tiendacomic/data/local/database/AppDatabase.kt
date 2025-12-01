package com.example.tiendacomic.data.local.database



import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tiendacomic.data.local.usuario.ComicDao
import com.example.tiendacomic.data.local.usuario.ComicEntity
import com.example.tiendacomic.data.local.usuario.UsuarioDao
import com.example.tiendacomic.data.local.usuario.UsuarioEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UsuarioEntity::class, ComicEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    //exponemos los dao que usaremos para los insert por defecto
    abstract fun usuarioDao(): UsuarioDao
    //obtener la instancia de la BD
    abstract fun comicDao(): ComicDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "tienda_comic.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                //buildear la BD
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    //si es la primera vez que se ejecuta - creamos una funcion callback
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            //mediante una corrutina ejecute de manera sincronica
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).usuarioDao()
                                //insert precargados a ejecutar
                                val seed = listOf(
                                    UsuarioEntity(
                                        nombre = "Admin",
                                        rut = "11.111.111-1",
                                        correo = "admin@gmail.com",
                                        contrasena = "Admin123!",
                                        rol = "admin"
                                    ),
                                    UsuarioEntity(
                                        nombre = "Demo",
                                        rut = "12.345.678-5",
                                        correo = "demo@duoc.cl",
                                        contrasena = "Demo123!",
                                        rol = "usuario"
                                    )
                                )

                                if (dao.contar() == 0) {
                                    seed.forEach { dao.insertar(it) }
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
