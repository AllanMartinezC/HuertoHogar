package com.example.huerto_hogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.huerto_hogar.navigation.AppNavigation
import com.example.huerto_hogar.ui.theme.HuertoHogarTheme // usa el nombre de tu tema, puede variar según cómo se generó

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HuertoHogarTheme {
                AppNavigation()
            }
        }
    }
}
