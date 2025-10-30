package com.example.huerto_hogar.ui.screen.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.huerto_hogar.navigation.AppScreens
import com.example.huerto_hogar.R

@Composable


fun InicioScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //  Logo
            Image(
                painter = painterResource(id = R.drawable.huerto_logo),
                contentDescription = "Logo Huerto Hogar",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            //  Título
            Text(
                text = "Huerto Hogar",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            //  Slogan
            Text(
                text = "Frutas frescas directo a tu mesa 🍎",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(48.dp))

            //  Botón
            Button(onClick = {
                navController.navigate(AppScreens.Login.route)
            }) {
                Text(text = "Comenzar")
            }
        }
    }
}
