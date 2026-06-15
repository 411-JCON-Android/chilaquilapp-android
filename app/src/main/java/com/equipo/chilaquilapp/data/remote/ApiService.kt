package com.equipo.chilaquilapp.data.remote

import com.equipo.chilaquilapp.data.remote.dto.CrearPedidoRequest
import com.equipo.chilaquilapp.data.remote.dto.CrearPedidoResponse
import com.equipo.chilaquilapp.data.remote.dto.ExtraDto
import com.equipo.chilaquilapp.data.remote.dto.LoginRequest
import com.equipo.chilaquilapp.data.remote.dto.PedidoDetalleDto
import com.equipo.chilaquilapp.data.remote.dto.PedidoResumenDto
import com.equipo.chilaquilapp.data.remote.dto.ProductoDetalleDto
import com.equipo.chilaquilapp.data.remote.dto.ProductoDto
import com.equipo.chilaquilapp.data.remote.dto.ProteinaDto
import com.equipo.chilaquilapp.data.remote.dto.RegistroRequest
import com.equipo.chilaquilapp.data.remote.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Endpoints de la API según los contratos de la Sección 5 del plan.
 * Autenticación simple sin token: la app guarda el `usuarioId` devuelto
 * por login/registro y lo manda en las llamadas que lo necesitan.
 */
interface ApiService {

    @POST("auth/registro")
    suspend fun registro(@Body request: RegistroRequest): Response<UsuarioDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioDto>

    @GET("productos")
    suspend fun obtenerProductos(): Response<List<ProductoDto>>

    @GET("productos/{id}")
    suspend fun obtenerProducto(@Path("id") id: Int): Response<ProductoDetalleDto>

    @GET("catalogo/proteinas")
    suspend fun obtenerProteinas(): Response<List<ProteinaDto>>

    @GET("catalogo/extras")
    suspend fun obtenerExtras(): Response<List<ExtraDto>>

    @POST("pedidos")
    suspend fun crearPedido(@Body request: CrearPedidoRequest): Response<CrearPedidoResponse>

    @GET("pedidos")
    suspend fun obtenerHistorial(@Query("usuarioId") usuarioId: Int): Response<List<PedidoResumenDto>>

    @GET("pedidos/{id}")
    suspend fun obtenerPedido(@Path("id") id: Int): Response<PedidoDetalleDto>
}
