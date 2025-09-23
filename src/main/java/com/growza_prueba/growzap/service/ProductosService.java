package com.growza_prueba.growzap.service;

import com.growza_prueba.growzap.dto.ProductoDTO;
import com.growza_prueba.growzap.model.Categorias;
import com.growza_prueba.growzap.model.Productos;
import com.growza_prueba.growzap.repository.ICategoriaRepository;
import com.growza_prueba.growzap.repository.IProductosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductosService implements IProductosService{
    private final IProductosRepository productosRepository;
    private final ICategoriaRepository categoriaRepository;

    @Autowired
    public ProductosService(IProductosRepository productosRepository, ICategoriaRepository categoriaRepository) {
        this.productosRepository = productosRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Productos> obtenerProductos() {
        return productosRepository.findAll();
    }

    @Override
    public Optional<Productos> obtenerPorId(Long id) {
        return productosRepository.findById(id);
    }

    public Productos crearProducto(ProductoDTO productoDto) {
        Categorias categoria = categoriaRepository.findById(productoDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        Productos nuevoProducto = new Productos();
        nuevoProducto.setNombre_producto(productoDto.getNombreProducto());
        nuevoProducto.setDescripcion(productoDto.getDescripcion());
        nuevoProducto.setPrecio(productoDto.getPrecio());
        nuevoProducto.setStock(productoDto.getStock());
        nuevoProducto.setImagen_url(productoDto.getImagenUrl());
        nuevoProducto.setCategoria(categoria);

        return productosRepository.save(nuevoProducto);
    }

    public Productos editarProducto(Long id, ProductoDTO productoDto) {
        Productos productoExistente = productosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        Categorias categoria = categoriaRepository.findById(productoDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        productoExistente.setNombre_producto(productoDto.getNombreProducto());
        productoExistente.setDescripcion(productoDto.getDescripcion());
        productoExistente.setPrecio(productoDto.getPrecio());
        productoExistente.setStock(productoDto.getStock());
        productoExistente.setImagen_url(productoDto.getImagenUrl());
        productoExistente.setCategoria(categoria);

        return productosRepository.save(productoExistente);
    }

    // Si realmente lo necesitas, puedes dejar este método, pero con la edición con DTO, podría ser obsoleto.
    @Override
    @Transactional
    public void asignarCategoriaAProducto(Long id_producto, Long id_categoria) {
        Productos producto = productosRepository.findById(id_producto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
        Categorias categoria = categoriaRepository.findById(id_categoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));
        producto.setCategoria(categoria);
        productosRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productosRepository.existsById(id)) {
            throw new RuntimeException("El Producto no fue encontrado.");
        }
        productosRepository.deleteById(id);
    }

    public void eliminarTodos() {
        productosRepository.deleteAll();
    }
}