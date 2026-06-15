package com.equipo.chilaquilapp.data.remote.dto

import com.equipo.chilaquilapp.domain.model.PedidoCreado
import com.equipo.chilaquilapp.domain.model.PedidoDetalle
import com.equipo.chilaquilapp.domain.model.PedidoProducto
import com.equipo.chilaquilapp.domain.model.PedidoResumen

data class CrearPedidoRequest(
    val usuarioId: Int,
    val productoId: Int,
    val cantidad: Int,
    val proteinaIds: List<Int>,
    val extraIds: List<Int>,
    val direccionEntrega: String
)

data class CrearPedidoResponse(
    val id: Int,
    val total: Double,
    val estado: String,
    val creadoEn: String
) {
    fun toDomain() = PedidoCreado(
        id = id,
        total = total,
        estado = estado,
        creadoEn = creadoEn
    )
}

data class PedidoResumenDto(
    val id: Int,
    val productoNombre: String,
    val cantidad: Int,
    val total: Double,
    val creadoEn: String
) {
    fun toDomain() = PedidoResumen(
        id = id,
        productoNombre = productoNombre,
        cantidad = cantidad,
        total = total,
        creadoEn = creadoEn
    )
}

data class PedidoProductoDto(
    val id: Int,
    val nombre: String,
    val imagen: String
) {
    fun toDomain() = PedidoProducto(id = id, nombre = nombre, imagen = imagen)
}

data class PedidoDetalleDto(
    val id: Int,
    val producto: PedidoProductoDto,
    val cantidad: Int,
    val proteinas: List<ProteinaDto>,
    val extras: List<ExtraDto>,
    val direccionEntrega: String,
    val total: Double,
    val creadoEn: String
) {
    fun toDomain() = PedidoDetalle(
        id = id,
        producto = producto.toDomain(),
        cantidad = cantidad,
        proteinas = proteinas.map { it.toDomain() },
        extras = extras.map { it.toDomain() },
        direccionEntrega = direccionEntrega,
        total = total,
        creadoEn = creadoEn
    )
}
