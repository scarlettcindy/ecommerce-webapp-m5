# ecommerce-webapp-m5 — Klicalia Admin de Productos

Aplicación web Java EE (JSP + Servlets + DAO + MVC) para administrar el catálogo de talleres educativos de Klicalia, conectada a PostgreSQL vía JDBC. Continúa el proyecto de los módulos anteriores: reutiliza el diseño del frontend (M2), el modelo de base de datos (M3) y el catálogo de productos usado en la app de consola (M4).

## Repositorio
https://github.com/scarlettcindy/ecommerce-webapp-m5

## Tecnologías
- Java (Jakarta EE 9+)
- Servlets + JSP + JSTL
- Patrón DAO con JDBC (PreparedStatement, conexión Singleton)
- PostgreSQL
- Apache Tomcat 10.1

## Instrucciones de ejecución

1. Crear una base de datos PostgreSQL llamada `ecommerce_Klicalia` (o reutilizar la del Módulo 3).
2. Ejecutar `SQL/schema.sql` y luego `SQL/seed.sql`.
3. Importar el proyecto en Eclipse como Dynamic Web Project (Jakarta EE 9, Servlet 5.0).
4. Verificar que en `WEB-INF/lib` estén: el driver de PostgreSQL y las librerías JSTL.
5. En `dao/ConexionBD.java`, reemplazar `CLAVE` por tu contraseña real de PostgreSQL.
6. Agregar el proyecto a Tomcat 10.1 y ejecutar (`Run As > Run on Server`).
7. Abrir `http://localhost:8080/KlicaliaEcommerce/`

## Rutas principales

| Método | Ruta                        | Acción                                         |
|--------|-----------------------------|------------------------------------------------|
| GET    | /admin/products             | Listar productos (búsqueda + filtro por categoría) 
| GET    | /admin/products/new         | Formulario para crear producto                 |
| POST   | /admin/products             | Crear producto                                 |
| GET    | /admin/products/edit?id=..  | Formulario para editar producto                |
| POST   | /admin/products/update      | Actualizar producto                            |
| POST   | /admin/products/delete      | Eliminar producto (baja lógica)                |

## Notas de diseño

- **Eliminación:** es una baja lógica (`activo = FALSE`) en vez de un `DELETE`, para no romper la relación con `detalle_orden` si el producto ya fue vendido.
- **Validaciones:** nombre obligatorio, categoría obligatoria, precio numérico mayor a 0. Si el formulario tiene errores, se vuelve a mostrar con los datos ingresados y el mensaje de error.
- **Patrón:** un único Servlet (`ProductoServlet`) enruta todas las acciones según el método HTTP y la URL (Front Controller), siguiendo Post/Redirect/Get para evitar reenvíos duplicados al recargar la página.