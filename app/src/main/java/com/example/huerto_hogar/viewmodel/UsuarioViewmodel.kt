package com.example.huerto_hogar.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.data.UsuarioDatabase
import com.example.huerto_hogar.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val db: UsuarioDatabase
) : ViewModel() {

    private val usuarioDao = db.usuarioDao()

    // Lista observable para pantallas que quieran mostrar usuarios
    val usuarios = mutableStateListOf<Usuario>()

    /**
     * Inserta un usuario y refresca la lista local.
     */
    fun insertarUsuario(
        nombre: String,
        correo: String,
        contrasena: String,
        onResult: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nuevo = Usuario(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena
                )
                usuarioDao.insertar(nuevo)
                val lista = usuarioDao.obtenerUsuarios()
                withContext(Dispatchers.Main) {
                    usuarios.clear()
                    usuarios.addAll(lista)
                    onResult(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(false) }
            }
        }
    }

    /**
     * Carga todos los usuarios desde la BD a la lista observable.
     */
    fun obtenerUsuarios() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lista = usuarioDao.obtenerUsuarios()
                withContext(Dispatchers.Main) {
                    usuarios.clear()
                    usuarios.addAll(lista)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Login: devuelve el Usuario si las credenciales son válidas; null si no.
     */
    fun login(
        correo: String,
        contrasena: String,
        onResult: (Usuario?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val usuario = usuarioDao.obtenerUsuarioPorCredenciales(correo, contrasena)
                withContext(Dispatchers.Main) { onResult(usuario) }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(null) }
            }
        }
    }

    /**
     * Verifica si ya existe un usuario con ese correo.
     */
    fun existeCorreo(
        correo: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val existe = usuarioDao.obtenerUsuarioPorCorreo(correo) != null
            withContext(Dispatchers.Main) { onResult(existe) }
        }
    }

    /**
     * Actualiza la ruta de la foto para un usuario (por si lo necesitas fuera del PerfilViewModel).
     */
    fun actualizarFotoUsuario(
        usuarioId: Int,
        fotoPath: String,
        onResult: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                usuarioDao.actualizarFoto(usuarioId, fotoPath)
                withContext(Dispatchers.Main) { onResult(true) }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { onResult(false) }
            }
        }
    }
}
