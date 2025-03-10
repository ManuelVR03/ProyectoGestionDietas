package com.example.mygestiondietas.componentes

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.mygestiondietas.modelo.ComponenteDieta

@Composable
fun AlertDialogSP(
    componente: ComponenteDieta,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (ComponenteDieta) -> Unit
) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf(componente.nombre) }
    var grHC by remember { mutableStateOf(componente.grHC_ini.toString()) }
    var grLip by remember { mutableStateOf(componente.grLip_ini.toString()) }
    var grPro by remember { mutableStateOf(componente.grPro_ini.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Componente") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = grHC, onValueChange = { grHC = it }, label = { Text("Gramos HC") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = grLip, onValueChange = { grLip = it }, label = { Text("Gramos Lípidos") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = grPro, onValueChange = { grPro = it }, label = { Text("Gramos Proteínas") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedComponente = componente.copy(
                        nombre = nombre,
                        grHC_ini = grHC.toDoubleOrNull() ?: 0.0,
                        grLip_ini = grLip.toDoubleOrNull() ?: 0.0,
                        grPro_ini = grPro.toDoubleOrNull() ?: 0.0
                    )
                    onUpdate(updatedComponente)
                    Toast.makeText(context, "Componente actualizado correctamente", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Row {
                Button(onClick = {
                    onDelete()
                    Toast.makeText(context, "Componente eliminado correctamente", Toast.LENGTH_SHORT).show()
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Eliminar", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}
