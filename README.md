# Nombre pendiente

## Descripción

El principal objetivo de esta aplicación es facilitar y automatizar el proceso de toma de asistencia durante las clases.
Para ello, se planea el uso de sensores físicos que detectan la presencia de los estudiantes y se comunican con la aplicación móvil.
La app permitirá a los docentes registrar la asistencia de forma rápida y precisa, evitando errores manuales, mientras que los estudiantes podrán visualizar su historial de asistencias y notificaciones.




## Estructura del Proyecto

```
app/src/main/java/com/example/ppm_proyecto
│
├─ data/            # Capa de datos, obtiene y guarda información desde distintas fuentes
│  ├─ local/        # Almacenamiento local.
│  └─ remote/       # Comunicación con APIs o servicios externos.
├─ domain/          # Lógica de la aplicación 
│  ├─ model/        # Modelos principales del la aplicación
│  ├─ repository/   # Interfaces que definen cómo acceder a los datos
│  └─ usecase/      # Casos de uso
└─ presentation/    # Interfaz de usuario (Jetpack Compose + patrón MVI)
   ├─ components/   # Elementos UI reutilizables (botones, barras, gráficos, etc.)
   ├─ navigation/   # Definición de rutas y grafo de navegación
   ├─ theme/        # Estilos visuales: colores, tipografías y temas
   └─ ui/           # Pantallas principales (cada módulo con su Screen, State, Intent, VM)
   ```
