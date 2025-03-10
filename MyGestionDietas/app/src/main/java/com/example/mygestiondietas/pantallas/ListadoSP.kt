package com.example.mygestiondietas.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mygestiondietas.componentes.AlertDialogSP
import com.example.mygestiondietas.modelo.ComponenteDietaViewModel
import com.example.mygestiondietas.modelo.ComponenteDieta
import com.example.mygestiondietas.modelo.TipoComponente

@Composable
fun ListadoSP(viewModel: ComponenteDietaViewModel) {
    val componentes by viewModel.componentes.collectAsState()
    var componenteSeleccionado by remember { mutableStateOf<ComponenteDieta?>(null) }

    LaunchedEffect(Unit, componentes) {
        viewModel.loadComponentesByTipoDoble(TipoComponente.SIMPLE, TipoComponente.PROCESADO)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 55.dp)
    ) {
        Text("Componentes Simples y Procesados", style = MaterialTheme.typography.headlineMedium)

        LazyColumn {
            items(componentes) { componente ->
                ComponenteItem(componente, viewModel, onClick = { componenteSeleccionado = it })
            }
        }

        componenteSeleccionado?.let {
            AlertDialogSP(
                componente = it,
                onDismiss = { componenteSeleccionado = null },
                onDelete = {
                    viewModel.deleteComponente(it)
                    viewModel.loadComponentesByTipoDoble(TipoComponente.SIMPLE, TipoComponente.PROCESADO)
                    componenteSeleccionado = null
                },
                onUpdate = { updatedComponente ->
                    viewModel.updateComponente(updatedComponente)
                    viewModel.loadComponentesByTipoDoble(TipoComponente.SIMPLE, TipoComponente.PROCESADO)
                    componenteSeleccionado = null
                }
            )
        }
    }
}


@Composable
fun ComponenteItem(componente: ComponenteDieta, viewModel: ComponenteDietaViewModel, onClick: (ComponenteDieta) -> Unit) {
    var kcal by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(componente.id, componente.grHC_ini, componente.grLip_ini, componente.grPro_ini) {
        viewModel.getKcalAsync(componente.id) { kcal = it }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(componente) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = componente.nombre,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Tipo: ${componente.tipo}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )

            Text(text = "H. Carbono: ${componente.grHC_ini}g", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF388E3C))
            Text(text = "Lípidos: ${componente.grLip_ini}g", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD32F2F))
            Text(text = "Proteínas: ${componente.grPro_ini}g", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1976D2))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Kcal: %.2f kcal".format(kcal), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}
