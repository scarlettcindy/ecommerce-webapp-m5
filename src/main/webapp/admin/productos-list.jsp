<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Klicalia | Administrar productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body>

<%@ include file="_header.jsp" %>

<div class="container py-4">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 mb-0">Catálogo de talleres</h1>
        <a href="${pageContext.request.contextPath}/admin/products/new" class="btn btn-klicalia">
            + Nuevo producto
        </a>
    </div>

    <c:if test="${param.msg == 'creado'}">
        <div class="alert alert-success">Producto creado correctamente.</div>
    </c:if>
    <c:if test="${param.msg == 'editado'}">
        <div class="alert alert-success">Producto actualizado correctamente.</div>
    </c:if>
    <c:if test="${param.msg == 'eliminado'}">
        <div class="alert alert-success">Producto eliminado correctamente.</div>
    </c:if>
    <c:if test="${param.msg == 'no_encontrado'}">
        <div class="alert alert-danger">El producto solicitado no existe.</div>
    </c:if>

    <div class="card-panel mb-4">
        <form method="get" action="${pageContext.request.contextPath}/admin/products" class="row g-2">
            <div class="col-md-5">
                <input type="text" name="texto" class="form-control" placeholder="Buscar por nombre..."
                       value="${textoBuscado}">
            </div>
            <div class="col-md-4">
                <select name="idCategoria" class="form-select">
                    <option value="">Todas las categorías</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat.id}" ${cat.id == idCategoriaSeleccionada ? 'selected' : ''}>
                            <c:out value="${cat.nombre}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-outline-klicalia w-100">Buscar</button>
            </div>
        </form>
    </div>

    <div class="card-panel">
        <table class="table table-striped align-middle mb-0">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Precio</th>
                    <th class="text-end">Acciones</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${productos}">
                <tr>
                    <td><c:out value="${p.nombre}"/></td>
                    <td><c:out value="${p.nombreCategoria}"/></td>
                    <td>$<c:out value="${p.precio}"/></td>
                    <td class="text-end">
                        <a href="${pageContext.request.contextPath}/admin/products/edit?id=${p.id}"
                           class="btn btn-sm btn-outline-klicalia">Editar</a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/products/delete"
                              class="d-inline"
                              onsubmit="return confirm('¿Eliminar este producto del catálogo?');">
                            <input type="hidden" name="id" value="${p.id}">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Eliminar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty productos}">
                <tr>
                    <td colspan="4" class="text-center text-muted py-4">
                        No se encontraron productos con ese criterio de búsqueda.
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>