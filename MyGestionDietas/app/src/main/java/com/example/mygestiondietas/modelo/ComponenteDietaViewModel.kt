package com.example.mygestiondietas.modelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ComponenteDietaViewModel(private val repository: ComponenteDietaRepository) : ViewModel() {

    private val _componentes = MutableStateFlow<List<ComponenteDieta>>(emptyList())
    val componentes: StateFlow<List<ComponenteDieta>> = _componentes

    private val _ingredientes = MutableStateFlow<List<Ingrediente>>(emptyList())
    val ingredientes: StateFlow<List<Ingrediente>> = _ingredientes

    fun insertaComponente(componente: ComponenteDieta) {
        viewModelScope.launch {
            repository.insertaComponente(componente)
            loadComponentes()
        }
    }

    fun insertaIngrediente(ingrediente: Ingrediente) {
        viewModelScope.launch {
            repository.insertaIngrediente(ingrediente)
            loadIngredientes(ingrediente.componentePadreId)
        }
    }

    fun loadComponentes() {
        viewModelScope.launch {
            _componentes.value = repository.getAllComponentes()
        }
    }

    fun loadComponentesByTipo(tipo: TipoComponente) {
        viewModelScope.launch {
            _componentes.value = repository.getComponentesByTipo(tipo)
        }
    }

    fun loadComponentesByTipoDoble(tipo1: TipoComponente, tipo2: TipoComponente) {
        viewModelScope.launch {
            _componentes.value = repository.getComponentesByTipos(tipo1, tipo2)
        }
    }

    fun loadIngredientes(componentePadreId: String) {
        viewModelScope.launch {
            _ingredientes.value = repository.getIngredientesByComponente(componentePadreId)
        }
    }

    fun getComponenteById(id: String, callback: (ComponenteDieta?) -> Unit) {
        viewModelScope.launch {
            val componente = repository.getComponenteById(id)
            callback(componente)
        }
    }

    fun getIngredientesByComponente(componentePadreId: String, callback: (List<Ingrediente>) -> Unit) {
        viewModelScope.launch {
            val ingredientes = repository.getIngredientesByComponente(componentePadreId)
            callback(ingredientes)
        }
    }

    fun getKcalAsync(id: String, callback: (Double) -> Unit) {
        viewModelScope.launch {
            val kcal = repository.getKcalRecursivo(id)
            callback(kcal)
        }
    }

    fun deleteComponente(componente: ComponenteDieta) {
        viewModelScope.launch {
            repository.deleteComponente(componente)
            loadComponentes()
        }
    }

    fun updateComponente(componente: ComponenteDieta) {
        viewModelScope.launch {
            repository.updateComponente(componente)
            loadComponentes()
        }
    }

    fun updateIngrediente(ingrediente: Ingrediente) {
        viewModelScope.launch {
            repository.updateIngrediente(ingrediente)
            loadIngredientes(ingrediente.componentePadreId)
        }
    }
}
