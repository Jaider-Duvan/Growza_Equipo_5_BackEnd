package com.growza_prueba.growzap.service;

import com.growza_prueba.growzap.model.Categorias;
import com.growza_prueba.growzap.model.Detalles_Carrito;
import com.growza_prueba.growzap.model.Productos;

import java.util.List;
import java.util.Optional;

public interface ICategoriasService {

    List<Categorias> obtenerCategorias();
    Optional<Categorias> obtenerPorId(Long id);
    void guardarCategorias (Categorias categorias);
    void editarCategoria (Long id, Categorias categorias);
    void eliminarCategoria (Long id);
    List<Productos> obtenerProductosPorCategoria(Long id_categoria);
}
