package com.example.huerto_hogar.viewmodel

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.data.UsuarioDatabase
import com.example.huerto_hogar.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroViewModel(private val db: UsuarioDatabase) : ViewModel() {

    private val usuarioDao = db.usuarioDao()

    var nombre = mutableStateOf("")
    var correo = mutableStateOf("")
    var contrasena = mutableStateOf("")
    var mensajeError = mutableStateOf("")

    private fun esCorreoValido(texto: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(texto.trim()).matches()

    fun registrarUsuario(onSuccess: () -> Unit) {
        val n = nombre.value.trim()
        val c = correo.value.trim()
        val p = contrasena.value

        // 1) campos obligatorios
        if (n.isEmpty() || c.isEmpty() || p.isEmpty()) {
            mensajeError.value = "Todos los campos son obligatorios"
            return
        }

        // 2) formato del correo
        if (!esCorreoValido(c)) {
            mensajeError.value = "El correo debe tener un formato válido (ej: usuario@dominio.com)"
            return
        }

        // 3) (opcional) mínima seguridad de contraseña
        if (p.length < 6) {
            mensajeError.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        // 4) verificar existencia en BD e insertar
        viewModelScope.launch(Dispatchers.IO) {
            val existe = usuarioDao.obtenerUsuarioPorCorreo(c)
            if (existe != null) {
                withContext(Dispatchers.Main) {
                    mensajeError.value = "Este correo ya está registrado"
                }
            } else {
                val nuevoUsuario = Usuario(
                    id = 0,
                    nombre = n,
                    correo = c,
                    contrasena = p
                )
                usuarioDao.insertar(nuevoUsuario)
                withContext(Dispatchers.Main) {
                    mensajeError.value = ""
                    onSuccess()
                }
            }
        }
    }
}
