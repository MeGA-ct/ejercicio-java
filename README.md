# Proyecto Tarea de evaluación Técnica en Java

## Objetivo:
- Gestión en backend de recursos de una biblioteca.

## Descipción:
- Se ha creado una API REST para gestionar los recursos de la biblioteca haciendo uso de Spring-boot, Maven, Lombok y MapStruct.
- La API puede controlar usuarios, libros y los prestamos de libros a usuarios.

## Instalación y ejecución:
- Descargar el respositorio y o comprimido del mismo.
    - Clic en <>Code y luego clic en "Dowload ZIP"
    - O puede ejecutar en consola:  
        ``` shell
        $ git clone https://github.com/MeGA-ct/ejercicio-java.git
        ```
- Para ejecutar, en la ruta principal del repositorio, asegurándose de que maven está instalado correctamente, se puede escribir el comando indicado a continuación:
  - ``` shell
    $ mvm spring-boot:run 
    ```
    lo que producirá una salida parecída a esta:
    ```
    [INFO] Scanning for projects...
    [INFO]
    [INFO] ---------------------< com.example:ejercicio-java >---------------------
    [INFO] Building ejercicio-java 0.0.1-SNAPSHOT
    [INFO] --------------------------------[ jar ]---------------------------------
        ...
    
        ...
    INFO 4242 --- [ejercicio-java] [           main] c.e.e.EjercicioJavaApplication           : Started EjercicioJavaApplication in 6.896 seconds (process running for 7.433)
    ```
    Con esa última línea apareciendo en la consola, se puede considerar que el backend ha arrancado.

## Detalles técnicos:
### Endpoint Libros:
| Path           | Method | Res Status | Res Body     | Req Param | Req Body   | Comments                                                             |
|----------------|--------|------------|--------------|-----------|------------|----------------------------------------------------------------------|
| /libros        | GET    | 200        | List [libro] | -         | -          | Lista de todos los libros                                            |
| /libros/{id}   | GET    | 200        | libro        | {id}      | -          | Los detalles de un libro específico por su ID                        |
| /libros        | POST   | 201        | libro        | -         | libro-body | Crear un nuevo libro                                                 |
| /libros/{id}   | PUT    | 200        | libro        | {id}      | libro-body | Actualizar los detalles de un libro existente por su ID              |
| /libros/{id}   | PATCH  | 200        | libro        | {id}      | libro-body | Actualizar parcialmente los detalles de un libro existente por su ID |
| /libros/{id}   | DEL    | 204        | -            | {id}      | -          | Eliminar un libro por su ID                                          |

### Endpoint Usuarios:
| Path            | Method | Res Status | Res Body       | Req Param | Req Body     | Comments                                                               |
|-----------------|--------|------------|----------------|-----------|--------------|------------------------------------------------------------------------|
| /usuarios       | GET    | 200        | List [usuario] | -         | -            | Lista de todos los usuarios                                            |
| /usuarios/{id}  | GET    | 200        | usuario        | {id}      | -            | Los detalles de un usuario específico por su ID                        |
| /usuarios       | POST   | 201        | usuario        | -         | usuario-body | Crear un nuevo usuario                                                 |
| /usuarios/{id}  | PUT    | 200        | usuario        | {id}      | usuario-body | Actualizar los detalles de un usuario existente por su ID              |
| /usuarios/{id}  | PATCH  | 200        | usuario        | {id}      | usuario-body | Actualizar parcialmente los detalles de un usuario existente por su ID |
| /usuarios/{id}  | DEL    | 204        | -              | {id}      | -            | Eliminar un usuario por su ID                                          |

### Endpoint Prestamos:
| Path              | Method | Res Status | Res Body        | Req Param | Req Body      | Comments                                                                |
|-------------------|--------|------------|-----------------|-----------|---------------|-------------------------------------------------------------------------|
| /prestamos        | GET    | 200        | List [prestamo] | -         | -             | Lista de todos los prestamos                                            |
| /prestamos/{id}   | GET    | 200        | prestamo        | {id}      | -             | Los detalles de un prestamo específico por su ID                        |
| /prestamos        | POST   | 201        | prestamo        | -         | prestamo-body | Crear un nuevo prestamo                                                 |
| /prestamos/{id}   | PUT    | 200        | prestamo        | {id}      | prestamo-body | Actualizar los detalles de un prestamo existente por su ID              |
| /prestamos/{id}   | PATCH  | 200        | prestamo        | {id}      | prestamo-body | Actualizar parcialmente los detalles de un prestamo existente por su ID |
| /prestamos/{id}   | DEL    | 204        | -               | {id}      | -             | Eliminar un prestamo por su ID                                          |

### Entidades:
#### Libro:
| columna          | Tipo        | Comentario                      |
|------------------|-------------|---------------------------------|
| id               | `Long`      | Identificador único del libro.  |
| titulo           | `String`    | Título del libro.               |
| autor            | `String`    | Autor del libro.                |
| isbn             | `String`    | Código ISBN del libro           |
| fechaPublicacion | `LocalDate` | Fecha de publicación del libro. |

#### Usuario:
| columna       | Tipo        | Comentario                       |
|---------------|-------------|----------------------------------|
| id            | `Long`      | Identificador único del usuario. |
| nombre        | `String`    | Nombre del usuario.              |
| email         | `String`    | Correo electrónico del usuario.  |
| telefono      | `String`    | Número de teléfono del usuario.  |
| fechaRegistro | `LocalDate` | Fecha de registro del usuario.   |

#### Préstamo:
| columna         | Tipo        | Comentario                              |
|-----------------|-------------|-----------------------------------------|
| id              | `Long`      | Identificador único del préstamo.       |
| libro           | `Libro`     | Libro prestado.                         |
| usuario         | `Usuario`   | Usuario que realiza el préstamo.        |
| fechaPrestamo   | `LocalDate` | Fecha en que se realizó el préstamo.    |
| fechaDevolucion | `LocalDate` | Fecha en que se debe devolver el libro. |

### Partes del request:
#### Entidad Libro-body:
Igual que la entidad Libro pero sin el ID.
#### Entidad Usuario-body:
Igual que la entidad Usuario pero sin el ID.
#### Entidad Préstamo-body:
Igual que la entidad Préstamo pero sin el ID.