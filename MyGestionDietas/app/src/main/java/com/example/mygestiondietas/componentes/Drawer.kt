package com.example.mygestiondietas.componentes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.AddCircle
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.mygestiondietas.pantallas.Rutas

@Composable
fun Drawer(onDestinationClicked: (String) -> Unit) {
    val tabs = listOf(
        TabData("Inicio", Rutas.Inicio.ruta, Icons.Sharp.Home),
        TabData("Formulario Simple y Procesados", Rutas.FormularioSP.ruta, Icons.Sharp.Add),
        TabData("Formulario Recetas / Menús / Dietas", Rutas.FormularioRMD.ruta, Icons.Sharp.AddCircle),
        TabData("Listado Simples y Procesados", Rutas.ListadoSP.ruta, Icons.Sharp.Menu),
        TabData("Listado Recetas", Rutas.ListadoRecetas.ruta, Icons.Sharp.Menu),
        TabData("Listado Menús", Rutas.ListadoMenus.ruta, Icons.Sharp.Menu),
        TabData("Listado Dietas", Rutas.ListadoDietas.ruta, Icons.Sharp.Menu),
    )
    ModalDrawerSheet {
        Spacer(Modifier.height(24.dp))
        tabs.forEach { tab ->
            NavigationDrawerItem(
                label = { Text(text = tab.title) },
                selected = false,
                onClick = { onDestinationClicked(tab.route) },
                icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

data class TabData(
    val title: String,
    val route: String,
    val icon: ImageVector
)