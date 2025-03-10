package com.example.mygestiondietas.modelo

class ComponenteDietaRepository(private val dao: IDaoCD, private val ingredienteDao: IDaoIngrediente) {

    suspend fun insertaComponente(componente: ComponenteDieta) {
        dao.insertaComponente(componente)
    }

    suspend fun insertaIngrediente(ingrediente: Ingrediente) {
        ingredienteDao.insertaIngrediente(ingrediente)
    }

    suspend fun getAllComponentes(): List<ComponenteDieta> {
        return dao.getAllComponentes()
    }

    suspend fun getComponentesByTipo(tipo: TipoComponente): List<ComponenteDieta> {
        return dao.getComponentesByTipo(tipo)
    }

    suspend fun getComponentesByTipos(tipo1: TipoComponente, tipo2: TipoComponente): List<ComponenteDieta> {
        return dao.getComponentesByTipos(tipo1.name, tipo2.name)
    }

    suspend fun getComponenteById(id: String): ComponenteDieta? {
        return dao.getComponenteById(id)
    }

    suspend fun getIngredientesByComponente(componentePadreId: String): List<Ingrediente> {
        return ingredienteDao.getIngredientesByComponente(componentePadreId)
    }

    suspend fun getKcalRecursivo(id: String): Double {
        val componente = getComponenteById(id) ?: return 0.0

        // 🔹 Si es SIMPLE o PROCESADO, obtenemos kcal con la consulta SQL
        if (componente.tipo == TipoComponente.SIMPLE || componente.tipo == TipoComponente.PROCESADO) {
            return getKcal(id) ?: 0.0
        }

        // 🔹 Si es una RECETA, MENÚ o DIETA, calculamos kcal en Kotlin
        val ingredientes = getIngredientesByComponente(id)
        var kcalTotal = 0.0

        for (ingrediente in ingredientes) {
            val kcalHijo = getKcalRecursivo(ingrediente.componenteHijoId)
            kcalTotal += (ingrediente.cantidad / 100) * kcalHijo
        }

        return kcalTotal
    }


    suspend fun getKcal(id: String): Double? {
        return dao.getKcal(id)
    }

    suspend fun deleteComponente(componente: ComponenteDieta) {
        ingredienteDao.deleteIngredientesByComponenteHijo(componente.id)
        ingredienteDao.deleteIngredientesByComponentePadre(componente.id)
        dao.deleteComponente(componente)
    }

    suspend fun updateComponente(componente: ComponenteDieta) {
        dao.updateComponente(componente)
    }

    suspend fun updateIngrediente(ingrediente: Ingrediente) {
        ingredienteDao.updateIngrediente(ingrediente)
    }
}
