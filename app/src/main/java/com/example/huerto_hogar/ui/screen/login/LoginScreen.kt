package com.example.huerto_hogar.ui.screen.login


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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huerto_hogar.R
import com.example.huerto_hogar.navigation.AppScreens
import com.example.huerto_hogar.ui.theme.HuertoHogarTheme
import com.example.huerto_hogar.data.UsuarioDatabaseProvider
import com.example.huerto_hogar.model.Usuario
import com.example.huerto_hogar.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (Usuario) -> Unit // <-- callback que devuelve el usuario logueado
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Base de datos
    val db = remember { UsuarioDatabaseProvider.getDatabase(context) }

    // ViewModel que maneja la base de datos
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
                    return UsuarioViewModel(db) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    HuertoHogarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.huerto_logo),
                    contentDescription = "Logo Huerto Hogar",
                    modifier = Modifier.size(180.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Iniciar Sesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    if (correo.isNotBlank() && contrasena.isNotBlank()) {
                        scope.launch {
                            // Login devuelve el usuario si existe, null si no
                            usuarioViewModel.login(correo, contrasena) { usuario ->
                                if (usuario != null) {
                                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(usuario) // <-- enviamos el usuario al callback
                                } else {
                                    mensajeError = "Correo o contraseña incorrectos"
                                }
                            }
                        }
                    } else {
                        mensajeError = "Debe ingresar correo y contraseña"
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
                    Text("Entrar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = { navController.navigate(AppScreens.Registro.route) }) {
                    Text("¿No tienes cuenta? Regístrate aquí")
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

