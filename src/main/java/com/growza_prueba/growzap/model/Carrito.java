package com.growza_prueba.growzap.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;

    // Relaciones
    @OneToOne
    @JoinColumn(name = "id_usuarios")
    @JsonBackReference // Evita el bucle infinito al serializar
    private Usuarios usuarios;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detalles_Carrito> detallesCarrito;

    // Constructores
    public Carrito() {
    }

    // Getters and Setters
    public Long getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(Long id_carrito) {
        this.id_carrito = id_carrito;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public List<Detalles_Carrito> getDetallesCarrito() {
        return detallesCarrito;
    }

    public void setDetallesCarrito(List<Detalles_Carrito> detallesCarrito) {
        this.detallesCarrito = detallesCarrito;
    }
}