package com.equipo.chilaquilapp.data.remote.dto

import com.equipo.chilaquilapp.domain.model.Producto
import com.equipo.chilaquilapp.domain.model.ProductoDetalle

data class ProductoDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val salsa: String
) {
    fun toDomain() = Producto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagen = imagen,
        salsa = salsa
    )
}

data class ProductoDetalleDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val salsa: String,
    val proteinas: List<ProteinaDto>,
    val extras: List<ExtraDto>
) {
    fun toDomain() = ProductoDetalle(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        imagen = imagen,
        salsa = salsa,
        proteinas = proteinas.map { it.toDomain() },
        extras = extras.map { it.toDomain() }
    )
}
