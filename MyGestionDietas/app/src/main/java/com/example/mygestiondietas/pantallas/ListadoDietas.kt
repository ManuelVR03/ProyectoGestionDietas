package com.example.mygestiondietas.pantallas

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mygestiondietas.componentes.AlertDialogRMD
import com.example.mygestiondietas.modelo.ComponenteDietaViewModel
import com.example.mygestiondietas.modelo.ComponenteDieta
import com.example.mygestiondietas.modelo.Ingrediente
import com.example.mygestiondietas.modelo.TipoComponente

@Composable
fun ListadoDietas(viewModel: ComponenteDietaViewModel) {
    val dietas by viewModel.componentes.collectAsState()
    var dietaSeleccionado by remember { mutableStateOf<ComponenteDieta?>(null) }

    LaunchedEffect(Unit, dietas) {
        viewModel.loadComponentesByTipo(TipoComponente.DIETA)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 55.dp)
    ) {
        Text("Listado de Dietas", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(dietas) { dieta ->
                DietaItem(dieta, viewModel) {
                    dietaSeleccionado = it
                    viewModel.loadIngredientes(it.id)
                }
            }
        }
    }

    dietaSeleccionado?.let {
        AlertDialogRMD(
            componente = it,
            ingredientes = viewModel.ingredientes.collectAsState().value,
            viewModel = viewModel,
            onDismiss = { dietaSeleccionado = null },
            onUpdate = { dietaActualizada, ingredientesEditados ->
                viewModel.updateComponente(dietaActualizada)
                ingredientesEditados.forEach { viewModel.updateIngrediente(it) }
                viewModel.loadComponentesByTipo(TipoComponente.DIETA)
                dietaSeleccionado = null
            },
            onDelete = {
                viewModel.deleteComponente(it)
                viewModel.loadComponentesByTipo(TipoComponente.DIETA)
                dietaSeleccionado = null
            }
        )
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun DietaItem(dieta: ComponenteDieta, viewModel: ComponenteDietaViewModel, onClick: (ComponenteDieta) -> Unit) {
    var kcal by remember { mutableDoubleStateOf(0.0) }
    var ingredientes by remember { mutableStateOf<List<Ingrediente>>(emptyList()) }

    LaunchedEffect(dieta.id) {
        viewModel.getKcalAsync(dieta.id) { kcal = it }
        viewModel.getIngredientesByComponente(dieta.id) { lista ->
            ingredientes = lista
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(dieta) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = dieta.nombre,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Kcal: %.2f kcal".format(kcal),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingredientes:",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .heightIn(max = 150.dp) // 🔹 Evitar altura infinita
                    .padding(start = 8.dp)
            ) {
                ingredientes.forEach { ingrediente ->
                    var nombre by remember { mutableStateOf<String?>(null) }
                    LaunchedEffect(ingrediente.componenteHijoId) {
                        viewModel.getComponenteById(ingrediente.componenteHijoId) { componente ->
                            nombre = componente?.nombre
                        }
                    }
                    Text(
                        text = "- ${ingrediente.cantidad}g de $nombre",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}