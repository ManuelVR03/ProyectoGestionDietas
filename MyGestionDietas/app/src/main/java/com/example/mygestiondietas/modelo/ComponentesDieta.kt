package com.example.mygestiondietas.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

enum class TipoComponente {

    SIMPLE,PROCESADO,MENU,RECETA,DIETA

}

@Entity(tableName = "componente_dieta")
data class ComponenteDieta(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var nombre: String,
    var tipo: TipoComponente = TipoComponente.SIMPLE,
    var grHC_ini: Double=0.0,
    var grLip_ini:Double=0.0,
    var grPro_ini:Double=0.0): Serializable

