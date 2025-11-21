# ClassControl
## Integrantes: 
*24880 Oscar Rompich
*24952 Marcela Castillo
*24376 José Rivera

<img width="100" height="100" alt="logo" src="https://github.com/user-attachments/assets/2b3f3075-d76a-4a25-a5a9-dc41001842ae" />

## Descripción

ClassControl es una aplicación diseñada para facilitar y automatizar la toma de asistencia en las clases.
Utiliza tags NFC para registrar la presencia de los estudiantes de forma rápida, precisa y sin intervención manual. Al acercar el dispositivo al tag, la información se sincroniza de inmediato con la aplicación móvil.

La plataforma permite a los docentes gestionar la asistencia de sus cursos de manera eficiente y sin errores, mientras que los estudiantes pueden consultar su historial de asistencias y mantenerse al día con su desempeño.


Avances para la revisión de la entrega 4: 

https://youtu.be/0IVfnxlzv3M


!!!!VIDEO DE DEMOSTRACIÓN FINAL PENDIENTE!!!!



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
