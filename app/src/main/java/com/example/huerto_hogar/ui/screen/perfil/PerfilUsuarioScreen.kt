package com.example.huerto_hogar.ui.screen.perfil

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.huerto_hogar.R
import com.example.huerto_hogar.data.UsuarioDatabaseProvider
import com.example.huerto_hogar.model.Usuario
import com.example.huerto_hogar.viewmodel.PerfilViewModel
import java.io.File

@Composable
fun PerfilScreen(
    usuarioLogueado: Usuario,
    onCerrarSesion: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { UsuarioDatabaseProvider.getDatabase(context) }

    // ViewModel con DB + usuario inicial
    val perfilViewModel: PerfilViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return PerfilViewModel(db, usuarioLogueado) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    val uiState by perfilViewModel.uiState
    val fotoPath by perfilViewModel.fotoPath

    // Selector de imagen desde galería
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Copia a almacenamiento interno y persiste ruta en DB
            perfilViewModel.guardarFotoEnApp(context, uri)
        }
    }

    PerfilContent(
        nombre = uiState.nombre,
        correo = uiState.correo,
        fotoPath = fotoPath,
        onCambiarFoto = { launcher.launch("image/*") },
        onCerrarSesion = { perfilViewModel.cerrarSesion(onCerrarSesion) }
    )
}

@Composable
private fun PerfilContent(
    nombre: String,
    correo: String,
    fotoPath: String?,
    onCambiarFoto: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto de perfil (desde almacenamiento interno si existe)
        val painter = if (!fotoPath.isNullOrBlank()) {
            rememberAsyncImagePainter(model = File(fotoPath!!))
        } else {
            painterResource(id = R.drawable.default_profile)
        }

        Image(
            painter = painter,
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = onCambiarFoto) {
            Text("Cambiar foto")
        }

        Spacer(Modifier.height(24.dp))

        Text("Nombre: $nombre", fontWeight = FontWeight.Bold)
        Text("Correo: $correo")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onCerrarSesion,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar sesión")
        }
    }
}
