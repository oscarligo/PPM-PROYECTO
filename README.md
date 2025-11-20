# Nombre pendiente
## Integrantes: 
*24880 Oscar Rompich
*24952 Marcela Castillo
*24376 José Rivera

## Descripción

El principal objetivo de esta aplicación es facilitar y automatizar el proceso de toma de asistencia durante las clases.
Para ello, se planea el uso de sensores físicos que detectan la presencia de los estudiantes y se comunican con la aplicación móvil.
La app permitirá a los docentes registrar la asistencia de forma rápida y precisa, evitando errores manuales, mientras que los estudiantes podrán visualizar su historial de asistencias y notificaciones.


Avances para la revisión de la entrega 4: 

https://youtu.be/0IVfnxlzv3M



## Estructura del Proyecto

```
app/src/main/java/com/example/ppm_proyecto
│
├─ core/                          // Utilidades para todo el proyecto
│  ├─ di/                         // Configuración de inyección de dependencias con Hilt
│  ├─ nfc/                        // Utildades para comunicación con tags NFC
│  └─ util/                       // Un resultado genérico, para manejo de errores
│
├─ data/                          // Capa de datos
│  ├─ local/                      // Room, caché local
│  ├─ remote/                     // Firebase y Firestore
│
├─ domain/                        // Lógica de negocio
|  ├─ di/                         // Módulos de inyección de dependencias por módulo
│  ├─ model/                      // Entidades core (Student, Professor, Tag, AttendanceRecord, Session)
│  ├─ repository/                 // Interfaces de repositorios (AttendanceRepository, SecurityRepository, NfcRepository)
│  └─ usecase/                    // Casos de uso organizados por módulo (attendance, security, nfc)
│
└─ presentation/                  // Interfaz de usuario (Jetpack Compose + MVI)
   ├─ components/                 // Componentes UI reutilizables (botones, cards, barras)
   ├─ navigation/                 // BackStack de navegación y definición de rutas
   ├─ theme/                      // Sistema de diseño (colores, tipografías, temas)
   └─ ui/                         // Pantallas por funcionalidad (attendance, security, nfc)
                                  // Cada pantalla con Screen, State, Intent, ViewModel
   ```
