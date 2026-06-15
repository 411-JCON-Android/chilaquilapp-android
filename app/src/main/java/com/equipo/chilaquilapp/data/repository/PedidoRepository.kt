package com.equipo.chilaquilapp.data.repository

import com.equipo.chilaquilapp.data.remote.ApiService
import com.equipo.chilaquilapp.data.remote.dto.CrearPedidoRequest
import com.equipo.chilaquilapp.domain.model.PedidoCreado
import com.equipo.chilaquilapp.domain.model.PedidoDetalle
import com.equipo.chilaquilapp.domain.model.PedidoResumen
import javax.inject.Inject

class PedidoRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun crearPedido(
        usuarioId: Int,
        productoId: Int,
        cantidad: Int,
        proteinaIds: List<Int>,
        extraIds: List<Int>,
        direccionEntrega: String
    ): Resultado<PedidoCreado> = safeApiCall {
        apiService.crearPedido(
            CrearPedidoRequest(
                usuarioId = usuarioId,
                productoId = productoId,
                cantidad = cantidad,
                proteinaIds = proteinaIds,
                extraIds = extraIds,
                direccionEntrega = direccionEntrega
            )
        ).toResultado { it.toDomain() }
    }

    suspend fun obtenerHistorial(usuarioId: Int): Resultado<List<PedidoResumen>> = safeApiCall {
        apiService.obtenerHistorial(usuarioId).toResultado { lista -> lista.map { it.toDomain() } }
    }

    suspend fun obtenerPedido(id: Int): Resultado<PedidoDetalle> = safeApiCall {
        apiService.obtenerPedido(id).toResultado { it.toDomain() }
    }
}
