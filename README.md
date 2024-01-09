# SINF-P1
# Sistema de Gestión de Base de Datos para Agencia de Viajes

Este proyecto Java tiene como objetivo gestionar la base de datos de una agencia de viajes, haciendo uso de los sistemas de gestión de bases de datos NoSQL: Cassandra y MongoDB. Proporciona una interfaz de consola para interactuar con las bases de datos y realizar operaciones CRUD.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Contribución](#contribución)
- [Licencia](#licencia)

## Descripción

El proyecto está diseñado como una aplicación de consola Java que permite a los usuarios realizar operaciones de creación, lectura, actualización y eliminación de registros en las bases de datos Cassandra y MongoDB. Ofrece una interfaz amigable para gestionar destinos turísticos, paquetes, reservas y clientes.

## Características

- **Interfaz de Consola Intuitiva:** Presenta un menú interactivo que guía a los usuarios a través de las funcionalidades de la aplicación.
- **Operaciones CRUD:** Permite realizar operaciones básicas de la base de datos, como crear, leer, actualizar y eliminar registros.
- **Consultas Personalizadas:** Ofrece consultas específicas para obtener detalles sobre paquetes turísticos, reservas por cliente, destinos populares, entre otros.

## Requisitos

- **Java:** Asegúrate de tener instalado Java en tu sistema.
- **Bases de Datos:** Se requiere la instalación y configuración de los sistemas de gestión de bases de datos Cassandra y MongoDB.

## Instalación

1. Clona el repositorio a tu máquina local utilizando `git clone`.
2. Abre el proyecto en tu entorno de desarrollo preferido (Eclipse, IntelliJ, etc.).
3. Configura las dependencias y el classpath necesario para la conexión a las bases de datos.
4. Asegúrate de tener configuradas las credenciales y los puertos para las conexiones a Cassandra y MongoDB.

## Uso

1. Ejecuta la clase `Main` para iniciar la aplicación.
2. Selecciona el sistema de base de datos que deseas utilizar (Cassandra o MongoDB).
3. Utiliza el menú interactivo para realizar las operaciones deseadas en la base de datos seleccionada.

## Estructura del Proyecto

El proyecto se organiza en varios paquetes:

- `CasandraSinf`: Contiene la interfaz `DataBase`, las clases para interactuar con la base de datos y los modelos de datos (`Cliente`, `Destino`, `Paquete`, `Reserva`).
- `Main`: Clase principal que gestiona la interfaz de consola y las opciones del menú.
- `Mongo`, `_Mongo`: Clases para manejar la conexión y operaciones específicas para MongoDB.
- `Cassandra`: Clase para manejar la conexión y operaciones específicas para Cassandra.

## Contribución

Si deseas contribuir al proyecto, puedes hacerlo abriendo issues, proponiendo mejoras o enviando solicitudes de extracción (Pull Requests) en GitHub.

## Licencia

Este proyecto está bajo la licencia [LICENSE]. Consulta el archivo LICENSE para obtener más detalles.
