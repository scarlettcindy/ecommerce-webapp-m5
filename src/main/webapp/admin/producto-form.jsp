<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Klicalia | <c:choose><c:when test="${modo == 'crear'}">Nuevo producto</c:when><c:otherwise>Editar producto</c:otherwise></c:choose></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body>

<%@ include file="_header.jsp" %>

<div class="container py-4" style="max-width: 640px;">

    <h1 class="h4 mb-4">
        <c:choose>
            <c:when test="${modo == 'crear'}">Nuevo producto</c:when>
            <c:otherwise>Editar producto</c:otherwise>
        </c:choose>
    </h1>

    <c:if test="${not empty errores}">
        <div class="alert alert-danger"><c:out value="${errores}"/></div>
    </c:if>

    <div class="card-panel">
        <c:choose>
            <c:when test="${modo == 'crear'}">
                <c:set var="accionForm" value="${pageContext.request.contextPath}/admin/products"/>
            </c:when>
            <c:otherwise>
                <c:set var="accionForm" value="${pageContext.request.contextPath}/admin/products/update"/>
            </c:otherwise>
        </c:choose>

        <form method="post" action="${accionForm}">

            <c:if test="${modo == 'editar'}">
                <input type="hidden" name="id" value="${producto.id}">
            </c:if>

            <div class="mb-3">
                <label class="form-label">Nombre</label>
                <input type="text" name="nombre" class="form-control" value="${producto.nombre}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Categoría</label>
                <select name="idCategoria" class="form-select" required>
                    <option value="">Selecciona una categoría</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat.id}" ${cat.id == producto.idCategoria ? 'selected' : ''}>
                            <c:out value="${cat.nombre}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Descripción</label>
                <textarea name="descripcion" class="form-control" rows="3"><c:out value="${producto.descripcion}"/></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Precio (CLP)</label>
                <input type="number" name="precio" class="form-control" min="1" step="1"
                       value="${producto.precio}" required>
            </div>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-klicalia">Guardar</button>
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>