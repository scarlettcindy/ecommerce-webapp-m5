package modelo;

/**
 * DTO que representa un producto (taller educativo) del catálogo Klicalia.
 * Mapea la tabla "productos" y trae además el nombre de la categoría
 * (vía JOIN) solo para mostrarlo en las vistas.
 */
public class Producto {

    private int id;
    private int idCategoria;
    private String nombreCategoria;
    private String nombre;
    private String descripcion;
    private int precio;
    private boolean activo;

    public Producto() {
    }

    public Producto(int id, int idCategoria, String nombreCategoria,
                     String nombre, String descripcion, int precio, boolean activo) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}