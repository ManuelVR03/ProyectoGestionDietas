package com.example.mygestiondietas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygestiondietas.baseDatos.AppDatabase
import com.example.mygestiondietas.componentes.Drawer
import com.example.mygestiondietas.modelo.*
import com.example.mygestiondietas.pantallas.*
import com.example.mygestiondietas.ui.theme.MyGestionDietasTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = ComponenteDietaRepository(database.componenteDietaDao(), database.ingredienteDao())
        val viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(ComponenteDietaViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            val navigationController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            MyGestionDietasTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        Drawer { route ->
                            scope.launch { drawerState.close() }
                            navigationController.navigate(route)
                        }
                    }
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Principal(
                            name = "Android",
                            navigationController = navigationController,
                            modifier = Modifier.padding(innerPadding),
                            viewModel,
                            onOpenDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Principal(
    name: String,
    navigationController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ComponenteDietaViewModel,
    onOpenDrawer: () -> Unit)
{
    Column {
        NavHost(navController = navigationController, startDestination = Rutas.Inicio.ruta) {
            composable(Rutas.Inicio.ruta) {
                Inicio(onOpenDrawer)
            }
            composable(Rutas.FormularioSP.ruta){
                FormularioSP(viewModel)
            }
            composable(Rutas.FormularioRMD.ruta){
                FormularioRMD(viewModel)
            }
            composable(Rutas.ListadoSP.ruta){
                ListadoSP(viewModel)
            }
            composable(Rutas.ListadoRecetas.ruta){
                ListadoRecetas(viewModel)
            }
            composable(Rutas.ListadoMenus.ruta){
                ListadoMenus(viewModel)
            }
            composable(Rutas.ListadoDietas.ruta) {
                ListadoDietas(viewModel)
            }
        }
    }
}