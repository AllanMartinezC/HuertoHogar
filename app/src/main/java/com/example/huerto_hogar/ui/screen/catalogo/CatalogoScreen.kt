package com.example.huerto_hogar.ui.screen.catalogo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huerto_hogar.ui.components.ItemProducto
import com.example.huerto_hogar.viewmodel.CatalogoViewModel

@Composable
fun CatalogoScreen(
    catalogoViewModel: CatalogoViewModel = viewModel()
) {
    val productos = catalogoViewModel.productos

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        items(productos) { producto ->
            ItemProducto(
                nombre = producto.nombre,
                precio = producto.precio,
                imagenRes = producto.imagenRes
            )
        }
    }
}
