package com.example.mygestiondietas.componentes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mygestiondietas.modelo.ComponenteDietaViewModel
import com.example.mygestiondietas.modelo.Ingrediente
import com.example.mygestiondietas.modelo.ComponenteDieta

@Composable
fun AlertDialogRMD(
    componente: ComponenteDieta,
    ingredientes: List<Ingrediente>,
    viewModel: ComponenteDietaViewModel,
    onDismiss: () -> Unit,
    onUpdate: (ComponenteDieta, List<Ingrediente>) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf(componente.nombre) }
    var ingredientesEditados by remember { mutableStateOf(ingredientes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Componente") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del componente") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text("Ingredientes:", style = MaterialTheme.typography.titleSmall)

                LazyColumn {
                    items(ingredientesEditados) { ingrediente ->
                        var cantidad by remember { mutableStateOf(ingrediente.cantidad.toString()) }
                        var name by remember { mutableStateOf<String?>(null) }
                        LaunchedEffect(ingrediente.componenteHijoId) {
                            viewModel.getComponenteById(ingrediente.componenteHijoId) { componente ->
                                name = componente?.nombre
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            name?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                            OutlinedTextField(
                                value = cantidad,
                                onValueChange = {
                                    cantidad = it
                                    ingredientesEditados = ingredientesEditados.map { ing ->
                                        if (ing.componenteHijoId == ingrediente.componenteHijoId) {
                                            ing.copy(cantidad = it.toDoubleOrNull() ?: 100.0)
                                        } else ing
                                    }
                                },
                                label = { Text("Cantidad (g)") },
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val componenteActualizado = componente.copy(nombre = nombre)
                    onUpdate(componenteActualizado, ingredientesEditados)
                    Toast.makeText(context, "Componente actualizado", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Row {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.onError)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}
