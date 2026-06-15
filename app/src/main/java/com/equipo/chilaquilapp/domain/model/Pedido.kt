package com.equipo.chilaquilapp.domain.model

data class PedidoCreado(
    val id: Int,
    val total: Double,
    val estado: String,
    val creadoEn: String
)

data class PedidoResumen(
    val id: Int,
    val productoNombre: String,
    val cantidad: Int,
    val total: Double,
    val creadoEn: String
)

data class PedidoDetalle(
    val id: Int,
    val producto: PedidoProducto,
    val cantidad: Int,
    val proteinas: List<Proteina>,
    val extras: List<Extra>,
    val direccionEntrega: String,
    val total: Double,
    val creadoEn: String
)

data class PedidoProducto(
    val id: Int,
    val nombre: String,
    val imagen: String
)
