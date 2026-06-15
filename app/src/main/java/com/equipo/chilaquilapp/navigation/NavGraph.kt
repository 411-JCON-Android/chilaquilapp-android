package com.equipo.chilaquilapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.equipo.chilaquilapp.ui.confirmado.ConfirmadoScreen
import com.equipo.chilaquilapp.ui.confirmar.ConfirmarScreen
import com.equipo.chilaquilapp.ui.detalle.DetalleScreen
import com.equipo.chilaquilapp.ui.detallepedido.DetallePedidoScreen
import com.equipo.chilaquilapp.ui.historial.HistorialScreen
import com.equipo.chilaquilapp.ui.login.LoginScreen
import com.equipo.chilaquilapp.ui.menu.MenuScreen
import com.equipo.chilaquilapp.ui.registro.RegistroScreen

/**
 * Grafo de navegación de la app. Cada pantalla nueva agrega su ruta aquí;
 * el contenido de cada pantalla vive en su propio paquete `ui.<pantalla>`.
 */
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Login.route) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Routes.Menu.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Routes.Registro.route) }
            )
        }
        composable(Routes.Registro.route) {
            RegistroScreen(
                onRegistroExitoso = { navController.popBackStack() },
                onVolverClick = { navController.popBackStack() }
            )
        }
        composable(Routes.Menu.route) {
            MenuScreen(
                onProductoClick = { productoId ->
                    navController.navigate(Routes.Detalle.createRoute(productoId))
                },
                onVerHistorial = { navController.navigate(Routes.Historial.route) }
            )
        }
        composable(
            route = Routes.Detalle.route,
            arguments = listOf(navArgument(Routes.Detalle.ARG_PRODUCTO_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString(Routes.Detalle.ARG_PRODUCTO_ID).orEmpty()
            DetalleScreen(
                productoId = productoId,
                onConfirmar = { navController.navigate(Routes.Confirmar.route) },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Routes.Confirmar.route) {
            ConfirmarScreen(
                onConfirmar = {
                    navController.navigate(Routes.Confirmado.route) {
                        popUpTo(Routes.Menu.route)
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Routes.Confirmado.route) {
            ConfirmadoScreen(
                onVerHistorial = {
                    navController.navigate(Routes.Historial.route) {
                        popUpTo(Routes.Menu.route)
                    }
                },
                onVolverAlMenu = {
                    navController.navigate(Routes.Menu.route) {
                        popUpTo(Routes.Menu.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Historial.route) {
            HistorialScreen(
                onPedidoClick = { pedidoId ->
                    navController.navigate(Routes.DetallePedido.createRoute(pedidoId))
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DetallePedido.route,
            arguments = listOf(navArgument(Routes.DetallePedido.ARG_PEDIDO_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString(Routes.DetallePedido.ARG_PEDIDO_ID).orEmpty()
            DetallePedidoScreen(
                pedidoId = pedidoId,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
