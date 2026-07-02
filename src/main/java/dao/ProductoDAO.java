package dao;

import modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String SELECT_BASE =
        "SELECT p.id_producto, p.id_categoria, c.nombre AS nombre_categoria, " +
        "       p.nombre, p.descripcion, p.precio, p.activo " +
        "FROM productos p " +
        "JOIN categorias c ON p.id_categoria = c.id_categoria ";

    public List<Producto> listar(String textoBusqueda, Integer idCategoria) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        StringBuilder sql = new StringBuilder(SELECT_BASE);
        sql.append("WHERE p.activo = TRUE ");

        boolean hayTexto = textoBusqueda != null && !textoBusqueda.isBlank();
        if (hayTexto) {
            sql.append("AND LOWER(p.nombre) LIKE ? ");
        }
        if (idCategoria != null) {
            sql.append("AND p.id_categoria = ? ");
        }
        sql.append("ORDER BY p.nombre");

        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int index = 1;
            if (hayTexto) {
                ps.setString(index++, "%" + textoBusqueda.toLowerCase() + "%");
            }
            if (idCategoria != null) {
                ps.setInt(index++, idCategoria);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapear(rs));
                }
            }
        }
        return productos;
    }

    public Producto obtenerPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id_producto = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public void crear(Producto p) throws SQLException {
        String sql = "INSERT INTO productos (id_categoria, nombre, descripcion, precio, activo) " +
                     "VALUES (?, ?, ?, ?, TRUE)";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCategoria());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getPrecio());
            ps.executeUpdate();
        }
    }

    public boolean actualizar(Producto p) throws SQLException {
        String sql = "UPDATE productos SET id_categoria = ?, nombre = ?, descripcion = ?, precio = ? " +
                     "WHERE id_producto = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCategoria());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getPrecio());
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE productos SET activo = FALSE WHERE id_producto = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setNombreCategoria(rs.getString("nombre_categoria"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getInt("precio"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}