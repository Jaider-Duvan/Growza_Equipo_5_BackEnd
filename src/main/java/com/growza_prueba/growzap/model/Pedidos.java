package com.growza_prueba.growzap.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(nullable = false)
    private LocalDate fecha_pedido;

    @NotNull(message = "No puede estar vacío el campo")
    @Column(nullable = false)
    private double total;

    @NotNull(message = "No puede estar vacío el campo")
    @Column(nullable = false)
    private String estado;

    //! Relaciones
    //* Muchos pedidos pertenecen a un solo usuario
    @ManyToOne
    @JoinColumn(name = "id_usuarios")
    @JsonBackReference("usuario-pedidos")
    private Usuarios usuarios;

    //* Un pedido tiene un único pago
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Pagos pago;

    //* Un pedido tiene una única factura
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Facturas factura;

    //* Un pedido se compone de múltiples líneas de detalle
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference("pedido-detalle")
    private List<Detalles_Pedidos> detallesPedidos;

    //* Constructores
    public Pedidos() {
    }

    public Pedidos(Long id_pedido, LocalDate fecha_pedido, double total, String estado, Usuarios usuario) {
        this.id_pedido = id_pedido;
        this.fecha_pedido = fecha_pedido;
        this.total = total;
        this.estado = estado;
    }

    //* Getters y Setters

    public Long getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Long id_pedido) {
        this.id_pedido = id_pedido;
    }

    public LocalDate getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(LocalDate fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
