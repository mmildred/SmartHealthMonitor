# SmartHealth Monitor

![Android CI](https://img.shields.io/badge/Android%20CI-API%2026%2B-green?style=for-the-badge&logo=android)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-MD3-blue?style=for-the-badge&logo=jetpackcompose)

Aplicación Android de monitoreo de salud personal en tiempo real desarrollada como Proyecto Integrador para la UTNG (9° Cuatrimestre).

## 🚀 Stack Tecnológico

| Componente | Descripción |
| :--- | :--- |
| **Kotlin + Jetpack Compose** | UI declarativa moderna con Material Design 3. |
| **Wearable Data Layer API** | Sincronización de datos entre reloj y teléfono vía BLE. |
| **Health Services API** | Acceso a sensores de frecuencia cardíaca en segundo plano (Wear OS). |
| **Room Database** | Persistencia de datos local con soporte para flujos reactivos (Flow). |
| **Jetpack Navigation** | Gestión de rutas y navegación entre pantallas mediante NavHost. |
| **GitHub + Conventional Commits** | Control de versiones bajo estándares de mensajes profesionales. |

## 📱 Pantallas del Sistema

| Pantalla | Descripción |
| :--- | :--- |
| **LoginScreen** | Interfaz de acceso con validaciones de campos y manejo de estados de carga. |
| **DashboardScreen** | Visualización principal con frecuencia cardíaca y pasos sincronizados en tiempo real. |
| **HistorialScreen** | Listado de mediciones pasadas recuperadas de Room mediante Flow reactivo. |
| **AlertaScreen** | Sistema de emergencia basado en AlertDialog MD3 con retroalimentación vía Snackbar. |

## 📸 Capturas de Pantalla

| Login | Dashboard |
| :---: | :---: |
| ![Login](screenshots/login.png) | ![Dashboard](screenshots/dashboard.png) |

| Historial | Alerta de Emergencia |
| :---: | :---: |
| ![Historial](screenshots/historial.png) | ![Alerta](screenshots/alerta.png) |

## ⌚ Unidad II — Wear OS

| Pantalla | Descripción |
| :--- | :--- |
| **WearDashboardScreen** | Visualización de FC en tiempo real optimizada para pantallas circulares con `ScalingLazyColumn` y `TimeText`. |
| **WearHistorialScreen** | Listado de mediciones con soporte para **Rotary Input**, permitiendo el desplazamiento mediante la corona física del reloj. |
| **WearAlertaScreen** | Interfaz de confirmación de emergencia con botones circulares de alto contraste. |
| **SmartHealth WatchFace** | Carátula nativa digital que muestra la hora y la frecuencia cardíaca actual directamente en la esfera. |

### 📸 Capturas Wear OS
| WatchFace | Wear Dashboard |
| :---: | :---: |
| ![WatchFace](screenshots/watchface.png) | ![WearDashboard](screenshots/wear_dashboard.png) |

## 📺 Unidad III — Android TV

| Pantalla | Descripción |
| :--- | :--- |
| **TvDashboardScreen** | Vista panorámica de monitoreo con carrusel de mediciones reciente y visualización de FC actual en alta visibilidad. |
| **FcCardItem** | Componente de tarjeta optimizado para navegación con control remoto (D-pad) y estados visuales de foco. |

**Stack TV:** `Compose for TV + Material 3 + D-pad Navigation`

### 📸 Capturas Android TV
![TvDashboard](screenshots/tv_dashboard.png)

---

## 👩‍💻 Autor
**Mildred Banda**  
*Ingeniería en Desarrollo y Gestión de Software*  
**Universidad Tecnológica del Norte de Guanajuato (UTNG)**
