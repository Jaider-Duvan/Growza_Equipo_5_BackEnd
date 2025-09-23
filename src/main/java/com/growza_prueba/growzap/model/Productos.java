package com.growza_prueba.growzap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @NotNull(message = "No puede estar vacío el campo")
    @Column(nullable = false)
    private String nombre_producto;

    private String descripcion;

    @NotNull(message = "No puede estar vacío el campo")
    @Column(nullable = false)
    private double precio;

    @NotNull(message = "No puede estar vacío el campo")
    @Column(nullable = false)
    private int stock;

    @Column(name = "imagen_url", length = 5000)
    private String imagen_url;

    //! Relaciones
    //* Muchos productos pertenecen a una sola categoría
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categorias categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<Detalles_Pedidos> detallesPedidos;

    //* Constructores
    public Productos() {
    }

    public Productos(Long id_producto, String nombre_producto, String descripcion, int stock, double precio, String imagen_url) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.imagen_url = imagen_url;
    }

    //* Getter y Setter

    public Long getId_producto() {
        return id_producto;
    }

    public void setId_producto(Long id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

}
