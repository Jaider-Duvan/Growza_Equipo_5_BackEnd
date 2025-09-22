package com.growza_prueba.growzap.service;

import com.growza_prueba.growzap.dto.ProductoDTO;
import com.growza_prueba.growzap.model.Categorias;
import com.growza_prueba.growzap.model.Productos;
import com.growza_prueba.growzap.repository.ICarritoRepository;
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
    private CategoriasService categoriasService;

    @Autowired
    public ProductosService(IProductosRepository productosRepository, ICategoriaRepository categoriaRepository, CategoriasService categoriasService) {
        this.productosRepository = productosRepository;
        this.categoriaRepository = categoriaRepository;
        this.categoriasService = categoriasService;
    }


    @Override
    public List<Productos> obtenerProductos() {
        return productosRepository.findAll();
    }

    @Override
    public Optional<Productos> obtenerPorId(Long id) {
        return productosRepository.findById(id);
    }

    @Override
    public void guardarProducto(Productos producto) {
        productosRepository.save(producto);
    }

    @Override
    public void editarProducto(Long id, Productos producto) {
        Optional<Productos> productoExiste = productosRepository.findById(id);
        if (productoExiste.isPresent()){
            Productos editarProducto = productoExiste.get();
            editarProducto.setNombre_producto(producto.getNombre_producto());
            editarProducto.setDescripcion(producto.getDescripcion());
            editarProducto.setPrecio(producto.getPrecio());
            editarProducto.setStock(producto.getStock());
            productosRepository.save(editarProducto);
        }else {
            throw new RuntimeException("El Producto no fue encontrado.");
        }
    }

    // Nuevo método para crear un producto usando el DTO
    public Productos crearProductoConCategoria(ProductoDTO productoDto) {
        Categorias categoria = categoriaRepository.findById(productoDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        Productos nuevoProducto = new Productos();
        nuevoProducto.setNombre_producto(productoDto.getNombreProducto());
        nuevoProducto.setDescripcion(productoDto.getDescripcion());
        nuevoProducto.setPrecio(productoDto.getPrecio());
        nuevoProducto.setStock(productoDto.getStock());
        nuevoProducto.setImagen_url(productoDto.getImagenUrl());
        nuevoProducto.setCategoria(categoria); // Asigna la entidad Categorias

        return productosRepository.save(nuevoProducto);
    }


    // Nuevo método de edición que también recibe el DTO
    public Productos editarProductoConCategoria(Long id, ProductoDTO productoDto) {
        Productos productoExistente = productosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        // Busca la categoría
        Categorias categoria = categoriaRepository.findById(productoDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        // Actualiza los campos
        productoExistente.setNombre_producto(productoDto.getNombreProducto());
        productoExistente.setDescripcion(productoDto.getDescripcion());
        productoExistente.setPrecio(productoDto.getPrecio());
        productoExistente.setStock(productoDto.getStock());
        productoExistente.setImagen_url(productoDto.getImagenUrl());
        productoExistente.setCategoria(categoria);

        return productosRepository.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long id) {
        Optional<Productos> productoExiste = productosRepository.findById(id);
        if (productoExiste.isPresent()){
            Productos eliminarProducto = productoExiste.get();
            productosRepository.delete(eliminarProducto);
        }else {
            throw new RuntimeException("El Producto no fue encontrado.");
        }
    }

    @Override
    @Transactional
    public void asignarCategoriaAProducto(Long id_producto, Long id_categoria) {
        // Obtiene los objetos, lanzando una excepción si no se encuentran
        Productos producto = productosRepository.findById(id_producto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
        Categorias categoria = categoriasService.obtenerPorId(id_categoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));
        // Asigna la categoría al producto. Esta es la única línea que necesitas.
        producto.setCategoria(categoria);
        // Guarda el producto. JPA se encarga de actualizar la clave foránea en la tabla.
        productosRepository.save(producto);
    }


}
