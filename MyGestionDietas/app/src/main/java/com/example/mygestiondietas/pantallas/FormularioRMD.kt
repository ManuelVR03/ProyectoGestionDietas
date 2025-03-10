package com.example.mygestiondietas.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.dp
import com.example.mygestiondietas.modelo.ComponenteDietaViewModel
import com.example.mygestiondietas.modelo.Ingrediente
import com.example.mygestiondietas.modelo.ComponenteDieta
import com.example.mygestiondietas.modelo.TipoComponente

@Composable
fun FormularioRMD(viewModel: ComponenteDietaViewModel) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf(TipoComponente.RECETA) }
    val componentes by viewModel.componentes.collectAsState()
    var ingredientes by remember { mutableStateOf<List<Ingrediente>>(emptyList()) }
    var selectedComponente by remember { mutableStateOf<ComponenteDieta?>(null) }
    var cantidad by remember { mutableStateOf("") }

    LaunchedEffect(Unit, componentes) {
        viewModel.loadComponentes()
    }

    val componentesFiltrados = remember(tipoSeleccionado, componentes) {
        viewModel.loadComponentes()
        when (tipoSeleccionado) {
            TipoComponente.RECETA -> componentes.filter { it.tipo == TipoComponente.SIMPLE || it.tipo == TipoComponente.PROCESADO }
            TipoComponente.MENU -> componentes.filter { it.tipo == TipoComponente.SIMPLE || it.tipo == TipoComponente.PROCESADO || it.tipo == TipoComponente.RECETA }
            TipoComponente.DIETA -> componentes.filter { it.tipo == TipoComponente.SIMPLE || it.tipo == TipoComponente.PROCESADO || it.tipo == TipoComponente.RECETA || it.tipo == TipoComponente.MENU }
            else -> emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Receta, Menú o Dieta", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TipoComponente.entries
                .filter { it == TipoComponente.RECETA || it == TipoComponente.MENU || it == TipoComponente.DIETA }
                .forEach { tipo ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = tipo == tipoSeleccionado,
                            onClick = { tipoSeleccionado = tipo }
                        )
                        Text(text = tipo.name, modifier = Modifier.padding(start = 8.dp))
                    }
                }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Seleccionar Ingrediente:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            items(componentesFiltrados) { componente ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { selectedComponente = componente },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = componente.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Tipo: ${componente.tipo}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedComponente != null) {
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad (g)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val nuevoIngrediente = Ingrediente(
                        componentePadreId = "",
                        componenteHijoId = selectedComponente!!.id,
                        cantidad = cantidad.toDoubleOrNull() ?: 100.0
                    )
                    ingredientes = ingredientes + nuevoIngrediente
                    selectedComponente = null
                    cantidad = ""
                }
            ) {
                Text("Añadir Ingrediente")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        LazyColumn {
            items(ingredientes) { ing ->
                val componenteHijo = componentes.find { it.id == ing.componenteHijoId }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = componenteHijo?.nombre ?: "Desconocido",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Cantidad: ${ing.cantidad}g",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val nuevoComponente = ComponenteDieta(
                    nombre = nombre,
                    tipo = tipoSeleccionado
                )

                viewModel.insertaComponente(nuevoComponente)

                val componentePadreId = nuevoComponente.id
                val ingredientesActualizados =
                    ingredientes.map { it.copy(componentePadreId = componentePadreId) }
                ingredientesActualizados.forEach { viewModel.insertaIngrediente(it) }

                Toast.makeText(context, "Componente guardado correctamente", Toast.LENGTH_SHORT).show()

                nombre = ""
                tipoSeleccionado = TipoComponente.RECETA
                ingredientes = emptyList()
            }
        ) {
            Text("Guardar")
        }
    }
}
