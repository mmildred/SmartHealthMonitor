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

---

## 👩‍💻 Autor
**Mildred Banda**  
*Ingeniería en Desarrollo y Gestión de Software*  
**Universidad Tecnológica del Norte de Guanajuato (UTNG)**

---

## Arquitectura — SmartHealth Monitor
```text
Sensor PPG (Wear OS)
│  Health Services API
▼
PassiveListenerService (wear)
│  MessageClient (BLE)
▼
WearListenerService (app)
│  SmartHealthRepository
▼
StateFlow (fcActual)  ──────────────────────────────────┐
│                                                        │
▼                                                        ▼
DashboardViewModel (app)              TvViewModel (tv)
│  collectAsState()                    │  collectAsState()
▼                                        ▼
DashboardScreen (Compose)          TvCatalogScreen (Compose TV)
└── CastButton ──► Chromecast (Remote Playback)

Room DB (LecturaFC)  ◄──  Repository  ──►  Flow<List>
│
┌─────────────────────┴──────────┐
▼                                ▼
HistorialScreen (app)        TvCatalogScreen (tv)
```

