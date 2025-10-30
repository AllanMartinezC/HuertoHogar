package com.example.huerto_hogar.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto_hogar.data.UsuarioDatabase
import com.example.huerto_hogar.model.PerfilUiState
import com.example.huerto_hogar.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PerfilViewModel(
    private val db: UsuarioDatabase,
    private val usuarioInicial: Usuario
) : ViewModel() {

    // Estado general del perfil
    var uiState = mutableStateOf(
        PerfilUiState(
            nombre = usuarioInicial.nombre,
            correo = usuarioInicial.correo,
        )
    )
        private set

    // Ruta persistente de la foto
    var fotoPath = mutableStateOf(usuarioInicial.fotoUri)

    /**
     * Guarda la foto seleccionada en almacenamiento interno
     * y actualiza la base de datos Room con la ruta local.
     */
    fun guardarFotoEnApp(context: Context, contentUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Copiar imagen al almacenamiento interno
                val fileName = "perfil_${usuarioInicial.id}.jpg"
                val outFile = File(context.filesDir, fileName)

                context.contentResolver.openInputStream(contentUri)?.use { input ->
                    outFile.outputStream().use { output -> input.copyTo(output) }
                }

                // Guardar la ruta en la BD
                db.usuarioDao().actualizarFoto(usuarioInicial.id, outFile.absolutePath)

                // Actualizar estado UI en el hilo principal
                withContext(Dispatchers.Main) {
                    fotoPath.value = outFile.absolutePath
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    fun cerrarSesion(onCerrar: () -> Unit) {
        uiState.value = PerfilUiState()
        onCerrar()
    }
}
