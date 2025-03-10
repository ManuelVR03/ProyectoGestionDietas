package com.example.mygestiondietas.pantallas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygestiondietas.modelo.ComponenteDietaViewModel
import com.example.mygestiondietas.modelo.ComponenteDieta
import com.example.mygestiondietas.modelo.TipoComponente

@Composable
fun FormularioSP(viewModel: ComponenteDietaViewModel) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var grHC by remember { mutableStateOf("") }
    var grLip by remember { mutableStateOf("") }
    var grPro by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf(TipoComponente.SIMPLE) }

    fun resetFields() {
        nombre = ""
        grHC = ""
        grLip = ""
        grPro = ""
        tipoSeleccionado = TipoComponente.SIMPLE
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .padding(top = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Componente Simple o Procesado",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = grHC,
            onValueChange = { grHC = it },
            label = { Text("Gramos Hidratos de Carbono") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = grLip,
            onValueChange = { grLip = it },
            label = { Text("Gramos Lípidos") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = grPro,
            onValueChange = { grPro = it },
            label = { Text("Gramos Proteína") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Selecciona el tipo de Componente:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            TipoComponente.entries
                .filter { it == TipoComponente.SIMPLE || it == TipoComponente.PROCESADO }
                .forEach { tipo ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = (tipo == tipoSeleccionado),
                                onClick = { tipoSeleccionado = tipo }
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (tipo == tipoSeleccionado),
                            onClick = { tipoSeleccionado = tipo },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            tipo.name,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    val componente = ComponenteDieta(
                        nombre = nombre,
                        tipo = tipoSeleccionado,
                        grHC_ini = grHC.toDoubleOrNull() ?: 0.0,
                        grLip_ini = grLip.toDoubleOrNull() ?: 0.0,
                        grPro_ini = grPro.toDoubleOrNull() ?: 0.0
                    )
                    viewModel.insertaComponente(componente)

                    Toast.makeText(context, "Componente $nombre guardado", Toast.LENGTH_SHORT).show()

                    resetFields()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    resetFields()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reset", fontSize = 18.sp)
            }
        }
    }
}