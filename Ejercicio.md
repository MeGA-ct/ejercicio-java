# Ejercicio de Evaluación Técnica en Java
Objetivo: Diseñar y desarrollar un backend para una aplicación de gestión de bibliotecas utilizando Java y Spring Boot que cumpla con los siguientes requisitos.
## Contexto del Proyecto:
La Biblioteca Municipal de Yuncos ha decidido modernizar su sistema de gestión de libros y usuarios. Para ello, se requiere el desarrollo de una API REST que permita realizar operaciones básicas sobre los recursos de la biblioteca. La aplicación debe permitir gestionar libros, usuarios y préstamos de libros.
## Requisitos del Proyecto:
### 1. API REST:
- Implementar un API REST que soporte los verbos HTTP: :blue_book:GET, :green_book:POST, :ledger:PUT, :notebook:PATCH y :x:DELETE para gestionar libros, usuarios y préstamos.
#### Endpoints a implementar:
##### Entidad: Libro
- :heavy_check_mark::blue_book:GET /libros: Obtener la lista de todos los libros.
- :heavy_check_mark::blue_book:GET /libros/{id}: Obtener los detalles de un libro específico por su ID.
- :heavy_check_mark::green_book:POST /libros: Crear un nuevo libro.
- :heavy_check_mark::ledger:PUT /libros/{id}: Actualizar los detalles de un libro existente por su ID.
- :heavy_check_mark::notebook:PATCH /libros/{id}: Actualizar parcialmente los detalles de un libro existente por su ID.
- :heavy_check_mark::x:DELETE /libros/{id}: Eliminar un libro por su ID.
##### Entidad: Usuario
- :heavy_check_mark::blue_book:GET /usuarios: Obtener la lista de todos los usuarios.
- :heavy_check_mark::blue_book:GET /usuarios/{id}: Obtener los detalles de un usuario específico por su ID.
- :heavy_check_mark::green_book:POST /usuarios: Crear un nuevo usuario.
- :heavy_check_mark::ledger:PUT /usuarios/{id}: Actualizar los detalles de un usuario existente por su ID.
- :heavy_check_mark::notebook:PATCH /usuarios/{id}: Actualizar parcialmente los detalles de un usuario existente por su ID.
- :heavy_check_mark::x:DELETE /usuarios/{id}: Eliminar un usuario por su ID.
##### Entidad: Préstamo
- :blue_book:GET /prestamos: Obtener la lista de todos los préstamos.
- :blue_book:GET /prestamos/{id}: Obtener los detalles de un préstamo específico por su ID.
- :green_book:POST /prestamos: Crear un nuevo préstamo.
- :ledger:PUT /prestamos/{id}: Actualizar los detalles de un préstamo existente por su ID.
- :notebook:PATCH /prestamos/{id}: Actualizar parcialmente los detalles de un préstamo existente por su ID.
- :x:DELETE /prestamos/{id}: Eliminar un préstamo por su ID.
### 2. Capa de Servicio y Repositorios JPA:
- Crear una capa de servicio que gestione la lógica de negocio.
- Utilizar repositorios JPA para la interacción con la base de datos.
### 3. Modelo de Datos:
- Diseñar un modelo de datos que incluya al menos 3 entidades relacionadas: Libro, Usuario y Préstamo.
#### Libro::heavy_check_mark:
|columna          |Tipo        |Comentario                     |
|-----------------|------------|-------------------------------|
|id               |`Long`      |Identificador único del libro. |
|titulo           |`String`    |Título del libro.              |
|autor            |`String`    |Autor del libro.               |
|isbn             |`String`    |Código ISBN del libro          |
|fechaPublicacion |`LocalDate` |Fecha de publicación del libro.|
#### Usuario::heavy_check_mark:
| columna         |Tipo        |Comentario                       |
|-----------------|------------|---------------------------------|
| id              |`Long`      |Identificador único del usuario. |
| nombre          |`String`    |Nombre del usuario.              |
| email           |`String`    |Correo electrónico del usuario.  |
| telefono        |`String`    |Número de teléfono del usuario.  |
| fechaRegistro   |`LocalDate` |Fecha de registro del usuario.   |
#### Préstamo: 
|columna          |Tipo        |Comentario                             |
|-----------------|------------|---------------------------------------|
|id               |`Long`      |Identificador único del préstamo.      |
|libro            |`Libro`     |Libro prestado.                        |
|usuario          |`Usuario`   |Usuario que realiza el préstamo.       |
|fechaPrestamo    |`LocalDate` |Fecha en que se realizó el préstamo.   |
|fechaDevolucion  |`LocalDate` |Fecha en que se debe devolver el libro.|
### 4. Módulos de Spring Boot:
- Utilizar los módulos de Spring Boot, Spring Data JPA y Spring Web.
### 5. Base de Datos en Memoria:
- Configurar una base de datos en memoria H2 para el almacenamiento de datos.
### 6. Construcción del Proyecto:
- Utilizar Maven para la construcción y gestión de dependencias del proyecto.
### 7. Logger:
- Implementar un logger para mostrar mensajes de log en diferentes niveles (INFO, DEBUG, ERROR).
### 8. Gestión de Excepciones:
- Incluir una gestión personalizada de excepciones para manejar errores de manera adecuada.
### 9. Tests Unitarios:
- Escribir tests unitarios utilizando JUnit 5. Opcionalmente, se puede utilizar Mockito para la creación de mocks.
- Testeo Completo de la Capa de Servicio:
- Crear tests unitarios para cada método de la capa de servicio.
- Asegurarse de cubrir todos los casos de uso, incluyendo casos positivos y negativos.
- Utilizar Mockito para simular las dependencias de la capa de servicio, como los repositorios JPA.
- Verificar que los métodos de la capa de servicio interactúan correctamente con los repositorios y manejan las excepciones adecuadamente.
- Incluir tests para validar la lógica de negocio, como la creación, actualización y eliminación de entidades, así como la gestión de préstamos.
### 10. Librerías Opcionales:
- Se puede hacer uso opcional de librerías como Lombok, MapStruct u otras que se consideren útiles para el desarrollo del proyecto.
## Entrega:
- Subir el código del proyecto a un repositorio en GitHub o GitLab.
- Incluir un archivo README.md con instrucciones claras sobre cómo ejecutar el proyecto y una breve descripción de su funcionamiento.
