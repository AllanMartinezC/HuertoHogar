package com.example.huerto_hogar.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.R
import com.example.huerto_hogar.model.Producto

class CatalogoViewModel : ViewModel() {
    val productos = mutableStateListOf<Producto>()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        productos.addAll(
            listOf(
                Producto("Manzanas", 1200, R.drawable.manzanas," Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule"),
                Producto("Plátanos", 800, R.drawable.platanos," Plátanos maduros y dulces, perfectos para el desayuno o como snack\n" +
                        "energético."),
                Producto("Naranjas", 1000, R.drawable.naranjas,"Jugosas y ricas en vitamina C, estas naranjas Valencia son ideales para zumos frescos y refrescantes"),
                Producto("Pimientos", 1100, R.drawable.pimientos, " Pimientos rojos, amarillos y verdes, ideales para salteados y platos\n" +
                        "coloridos. "),
                Producto("Zanahoria", 1500, R.drawable.zanahoria ,"Zanahorias crujientes cultivadas sin pesticidas en la Región de O'Higgins")
            )
        )
    }
}
