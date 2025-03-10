package com.example.mygestiondietas.modelo

import androidx.room.*

@Dao
interface IDaoCD {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertaComponente(componente: ComponenteDieta)

    @Query("SELECT * FROM componente_dieta")
    suspend fun getAllComponentes(): List<ComponenteDieta>

    @Query("SELECT * FROM componente_dieta WHERE tipo = :tipo")
    suspend fun getComponentesByTipo(tipo: TipoComponente): List<ComponenteDieta>

    @Query("SELECT * FROM componente_dieta WHERE tipo = :tipo1 OR tipo = :tipo2")
    suspend fun getComponentesByTipos(tipo1: String, tipo2: String): List<ComponenteDieta>

    @Query("SELECT * FROM componente_dieta WHERE id = :id")
    suspend fun getComponenteById(id: String): ComponenteDieta?

    @Query("SELECT * FROM componente_dieta WHERE nombre = :nombre")
    suspend fun getComponenteByNombre(nombre: String): ComponenteDieta?

    @Query("""
    SELECT (4 * grHC_ini) + (4 * grPro_ini) + (9 * grLip_ini) 
    FROM componente_dieta 
    WHERE id = :id AND tipo IN ('SIMPLE', 'PROCESADO')
    """)
    suspend fun getKcal(id: String): Double?

    @Transaction
    suspend fun deleteComponenteWithIngredientes(componente: ComponenteDieta, ingredienteDao: IDaoIngrediente) {
        ingredienteDao.deleteIngredientesByComponente(componente.id)
        deleteComponente(componente)
    }

    @Delete
    suspend fun deleteComponente(componente: ComponenteDieta)

    @Update
    suspend fun updateComponente(componente: ComponenteDieta)
}
