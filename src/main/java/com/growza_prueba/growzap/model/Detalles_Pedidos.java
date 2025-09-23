package com.growza_prueba.growzap.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Detalles_Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id_detalle_pedido;

    @NotNull(message = "No puede estar vacío el campo")
    @Column(nullable = false)
    private int cantidad;

    @NotNull(message = "No puede estar vacío el campo")
    @Column(nullable = false)
    private double precio_unitario;

    //! Relaciones
    //* Muchos detalles de pedidos pertenecen a un solo pedido
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonBackReference("pedido-detalle")
    private Pedidos pedido;

    //* Muchos detalles de pedidos se refieren a un solo producto
    @ManyToOne
    @JoinColumn(name = "id_producto")
    @JsonBackReference("producto-detalle")
    private Productos producto;

    //* Constructores
    public Detalles_Pedidos() {
    }

    public Detalles_Pedidos(Long id_detalle_pedido, int cantidad, double precio_unitario) {
        this.id_detalle_pedido = id_detalle_pedido;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
    }

    //*  Getter y Setter
    public Long getId_detalle_pedido() {
        return id_detalle_pedido;
    }

    public void setId_detalle_pedido(Long id_detalle_pedido) {
        this.id_detalle_pedido = id_detalle_pedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}
