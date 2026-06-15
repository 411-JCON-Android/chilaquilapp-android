# ChilaquilApp 🌶️

Aplicación móvil Android para pedir chilaquiles a domicilio de un negocio de
barrio. El cliente crea su cuenta, ve el menú, personaliza su platillo
(proteínas y extras), define la dirección de entrega y envía el pedido; luego
puede consultar su historial y repetir un pedido con un toque.

> Proyecto escolar. Este repositorio es la **app Android**; el backend
> (Node.js + Supabase) vive en un repositorio aparte (`chilaquilapp-api`).

---

## Stack

| Capa | Tecnología |
|------|-----------|
| Lenguaje / UI | Kotlin + Jetpack Compose (Material 3) |
| Arquitectura | MVVM + StateFlow |
| Navegación | Navigation Compose |
| Inyección de dependencias | Hilt |
| Red | Retrofit + OkHttp + Gson |
| Asincronía | Coroutines |

**Toolchain:** AGP 9.2.1 · Kotlin 2.2.10 · Compose BOM 2026.02.01 ·
`compileSdk 36` · `minSdk 29` · `targetSdk 36`.

---

## Requisitos

- **Android Studio** (versión reciente, compatible con AGP 9).
- **JDK 11** o superior.
- Un emulador o dispositivo físico con **Android 10 (API 29)** o superior.

---

## Instalación y ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/411-JCON-Android/chilaquilapp-android.git
   ```
2. Abre la carpeta del proyecto en **Android Studio** y espera a que termine la
   sincronización de Gradle (la primera vez descarga dependencias).
3. Selecciona un emulador o conecta un dispositivo.
4. Pulsa **Run ▶️** (o `Shift + F10`).

Desde la terminal también puedes generar el APK de depuración:
```bash
./gradlew :app:assembleDebug
```

---

## Configuración de la API

La URL base de la API se define en
[`NetworkModule.kt`](app/src/main/java/com/equipo/chilaquilapp/di/NetworkModule.kt):

```kotlin
private const val BASE_URL = "https://chilaquil-api.vercel.app/api/"
```

La autenticación es **simple, sin token**: login y registro devuelven los datos
del usuario (incluido su `id`). La app guarda ese `usuarioId` en memoria
([`SessionManager`](app/src/main/java/com/equipo/chilaquilapp/data/session/SessionManager.kt))
y lo envía en las llamadas que lo requieren. Todo el JSON usa **camelCase**.

---

## Estructura del proyecto

Organización *feature-based* con MVVM, bajo el paquete raíz
`com.equipo.chilaquilapp`:

```
com.equipo.chilaquilapp
├── data
│   ├── remote
│   │   ├── ApiService.kt        → interfaz Retrofit con todos los endpoints
│   │   └── dto/                 → DTOs de la API (+ mapeadores toDomain())
│   ├── repository/              → AuthRepository, ProductoRepository, PedidoRepository
│   └── session/                 → SessionManager (usuario con sesión activa)
├── domain
│   └── model/                   → Usuario, Producto, Pedido, Proteina, Extra
├── di
│   └── NetworkModule.kt         → provee OkHttp, Retrofit y ApiService (Hilt)
├── ui
│   ├── theme/                   → Color.kt, Type.kt, Theme.kt (sistema de diseño)
│   ├── components/              → PillButton, ChilaquilCard, ChilaquilTextField…
│   └── <pantalla>/              → login, registro, menu, detalle, confirmar,
│                                   confirmado, historial, detallePedido
└── navigation
    └── NavGraph.kt              → grafo con las 8 rutas
```

### Rutas de navegación
`login` · `registro` · `menu` · `detalle/{productoId}` · `confirmar` ·
`confirmado` · `historial` · `detallePedido/{pedidoId}`

### Sistema de diseño
Tema **"Katania Festive Cantina"** (paleta mostaza, naranja especiado y tonos de
tierra) con `dynamicColor` desactivado para conservar la identidad de marca.
Componentes reutilizables en `ui/components`: botón píldora, tarjeta de producto
y campo de formulario, todos con `@Preview`.

---

## Estado

- ✅ Cimientos: setup, sistema de diseño, navegación y capa de red.
- ✅ Pantalla de inicio de sesión conectada a la API.
- 🚧 Resto de pantallas (menú, detalle, confirmar, historial…) en desarrollo.

---

## Equipo

| Integrante | Responsabilidad |
|------------|-----------------|
| Juan Carlos Orozco Nieto | Cimientos Android (setup, diseño, navegación, red) + login |
| Mauricio Aguilar Gómez | Backend + pantalla "Pedido confirmado" |
| Aarón Martínez Garcidueñas | Base de datos + pantalla "Crear cuenta" |
| Pablo Eduardo Magaña Gutiérrez | Pantallas menú, confirmar e historial |
| Amir Goyri Espinoza | Pantallas detalle y detalle de pedido |
