package com.example.huerto_hogar.ui.screen.registro

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.R
import com.example.huerto_hogar.data.UsuarioDatabase
import com.example.huerto_hogar.data.UsuarioDatabaseProvider
import com.example.huerto_hogar.model.Usuario
import com.example.huerto_hogar.navigation.AppScreens
import com.example.huerto_hogar.ui.theme.HuertoHogarTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroViewModel(private val db: UsuarioDatabase) : ViewModel() {

    private val usuarioDao = db.usuarioDao()

    var nombre = mutableStateOf("")
    var correo = mutableStateOf("")
    var contrasena = mutableStateOf("")
    var mensajeError = mutableStateOf("")

    fun registrarUsuario(onSuccess: () -> Unit) {
        if (nombre.value.isBlank() || correo.value.isBlank() || contrasena.value.isBlank()) {
            mensajeError.value = "Todos los campos son obligatorios"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val existe = usuarioDao.obtenerUsuarioPorCorreo(correo.value)
            if (existe != null) {
                withContext(Dispatchers.Main) {
                    mensajeError.value = "Este correo ya está registrado"
                }
            } else {
                val nuevoUsuario = Usuario(
                    id = 0, // Room autogenera
                    nombre = nombre.value,
                    correo = correo.value,
                    contrasena = contrasena.value
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

@Composable
fun RegistroScreen(navController: NavController) {
    val context = LocalContext.current

    // Base de datos
    val db = remember { UsuarioDatabaseProvider.getDatabase(context) }

    // ViewModel con factory para pasar la DB
    val registroViewModel: RegistroViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
                    return RegistroViewModel(db) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    val nombre by registroViewModel.nombre
    val correo by registroViewModel.correo
    val contrasena by registroViewModel.contrasena
    val mensajeError by registroViewModel.mensajeError

    HuertoHogarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.huerto_logo),
                        contentDescription = "Logo Huerto Hogar",
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Registro de Usuario",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { registroViewModel.nombre.value = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { registroViewModel.correo.value = it },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { registroViewModel.contrasena.value = it },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            registroViewModel.registrarUsuario {
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                navController.navigate(AppScreens.Login.route) {
                                    popUpTo(AppScreens.Registro.route) { inclusive = true }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .widthIn(min = 150.dp, max = 300.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text("Registrar")
                    }

                    if (mensajeError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = mensajeError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
