package com.example.mygestiondietas.pantallas

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
fun ListadoRecetas(viewModel: ComponenteDietaViewModel) {
    val recetas by viewModel.componentes.collectAsState()
    var recetaSeleccionada by remember { mutableStateOf<ComponenteDieta?>(null) }

    LaunchedEffect(Unit, recetas) {
        viewModel.loadComponentesByTipo(TipoComponente.RECETA)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 55.dp)
    ) {
        Text("Listado de Recetas", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(recetas) { receta ->
                RecetaItem(receta, viewModel) {
                    recetaSeleccionada = it
                    viewModel.loadIngredientes(it.id)
                }
            }
        }
    }

    recetaSeleccionada?.let {
        AlertDialogRMD(
            componente = it,
            ingredientes = viewModel.ingredientes.collectAsState().value,
            viewModel = viewModel,
            onDismiss = { recetaSeleccionada = null },
            onUpdate = { recetaActualizada, ingredientesEditados ->
                viewModel.updateComponente(recetaActualizada)
                ingredientesEditados.forEach { viewModel.updateIngrediente(it) }
                viewModel.loadComponentesByTipo(TipoComponente.RECETA)
                recetaSeleccionada = null
            },
            onDelete = {
                viewModel.deleteComponente(it)
                viewModel.loadComponentesByTipo(TipoComponente.RECETA)
                recetaSeleccionada = null
            }
        )
    }
}

@Composable
fun RecetaItem(receta: ComponenteDieta, viewModel: ComponenteDietaViewModel, onClick: (ComponenteDieta) -> Unit) {
    var kcal by remember { mutableDoubleStateOf(0.0) }
    var ingredientes by remember { mutableStateOf<List<Ingrediente>>(emptyList()) }

    LaunchedEffect(receta.id) {
        viewModel.getKcalAsync(receta.id) { kcal = it }
        viewModel.getIngredientesByComponente(receta.id) { lista ->
            ingredientes = lista
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(receta) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = receta.nombre,
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
                    .heightIn(max = 150.dp)
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
