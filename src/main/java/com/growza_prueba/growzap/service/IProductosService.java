package com.growza_prueba.growzap.service;

import com.growza_prueba.growzap.model.Categorias;
import com.growza_prueba.growzap.model.Productos;
import com.growza_prueba.growzap.model.Usuarios;

import java.util.List;
import java.util.Optional;

public interface IProductosService {
    List<Productos> obtenerProductos();
    Optional<Productos> obtenerPorId (Long id);
//    void guardarProducto (Productos producto);
//    void editarProducto (Long id , Productos producto);
    void eliminarProducto (Long id);
    void asignarCategoriaAProducto(Long id_producto, Long id_categoria);
}
