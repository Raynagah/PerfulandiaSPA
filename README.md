# 🧾 Proyecto: Transformación Digital - Perfulandia SPA


Este repositorio contiene el desarrollo técnico del sistema basado en microservicios para la empresa Perfulandia SPA.

## 📦 Descripción General del Proyecto

El sistema propuesto usa microservicios (Usuario, Producto y Carrito) para reemplazar el sistema monolítico actual.

Problema que resuelve: 
El sistema monolítico presenta problemas de rendimiento y disponibilidad a medida que la empresa crece.

Beneficios:

  - Escalabilidad: Cada microservicio puede escalar según la demanda.

  - Resiliencia: Un fallo en un microservicio no afecta a todo el sistema.

  - Mantenimiento ágil: Actualizaciones sin afectar a todo el sistema.
  
  - Mejor rendimiento: Respuestas más rápidas y eficaces.

Esto permite a Perfulandia SPA expandirse sin perder eficiencia ni calidad.

## 🧩 Arquitectura de Microservicios

Microservicios:

Microservicio de Producto:
  - Gestiona la información sobre productos (id,nombre, precio, cantidad).
  - Interactúa con su propia base de datos para almacenar la información de los productos.
  - Expone una API REST para crear, leer, actualizar y eliminar productos.

Microservicio de Usuario:
  - Maneja la gestión de usuarios (id, nombre, correo, rol).
  - Tiene su propia base de datos para almacenar los datos de los usuarios.

Microservicio de Carrito:

  - Gestiona los carritos de compra de los usuarios.
  - Permite agregar, eliminar productos en el carrito.
  - Permite consultar el contenido actual del carrito de un usuario.
  - Cada carrito está asociado a un user_id.
  - Interactúa con el microservicio de Producto para verificar la existencia, disponibilidad y precio del producto antes de añadirlo al carrito.
  - Interactúa con el microservicio de Usuario para validar que el usuario exista.
  
Base de Datos por Servicio:
  - Producto: Base de datos específica para almacenar la información de los productos.
  - Usuario: Base de datos para almacenar los perfiles, credenciales y roles de los usuarios.
  - Pedido: Base de datos para gestionar los pedidos, incluyendo detalles de los productos, cantidades, estado y usuario relacionado.
  - Carrito: Base de datos para almacenar los carritos de compra y los productos agregados por los usuarios.

### Microservicios Desarrollados

- `usuarioservice`: Este microservicio nos permite visualizar, agregar y eliminar usuarios dentro del sistema.
- `productoservice`: Este microservicio nos permite visualizar, agregar y eliminar productos, además de consultar usuarios desde el mismo microservicio.
- `carritoservice`: Este microservicio nos permite generar un carrito de compras, con el nombre del usuario, su id, id's del o los productos y el total a pagar por el mismo.

## 🛠️ Tecnologías Utilizadas

> Spring Boot

> Maven

> Github

> IA

> Postman

> Laragon

> Visual Studio Code

> Discord

> Xampp (MySQL, Apache)

> JUnit

> Mockito

> Swagger UI / OpenAPI

> HATEOAS


## 🗄️ Configuración de Bases de Datos

- Para este proyecto utilizamos como motor de base de datos MySQL. Para la realización de las conexiones entre microservicios utilizamos SpringBoot en nuestro archivo application.properties, que nos permitió definir  rutas, puertos, nombres de usuario y contraseñas para gestionar la comunicación entre microservicios correctamente. Dentro de otros aspectos que no son menos importantes dentro del archivo tenemos propiedades de Spring que nos permiten mantener todo actualizado comparando entidades y clases de Java para realizar cambios (o adiciones) automáticos de ser necesario. 
Finalmente dicho archivo contiene líneas de código que nos apoyan en la depuración del código, ayudando a que nuestro trabajo resulte más pulcro mientras progresa.
- Para el microservicio de productoservice (perfulandia_usuarios_01v): Esta BD se utiliza para almacenar productos (en este caso perfumes) con sus respectivos atributos, tales como id, nombre, precio y  stock.
- Para el microservicio de usuarioservice (perfulandia_productos_01v): Esta BD se utiliza para almacenar usuarios con sus respectivos atributos dentro del sistema, tales como id, nombre, correo y rol dentro del sistema.
- Para el microservicio de carritoservice (perfulandia_carritos_01v): Esta BD se utiliza para generar un carrito de compras mediante la comunicación con los 2 microservicios previamente mencionados. Crea un “carrito” con la id del mismo, el nombre del cliente, su id, la id del o los productos que se hayan agregado, y el total de la compra (en dólares).

## 📮 Endpoints y Pruebas

| Microservicio            | Endpoints                                   |
|--------------------------|---------------------------------------------|
| usuarioservice           | GET http://localhost:8081/api/usuarios, POST http://localhost:8081/api/usuarios, GET http://localhost:8081/api/usuarios/{id}, DELETE http://localhost:8081/api/usuarios/{id}|
| productoservice          | POST http://localhost:8082/api/productos, GET http://localhost:8082/api/productos/{id}, DELETE http://localhost:8082/api/productos/{{id}}, GET http://localhost:8082/api/productos/usuario/{{id}}                        |
| carritoservice           | GET http://localhost:8083/api/carrito/4, GET http://localhost:8083/api/carrito/4/resumen, POST http://localhost:8083/api/carrito/4/agregar/4, POST http://localhost:8083/api/carrito/4/agregar/5, DELETE http://localhost:8083/api/carrito/4/eliminar/5               |

---

- Descripción de cada Endpoint en el microservicio usuarioservice: 
  1) Método GET para el microservicio de Usuario: Se espera que este método nos entregue todos los usuarios existentes en la base de datos. En el ejemplo no hay ninguno pero la respuesta es de código 200 por lo que podemos ver que la ejecución ha sido exitosa.
  2) Método POST para el microservicio de Usuario: En este caso ingresamos un nuevo usuario mediante este método y lo que se espera es que al momento de enviar los datos en formato JSON el sistema nos entregue un código 200 y además poder visualizar al usuario que acabamos de ingresar en la parte inferior de la ventana.
  3) Método DELETE para el microservicio de Usuario: A diferencia de los 2 métodos utilizados previamente, en la barra de ingreso de url debemos incorporar la ID del usuario que queramos eliminar de nuestra base de datos. En este caso esperamos que en la parte inferior de la ventana nos entregue un código 200 y que luego corroborando con el método GET que busca por ID no veamos a dicho usuario.
- Descripción de cada Endpoint en el microservicio productoservice:
  1) Método GET para el microservicio de Producto: Método GET para el microservicio de Producto: Se espera que este método nos entregue todos los productos existentes en la base de datos. En el ejemplo hay uno, la respuesta es de código 200 y en la parte inferior de la ventana podemos ver dicho producto, por ende, podemos afirmar que la ejecución ha sido exitosa.
  2) Método POST para el microservicio de Producto: En este caso ingresamos un nuevo producto mediante este método y lo que se espera es que al momento de enviar los datos en formato JSON el sistema nos entregue un código 200 y además poder visualizar el producto que acabamos de ingresar en la parte inferior de la ventana.
  3) Método DELETE para el microservicio de Producto:  A diferencia de los 2 métodos utilizados previamente, en la barra de ingreso de url debemos incorporar la ID del producto que queramos eliminar de nuestra base de datos. En este caso esperamos que en la parte inferior de la ventana nos entregue un código 200 y que luego corroborando con el método GET que busca por ID no veamos dicho producto.
- Descripción de cada Endpoint en el microservicio carritoservice:
  1) Método GET para la creación de un carrito de compras: Esta función nos permite asignarle un carrito a un usuario en específico. Este carrito como tal posee ciertos atributos tales como la ID del usuario que lo posee, una lista con los productos que esté agregando y el total de la compra.
  2) Método GET para mostrar el resumen del carrito de compras de un usuario en específico: La función resumen nos permite ver el carrito de un usuario, mostrando su nombre, apellido paterno, el total de productos que contiene el carrito, y  el detalle. Este último incluye netamente la cantidad total de dinero que el usuario debería pagar por ese carrito.
  3) Método GET para ver el resumen y resultado de la adición de 2 productos distintos al carrito de compras del usuario con ID 4.
  4) Método POST para agregar un producto al carrito de compras de un usuario: En este caso, esta función nos permite agregar un producto mediante su ID al carrito de un usuario en específico. Mostrando posteriormente la información actualizada del mismo.
  5) Método DELETE para eliminar un producto específico del carrito de compras de un usuario con ID 4: Esta funcionalidad nos permite buscar un producto dentro del carrito de compras de un usuario en específico y mediante la ID de ese producto eliminarlo del mismo.

## 🧑‍💻 Integrantes del Equipo

| Nombre                  | Rol en el proyecto                      | Servicio principal trabajado                        |
|-------------------------|-----------------------------------------|-----------------------------------------------------|
| Martín Baza             | Backend - Pedidos (pendiente)           | pedidoservice, Informe, Swagger UI / OpenAPI        |
| Nicolás Bello           | Repositorio, apoyo, Backend - carrito   | carritoservice, Informe , readme, testing           |
| Rodrigo Vargas          | Gestión documentos, informe, readme     | Informe, readme, Swagger UI / OpenAPI, HATEOAS      |

## 📂 Estructura del Repositorio

```

📦 perfulandia-backend-01v
├── usuarioservice (Carpeta correspondiente al microservicio junto a todos los elementos que lo componen)
├── productoservice (Carpeta correspondiente al microservicio junto a todos los elementos que lo componen)
├── carritoservice (Carpeta correspondiente al microservicio junto a todos los elementos que lo componen)
├── LICENSE (Archivo correspondiente a la licencia de distribución)
└── README.md (Este archivo)

```

## 👥 Colaboración en GitHub

Los primeros dos microservicios fueron creados durante clases, y luego dos personas (Rodrigo y Nicolás) replicaron ese proceso en repositorios separados. Finalmente, Nicolás integró ambos trabajos al repositorio principal.

Tercer microservicio: Martín Baza desarrolló el tercer microservicio por su cuenta (pendiente).

Cuarto microservicio: Nicolás Bello desarrollo el cuarto microservicio por su cuenta.

Swagger UI / OpenAPI, HATEOAS: Rodrigo Vargas desarrollo este apartado en su mayoria con contribuciones menores de Martín Baza

Informe: el informe fue elaborado principalmente por Rodrigo Vargas, con algunas contribuciones de Nicolás y Martín Baza.

Colaboración fuera de GitHub: No fue necesario usar ramas ni hacer pull requests (Solo algunos básicos, como cambios al readme o la implementación del microservicio ya finalizado), ya que la colaboración se hizo fuera de GitHub, ademas de repartir el trabajo, por lo que cada uno trabajo en su respectivo entorno.

## 📈 Lecciones Aprendidas

- Para esta segunda evaluación hemos aprendido a manejar programas como Laragon, Xampp y POSTMAN, que nos han resultado en complementos muy útiles para la realización de las distintas tareas relacionadas netamente a la creación de los distintos microservicios que Martin y Nicolás han realizado. Es por ello que nos sentimos satisfechos con el resultado del trabajo que llevamos hasta la fecha, pero coincidimos en que siempre hay espacio para realizar futuras mejoras y entregar un producto o servicio mucho más pulcro, eficiente y satisfactorio para los usuarios finales/clientes.
  
- Haciendo referencia al trabajo en equipo de esta segunda entrega, estamos de acuerdo en forma unánime en que la distribución de las tareas por cada integrante ha sido realmente equitativa, y planeamos continuar de la misma forma, con la adición de pequeños cambios para la realización del tercer informe.
  
- Arquitectura de Microservicios: Comprendimos la importancia de migrar de un sistema monolítico a una arquitectura basada en microservicios para mejorar la escalabilidad, mantenibilidad y resiliencia del sistema.

- Integración de Tecnologías: Aprendimos a utilizar herramientas como Spring Boot, Docker, Kafka, y APIs REST para desarrollar microservicios independientes pero interoperables.

- Gestión de Bases de Datos: Implementamos bases de datos independientes para cada microservicio (MySQL en Laragon), optimizando consultas y evitando cuellos de botella.

- Documentación y Pruebas: Utilizamos Swagger (OpenAPI) para documentar APIs y JUnit/Mockito para pruebas unitarias, asegurando la calidad del código y la claridad en la comunicación entre servicios.

- Trabajo en Equipo: Coordinamos roles y responsabilidades para desarrollar módulos críticos como CarritoService, ProductoService, y UsuarioService, integrando sus funcionalidades de manera eficiente.
---

[Guía Oficial en Notion – Evaluación Parcial 2 (35%)](https://quilt-canary-969.notion.site/Gu-a-Oficial-Evaluaci-n-Parcial-2-35-1f75b3c4e31280aaab79c9a71f1cfb7b?pvs=4)

[Guía Oficial en Notion – Evaluación Parcial 3 (40%)](https://quilt-canary-969.notion.site/Gu-a-Evaluaci-n-Parcial-3-Desarrollo-Full-Stack-I-2165b3c4e3128066945fcc08ab81ce01)

Link video uso microservicio carrito: https://youtu.be/Ju3OVqRo3WA
Link video uso microservicio desplegado: https://youtu.be/VVvvo7KFjcI

ENDSPOINT DESPLIEGUE:

  ---- Crear usuarios
  Post
  https://usuarioprefulandia-1.onrender.com/api/usuarios

  
  BODY -> RAW -> json:
  
  
  {
  
      "nombre": "Rodrigo Vargas",
      "correo": "Ro.vargas@duocUC.cl",
      "rol": "CLIENTE"
  }
  
  {
  
  	"nombre": "Nicolás Bello",
          "correo": "Ni.bello@duocUC.cl",
          "rol": "CLIENTE"
  }
  
  {
  
  	"nombre": "Martin Baza",
          "correo": "ma.baza@duocUC.com",
          "rol": "CLIENTE"
  }
  
  
  --- Ver usuarios
  GET
  
  https://usuarioprefulandia-1.onrender.com/api/usuarios
  
  
  
  ---Crear perfumes
  https://productoprefulandia.onrender.com/api/productos

  
  BODY -> RAW -> json:
  {
  
          "nombre": "Acqua di Giò - Giorgio Armani",
          "precio": 110.75,
          "stock": 20
      }
  
  {
  
          "nombre": "Sauvage - Dior",
          "precio": 129.99,
          "stock": 25
      }
  
  {
  
          "nombre": "Light Blue - Dolce&Gabbana",
          "precio": 89.5,
          "stock": 40
      }
  
  {
  
          "nombre": "Black Opium - Yves Saint Laurent",
          "precio": 95.0,
          "stock": 30
      }
  
  {
  
          "nombre": "Flowerbomb - Viktor&Rolf",
          "precio": 102.25,
          "stock": 15
      }
  
  
  ---Ver perfumes

  
  GET
  https://productoprefulandia.onrender.com/api/productos

  
  
  --Añadir producto al carrito

  
  POST
  https://carritoprefulandia.onrender.com/api/carrito/2(id_usuario)/agregar/2(id_producto)


  
  ---Ver resumen carrito

  
  GET
  https://carritoprefulandia.onrender.com/api/carrito/2(id_usuario)/resumen


  
  
  --Eliminar producto del carrito

  
  DELETE
  https://carritoprefulandia.onrender.com/api/carrito/2(id_usuario)/eliminar/2(id_producto)
