package controlador;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import modelo.Categoria;
import modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet(urlPatterns = {"/admin/products", "/admin/products/*"})
public class ProductoServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ruta = req.getPathInfo(); // null, "/new" o "/edit"

        try {
            if (ruta == null || ruta.equals("/")) {
                listar(req, resp);
            } else if (ruta.equals("/new")) {
                mostrarFormularioNuevo(req, resp);
            } else if (ruta.equals("/edit")) {
                mostrarFormularioEditar(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al acceder a la base de datos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ruta = req.getPathInfo(); // null, "/update" o "/delete"

        try {
            if (ruta == null || ruta.equals("/")) {
                crear(req, resp);
            } else if (ruta.equals("/update")) {
                actualizar(req, resp);
            } else if (ruta.equals("/delete")) {
                eliminar(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al acceder a la base de datos", e);
        }
    }

    // ---------------------------------------------------------- LISTAR ----

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        String texto = req.getParameter("texto");
        String catParam = req.getParameter("idCategoria");
        Integer idCategoria = (catParam != null && !catParam.isBlank())
                ? Integer.valueOf(catParam) : null;

        List<Producto> productos = productoDAO.listar(texto, idCategoria);
        List<Categoria> categorias = categoriaDAO.listarTodas();

        req.setAttribute("productos", productos);
        req.setAttribute("categorias", categorias);
        req.setAttribute("textoBuscado", texto);
        req.setAttribute("idCategoriaSeleccionada", idCategoria);

        req.getRequestDispatcher("/admin/productos-list.jsp").forward(req, resp);
    }

    // ------------------------------------------------------ FORM NUEVO ----

    private void mostrarFormularioNuevo(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        req.setAttribute("categorias", categoriaDAO.listarTodas());
        req.setAttribute("modo", "crear");
        req.getRequestDispatcher("/admin/producto-form.jsp").forward(req, resp);
    }

    // ----------------------------------------------------- FORM EDITAR ----

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        Producto producto = productoDAO.obtenerPorId(id);

        if (producto == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?msg=no_encontrado");
            return;
        }

        req.setAttribute("categorias", categoriaDAO.listarTodas());
        req.setAttribute("producto", producto);
        req.setAttribute("modo", "editar");
        req.getRequestDispatcher("/admin/producto-form.jsp").forward(req, resp);
    }

    // ----------------------------------------------------------- CREAR ----

    private void crear(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Producto producto = leerYValidar(req);

        if (req.getAttribute("errores") != null) {
            req.setAttribute("categorias", categoriaDAO.listarTodas());
            req.setAttribute("modo", "crear");
            req.setAttribute("producto", producto);
            req.getRequestDispatcher("/admin/producto-form.jsp").forward(req, resp);
            return;
        }

        productoDAO.crear(producto);
        resp.sendRedirect(req.getContextPath() + "/admin/products?msg=creado");
    }

    // ------------------------------------------------------- ACTUALIZAR ---

    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Producto producto = leerYValidar(req);
        producto.setId(Integer.parseInt(req.getParameter("id")));

        if (req.getAttribute("errores") != null) {
            req.setAttribute("categorias", categoriaDAO.listarTodas());
            req.setAttribute("modo", "editar");
            req.setAttribute("producto", producto);
            req.getRequestDispatcher("/admin/producto-form.jsp").forward(req, resp);
            return;
        }

        productoDAO.actualizar(producto);
        resp.sendRedirect(req.getContextPath() + "/admin/products?msg=editado");
    }

    // --------------------------------------------------------- ELIMINAR ---

    private void eliminar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        productoDAO.eliminar(id);
        resp.sendRedirect(req.getContextPath() + "/admin/products?msg=eliminado");
    }

    private Producto leerYValidar(HttpServletRequest req) {
        StringBuilder errores = new StringBuilder();

        String nombre = req.getParameter("nombre");
        String descripcion = req.getParameter("descripcion");
        String precioTexto = req.getParameter("precio");
        String idCategoriaTexto = req.getParameter("idCategoria");

        if (nombre == null || nombre.isBlank()) {
            errores.append("El nombre es obligatorio. ");
        }
        if (idCategoriaTexto == null || idCategoriaTexto.isBlank()) {
            errores.append("Debes seleccionar una categoría. ");
        }

        int precio = 0;
        boolean precioValido = true;
        try {
            precio = Integer.parseInt(precioTexto);
            if (precio <= 0) {
                errores.append("El precio debe ser mayor a 0. ");
                precioValido = false;
            }
        } catch (NumberFormatException | NullPointerException e) {
            errores.append("El precio debe ser un número válido. ");
            precioValido = false;
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precioValido ? precio : 0);
        try {
            producto.setIdCategoria(Integer.parseInt(idCategoriaTexto));
        } catch (NumberFormatException | NullPointerException e) {
            producto.setIdCategoria(0);
        }

        if (errores.length() > 0) {
            req.setAttribute("errores", errores.toString());
        }

        return producto;
    }
}