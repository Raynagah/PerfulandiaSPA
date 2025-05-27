# 🧾 Proyecto: Transformación Digital - Perfulandia SPA


Este repositorio contiene el desarrollo técnico del sistema basado en microservicios para la empresa Perfulandia SPA, como parte de la Evaluación Parcial 2 de la asignatura **Desarrollo Full Stack I**.

## 📦 Descripción General del Proyecto

El sistema propuesto usa microservicios (Usuario, Producto y Pedido) para reemplazar el sistema monolítico actual.

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
  - Gestiona la información sobre productos (nombre, descripción, precio, cantidad, etc.).
  - Interactúa con su propia base de datos para almacenar la información de los productos.
  - Expone una API REST para crear, leer, actualizar y eliminar productos.

Microservicio de Usuario:
  - Maneja la gestión de usuarios (registro, inicio de sesión, perfiles, roles, etc.).
  - Tiene su propia base de datos para almacenar los datos de los usuarios.
  - Se integra con un sistema de autenticación (como JWT o OAuth) para asegurar las comunicaciones y operaciones sensibles.

Microservicio de Pedido:

  - Gestiona los pedidos de los usuarios (creación, estado del pedido, historial de pedidos, detalles de envío).
  - Puede interactuar con los microservicios de Producto y Usuario a través de API REST o eventos asincrónicos para validar el stock de productos y la información   del cliente.

Base de Datos por Servicio:
  - Producto: Base de datos específica para almacenar la información de los productos.
  - Usuario: Base de datos para almacenar los perfiles, credenciales y roles de los usuarios.
  - Pedido: Base de datos para gestionar los pedidos, incluyendo detalles de los productos, cantidades, estado y usuario relacionado.

### Microservicios Desarrollados

- `usuarioservice`: > 📝 Describir qué funcionalidades ofrece este microservicio.
- `productoservice`: > 📝 Describir qué funcionalidades ofrece este microservicio.
- `pedidoservice`: > 📝 Indicar el nombre y función del microservicio adicional implementado.

## 🛠️ Tecnologías Utilizadas

> Spring Boot
> Maven
> Github
> IA
> Postman
> Laragon
> Visual Studio Code
> Discord

## 🗄️ Configuración de Bases de Datos

> 📝 Indicar qué motor de base de datos usaron, cómo configuraron la conexión (`application.properties`), y qué tablas y campos definieron para cada microservicio.

## 📮 Endpoints y Pruebas

> 📝 Especificar los principales endpoints disponibles por microservicio (CRUD y llamadas entre servicios).  
> Incluir capturas o descripciones de pruebas realizadas con Postman (mínimo 3 por micro-servicio).

## 🧑‍💻 Integrantes del Equipo


| Nombre                  | Rol en el proyecto         | Servicio principal trabajado |
|-------------------------|----------------------------|------------------------------|
| Martín Baza             | Backend - Pedidos          | Pedidoservice                |
| Nicolás Bello           | Repositorio, apoyo         | Pedidoservice                |
| Rodrigo Vargas          | Gestión documentos         | Informe                      |

## 📂 Estructura del Repositorio

> 📝 Explicar brevemente la organización de carpetas del repositorio (por ejemplo, cada carpeta corresponde a un microservicio separado con su propio `pom.xml`).

```

📦 perfulandia-microservices
├── usuarioservice
├── productoservice
├── pedidoservice
└── README.md

```

## 👥 Colaboración en GitHub

Los primeros dos microservicios fueron creados durante clases, y luego dos personas (Rodrigo y Nicolás) replicaron ese proceso en repositorios separados. Finalmente, Nicolás integró ambos trabajos al repositorio principal.

Tercer microservicio: Martín Baza desarrolló el tercer microservicio por su cuenta, mientras Nicolás le brindaba soporte a través de Discord.

Informe: el informe fue elaborado principalmente por Rodrigo Vargas, con algunas contribuciones menores de Nicolás en ciertos aspectos.

Colaboración fuera de GitHub: No fue necesario usar ramas ni hacer pull requests, ya que la colaboración se hizo fuera de GitHub y no se usaron flujos convencionales de control de versiones en la plataforma.

## 📈 Lecciones Aprendidas

> 📝 Reflexionar brevemente sobre qué aprendieron durante el desarrollo del proyecto (técnico y en trabajo en equipo).

---

[Guía Oficial en Notion – Evaluación Parcial 2 (35%)](https://quilt-canary-969.notion.site/Gu-a-Oficial-Evaluaci-n-Parcial-2-35-1f75b3c4e31280aaab79c9a71f1cfb7b?pvs=4)

