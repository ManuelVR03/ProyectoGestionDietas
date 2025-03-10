package com.example.mygestiondietas.modelo

import java.io.Serializable
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "ingrediente",
    primaryKeys = ["componentePadreId", "componenteHijoId"],
    foreignKeys = [
        ForeignKey(
            entity = ComponenteDieta::class,
            parentColumns = ["id"],
            childColumns = ["componentePadreId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ComponenteDieta::class,
            parentColumns = ["id"],
            childColumns = ["componenteHijoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Ingrediente (
    val componentePadreId: String,
    val componenteHijoId: String,
    var cantidad: Double = 100.0
) : Serializable