package com.example.mygestiondietas.modelo

import androidx.room.*

@Dao
interface IDaoIngrediente {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertaIngrediente(ingrediente: Ingrediente)

    @Query("SELECT * FROM ingrediente WHERE componentePadreId = :componenteId")
    suspend fun getIngredientesByComponente(componenteId: String): List<Ingrediente>

    @Query("SELECT * FROM ingrediente WHERE componentePadreId = :componenteId AND componenteHijoId = :hijoId")
    suspend fun getIngredienteByComponente(componenteId: String, hijoId: String): Ingrediente?

    @Delete
    suspend fun deleteIngrediente(ingrediente: Ingrediente)

    @Update
    suspend fun updateIngrediente(ingrediente: Ingrediente)

    @Query("DELETE FROM ingrediente WHERE componentePadreId = :componenteId")
    suspend fun deleteIngredientesByComponente(componenteId: String)

    @Query("DELETE FROM ingrediente WHERE componenteHijoId = :componenteId")
    suspend fun deleteIngredientesByComponenteHijo(componenteId: String)

    @Query("DELETE FROM ingrediente WHERE componentePadreId = :componenteId")
    suspend fun deleteIngredientesByComponentePadre(componenteId: String)

}
