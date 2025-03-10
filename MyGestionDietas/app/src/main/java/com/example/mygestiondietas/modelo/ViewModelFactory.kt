package com.example.mygestiondietas.modelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val repository: ComponenteDietaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComponenteDietaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ComponenteDietaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}