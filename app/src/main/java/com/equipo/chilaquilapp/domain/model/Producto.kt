package com.equipo.chilaquilapp.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val salsa: String
)

data class ProductoDetalle(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val salsa: String,
    val proteinas: List<Proteina>,
    val extras: List<Extra>
)
