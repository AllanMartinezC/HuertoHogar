package com.example.huerto_hogar.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.data.UsuarioDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val db: UsuarioDatabase) : ViewModel() {

    val correo = mutableStateOf("")
    val contrasena = mutableStateOf("")
    val mensajeError = mutableStateOf("")

    fun login(onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val usuario = db.usuarioDao().obtenerUsuarioPorCredenciales(correo.value, contrasena.value)
            withContext(Dispatchers.Main) {
                if (usuario != null) {
                    mensajeError.value = ""
                    onResult(true)
                } else {
                    mensajeError.value = "Correo o contraseña incorrectos"
                    onResult(false)
                }
            }
        }
    }
}
