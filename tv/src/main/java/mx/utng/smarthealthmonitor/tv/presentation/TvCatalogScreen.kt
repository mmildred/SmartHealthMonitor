package mx.utng.smarthealthmonitor.tv.presentation

// ============================================
// IMPORTS - Bibliotecas necesarias
// ============================================

// Layouts y contenedores básicos de Compose
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

// Formas para elementos UI
import androidx.compose.foundation.shape.RoundedCornerShape

// Componentes Material Design
import androidx.compose.material3.CircularProgressIndicator

// Anotaciones y funciones principales de Compose
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

// Alineación y modificadores de UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Colores y estilos de texto
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

// Unidades de medida
import androidx.compose.ui.unit.dp

// Lifecycle - Para observar el estado del ViewModel respetando el ciclo de vida
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Lazy layouts - Para listas y filas con scroll eficiente
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

// Componentes específicos para TV de Material Design 3
import androidx.tv.material3.*

// ============================================
// PANTALLA PRINCIPAL DEL CATÁLOGO PARA TV
// ============================================

/**
 * Pantalla de catálogo para TV con sistema de filtrado interactivo.
 * Esta pantalla muestra las lecturas de frecuencia cardíaca (FC) en un formato
 * optimizado para dispositivos TV con navegación por control remoto.
 *
 * @param viewModel ViewModel que contiene la lógica de negocio y el estado
 * @param onCardClick Callback que se ejecuta cuando el usuario selecciona una tarjeta,
 *                    recibe el ID de la lectura seleccionada como parámetro
 */
@OptIn(ExperimentalTvMaterial3Api::class) // Permite usar APIs experimentales de TV Material 3
@Composable // Indica que esta función genera UI en Compose
fun TvCatalogScreen(
    viewModel: TvViewModel,
    onCardClick: (Int) -> Unit
) {
    // ============================================
    // OBSERVACIÓN DEL ESTADO
    // ============================================

    // collectAsStateWithLifecycle() observa el estado del ViewModel y actualiza la UI
    // automáticamente cuando cambia, respetando el ciclo de vida del componente
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Obtiene el filtro actualmente seleccionado (TODOS, NORMAL, ALERTA)
    val filtroActual = viewModel.filtroSeleccionado

    // Obtiene la lista de lecturas ya filtradas según el filtro activo
    // Esta lista se actualiza automáticamente cuando cambia el filtro
    val lecturasFiltradas = viewModel.lecturasFiltradas

    // ============================================
    // CONTENEDOR PRINCIPAL
    // ============================================

    // Box es un contenedor que permite apilar elementos y posicionarlos
    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible en la pantalla
            .background(Color(0xFF0D1B4A)) // Fondo azul oscuro (color corporativo)
    ) {
        // ============================================
        // INDICADOR DE CARGA
        // ============================================

        // Muestra un spinner circular mientras se cargan los datos iniciales
        // Solo aparece si está cargando Y no hay lecturas aún (evita parpadeos)
        if (state.isLoading && state.lecturas.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center), // Centrado en la pantalla
                color = MaterialTheme.colorScheme.primary // Color primario del tema
            )
        } else {
            // ============================================
            // LAYOUT PRINCIPAL CON SCROLL VERTICAL
            // ============================================

            // LazyColumn: Scroll vertical eficiente que solo renderiza los elementos visibles
            // Ideal para listas largas porque mejora el rendimiento
            LazyColumn(
                modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio
                contentPadding = PaddingValues(48.dp), // Márgenes internos de 48dp (ideal para TV)
                verticalArrangement = Arrangement.spacedBy(32.dp) // Espacio de 32dp entre secciones
            ) {
                // ============================================
                // SECCIÓN 1: FILTROS (Chips interactivos)
                // ============================================

                // "item" define un elemento individual en la LazyColumn
                item {
                    // Texto de título de la sección de filtros
                    Text(
                        text = "Filtrar por categoría",
                        style = MaterialTheme.typography.labelLarge, // Estilo de texto grande
                        color = Color.White.copy(alpha = 0.6f), // Blanco semi-transparente
                        modifier = Modifier.padding(bottom = 12.dp) // Espacio inferior
                    )

                    // LazyRow: Scroll horizontal para los chips de filtro
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp), // Espacio entre chips
                        contentPadding = PaddingValues(bottom = 16.dp) // Espacio inferior
                    ) {
                        // ============================================
                        // CHIP "TODAS"
                        // ============================================
                        item {
                            FiltroChipItem(
                                label = "Todas", // Texto del chip
                                selected = filtroActual == FiltroFc.TODOS, // ¿Está seleccionado?
                                onClick = { viewModel.cambiarFiltro(FiltroFc.TODOS) } // Acción al hacer clic
                            )
                        }

                        // ============================================
                        // CHIP "NORMALES"
                        // ============================================
                        item {
                            FiltroChipItem(
                                label = "Normales",
                                selected = filtroActual == FiltroFc.NORMAL,
                                onClick = { viewModel.cambiarFiltro(FiltroFc.NORMAL) }
                            )
                        }

                        // ============================================
                        // CHIP "ALERTAS"
                        // ============================================
                        item {
                            FiltroChipItem(
                                label = "Alertas",
                                selected = filtroActual == FiltroFc.ALERTA,
                                onClick = { viewModel.cambiarFiltro(FiltroFc.ALERTA) }
                            )
                        }
                    }
                }

                // ============================================
                // SECCIÓN 2: ESTADO ACTUAL (Últimas 3 lecturas)
                // ============================================

                item {
                    // RowSection: Componente reutilizable que muestra título + contenido
                    // Muestra la FC actual en el título en tiempo real
                    RowSection(title = "⚡ Estado Actual — ${state.fcActual} bpm") {
                        // LazyRow para las tarjetas horizontales
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre tarjetas
                        ) {
                            // takeLast(3): Toma solo las 3 lecturas más recientes
                            // Esto asegura que siempre se muestren los datos más actuales
                            items(state.lecturas.takeLast(3)) { lectura ->
                                // FcCardItem: Tarjeta que muestra la información de la lectura
                                // onClick: Propaga el evento hacia arriba con el ID de la lectura
                                FcCardItem(
                                    lectura = lectura,
                                    onClick = { onCardClick(lectura.id) }
                                )
                            }
                        }
                    }
                }

                // ============================================
                // SECCIÓN 3: HISTORIAL FILTRADO
                // ============================================

                item {
                    // Título dinámico según el filtro seleccionado
                    // when es una expresión que devuelve un valor según la condición
                    val tituloHistorial = when(filtroActual) {
                        FiltroFc.TODOS -> "📋 Historial Completo"
                        FiltroFc.NORMAL -> "📋 Historial: Frecuencia Normal"
                        FiltroFc.ALERTA -> "📋 Historial: Alertas de FC"
                    }

                    // RowSection con título dinámico y conteo de elementos
                    // Muestra cuántos elementos hay en la categoría actual
                    RowSection(title = "$tituloHistorial (${lecturasFiltradas.size})") {
                        // ============================================
                        // CONDICIONAL: ¿Hay lecturas filtradas?
                        // ============================================

                        if (lecturasFiltradas.isEmpty()) {
                            // CASO 1: No hay lecturas - Muestra mensaje informativo
                            Text(
                                text = "No hay lecturas en esta categoría.",
                                style = MaterialTheme.typography.bodyMedium, // Estilo de texto mediano
                                color = Color.Gray, // Color gris para texto secundario
                                modifier = Modifier.padding(vertical = 16.dp) // Espaciado vertical
                            )
                        } else {
                            // CASO 2: Hay lecturas - Muestra las tarjetas
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre tarjetas
                            ) {
                                // items: Itera sobre la lista de lecturas filtradas
                                items(lecturasFiltradas) { lectura ->
                                    // Cada lectura se muestra como una tarjeta
                                    FcCardItem(
                                        lectura = lectura,
                                        onClick = { onCardClick(lectura.id) } // Propaga el evento
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================
// COMPONENTE: FILTRO CHIP ITEM
// ============================================

/**
 * Componente personalizado de Chip para filtros en TV.
 * Un chip es un elemento interactivo que permite seleccionar una categoría de filtro.
 *
 * Características:
 * - Soporte para enfoque (focus) en TV
 * - Efecto de resplandor (glow) cuando está enfocado
 * - Cambia de color según el estado (normal, enfocado, seleccionado)
 * - Forma redondeada para mejor apariencia en TV
 *
 * @param label Texto que se muestra en el chip (ej. "Todas")
 * @param selected Indica si el chip está seleccionado actualmente
 * @param onClick Callback que se ejecuta al hacer clic en el chip
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FiltroChipItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Surface: Componente base de Material Design que actúa como contenedor interactivo
    // Específico para TV con soporte para navegación por control remoto
    Surface(
        selected = selected, // Estado de selección (afecta el color y estilo)
        onClick = onClick, // Acción al hacer clic
        modifier = Modifier.height(40.dp), // Altura fija para todos los chips
        // ============================================
        // CONFIGURACIÓN DE COLORES SEGÚN EL ESTADO
        // ============================================
        colors = SelectableSurfaceDefaults.colors(
            // Estado NORMAL: Cuando no está seleccionado ni enfocado
            containerColor = Color(0xFF1E2C5B), // Azul oscuro

            // Estado ENFOCADO: Cuando el control remoto está sobre el chip
            focusedContainerColor = Color(0xFF3D4F85), // Azul más claro

            // Estado SELECCIONADO: Cuando el filtro está activo
            selectedContainerColor = Color(0xFF4A90E2), // Azul brillante

            // Estado SELECCIONADO + ENFOCADO: Cuando está activo y enfocado
            focusedSelectedContainerColor = Color(0xFF64B5F6) // Azul más brillante aún
        ),
        // ============================================
        // FORMA DEL CHIP
        // ============================================
        shape = SelectableSurfaceDefaults.shape(RoundedCornerShape(20.dp)), // Bordes redondeados

        // ============================================
        // EFECTO DE RESPLANDOR (GLOW) PARA TV
        // ============================================
        // El glow es importante en TV porque ayuda al usuario a saber qué elemento está enfocado
        glow = SelectableSurfaceDefaults.glow(
            focusedGlow = Glow(
                elevation = 8.dp, // Altura del resplandor
                elevationColor = Color.Cyan.copy(alpha = 0.2f) // Color cian semi-transparente
            )
        )
    ) {
        // ============================================
        // CONTENIDO DEL CHIP
        // ============================================
        Box(
            contentAlignment = Alignment.Center, // Centra el texto horizontal y verticalmente
            modifier = Modifier.padding(horizontal = 20.dp) // Espaciado interno horizontal
        ) {
            // Texto del chip
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge, // Estilo de texto grande
                // ============================================
                // COLOR DEL TEXTO SEGÚN EL ESTADO
                // ============================================
                color = if (selected) {
                    Color.White // Blanco cuando está seleccionado
                } else {
                    Color.White.copy(alpha = 0.8f) // Blanco semi-transparente cuando no
                },
                // ============================================
                // PESO DE LA FUENTE SEGÚN EL ESTADO
                // ============================================
                fontWeight = if (selected) {
                    FontWeight.Bold // Negrita cuando está seleccionado (más énfasis)
                } else {
                    FontWeight.Normal // Normal cuando no
                }
            )
        }
    }
}

// ============================================
// COMPONENTE: ROW SECTION
// ============================================

/**
 * Componente reutilizable para crear secciones con título y contenido.
 * Proporciona una estructura consistente para todas las secciones de la pantalla.
 *
 * Características:
 * - Título descriptivo con estilo de encabezado
 * - Espaciado consistente entre título y contenido
 * - Contenido flexible que puede ser cualquier elemento Composable
 *
 * @param title Título de la sección (puede incluir emojis e información dinámica)
 * @param content Lambda que contiene el contenido de la sección (tarjetas, mensajes, etc.)
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RowSection(
    title: String,
    content: @Composable () -> Unit
) {
    // Column: Organiza los elementos verticalmente (título arriba, contenido abajo)
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio de 16dp entre título y contenido
    ) {
        // ============================================
        // TÍTULO DE LA SECCIÓN
        // ============================================
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall, // Estilo de encabezado pequeño
            color = Color.White, // Blanco para máximo contraste sobre fondo oscuro
            fontWeight = FontWeight.Bold // Negrita para destacar
        )

        // ============================================
        // CONTENIDO DE LA SECCIÓN
        // ============================================
        // Aquí se inyecta el contenido proporcionado (tarjetas, mensajes, etc.)
        content()
    }
}

// ============================================
// NOTAS ADICIONALES SOBRE EL DISEÑO PARA TV
// ============================================

/*
 * 1. TAMAÑOS Y PADDINGS:
 *    - Los paddings son grandes (48dp, 32dp) porque las pantallas de TV son grandes
 *    - Esto mejora la legibilidad desde la distancia
 *
 * 2. NAVEGACIÓN POR CONTROL REMOTO:
 *    - Los elementos tienen soporte para "focus" (Surface con glow)
 *    - El resplandor (glow) ayuda a saber qué elemento está seleccionado
 *
 * 3. SCROLL EFICIENTE:
 *    - Se usan LazyColumn y LazyRow para scroll eficiente
 *    - Solo renderizan los elementos visibles, mejorando el rendimiento
 *
 * 4. ESTADOS VISUALES CLAROS:
 *    - Los chips cambian de color al estar seleccionados o enfocados
 *    - El texto se pone en negrita cuando está seleccionado
 *    - Feedback visual inmediato para el usuario
 *
 * 5. ESTRUCTURA DE DATOS:
 *    - El estado se observa con collectAsStateWithLifecycle()
 *    - Los datos se filtran en el ViewModel (separación de responsabilidades)
 *    - La UI solo se preocupa por mostrar los datos, no por procesarlos
 *
 * 6. EVENTOS HACIA ARRIBA:
 *    - Los clics en tarjetas se propagan mediante onCardClick
 *    - Los cambios de filtro se manejan en el ViewModel
 *    - Patrón de comunicación claro (UI → ViewModel → UI)
 */