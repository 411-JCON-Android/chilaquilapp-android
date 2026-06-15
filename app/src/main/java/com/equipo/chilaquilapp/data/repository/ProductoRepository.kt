package com.equipo.chilaquilapp.data.repository

import com.equipo.chilaquilapp.data.remote.ApiService
import com.equipo.chilaquilapp.domain.model.Extra
import com.equipo.chilaquilapp.domain.model.Producto
import com.equipo.chilaquilapp.domain.model.ProductoDetalle
import com.equipo.chilaquilapp.domain.model.Proteina
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun obtenerProductos(): Resultado<List<Producto>> = safeApiCall {
        apiService.obtenerProductos().toResultado { lista -> lista.map { it.toDomain() } }
    }

    suspend fun obtenerProducto(id: Int): Resultado<ProductoDetalle> = safeApiCall {
        apiService.obtenerProducto(id).toResultado { it.toDomain() }
    }

    suspend fun obtenerProteinas(): Resultado<List<Proteina>> = safeApiCall {
        apiService.obtenerProteinas().toResultado { lista -> lista.map { it.toDomain() } }
    }

    suspend fun obtenerExtras(): Resultado<List<Extra>> = safeApiCall {
        apiService.obtenerExtras().toResultado { lista -> lista.map { it.toDomain() } }
    }
}
