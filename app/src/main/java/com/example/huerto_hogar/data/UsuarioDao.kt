package com.example.huerto_hogar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.huerto_hogar.model.Usuario

// data/UsuarioDao.kt
@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM Usuario")
    suspend fun obtenerUsuarios(): List<Usuario>

    @Query("SELECT * FROM Usuario WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun obtenerUsuarioPorCredenciales(correo: String, contrasena: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE id = :id LIMIT 1")
    suspend fun obtenerUsuarioPorId(id: Int): Usuario?

    @Query("UPDATE Usuario SET fotoUri = :path WHERE id = :id")
    suspend fun actualizarFoto(id: Int, path: String)

}
