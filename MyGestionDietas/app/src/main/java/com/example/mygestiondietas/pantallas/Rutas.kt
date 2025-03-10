package com.example.mygestiondietas.pantallas

sealed class Rutas(val ruta: String) {
    object Inicio: Rutas("inicio")
    object FormularioSP: Rutas("formularioSP")
    object FormularioRMD: Rutas("formularioRMD")
    object ListadoSP: Rutas("listadoSP")
    object ListadoRecetas: Rutas("listadoRecetas")
    object ListadoMenus: Rutas("listadoMenus")
    object ListadoDietas: Rutas("listadoDietas")
}