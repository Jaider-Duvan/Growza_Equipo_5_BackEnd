package com.growza_prueba.growzap.controller;

import com.growza_prueba.growzap.dto.ProductoDTO;
import com.growza_prueba.growzap.model.Productos;
import com.growza_prueba.growzap.model.Usuarios;
import com.growza_prueba.growzap.service.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/growza/productos")
public class ProductosCotroller {
    public ProductosService productosService;

    @Autowired
    public ProductosCotroller(ProductosService productosService) {
        this.productosService =productosService;
    }

    @GetMapping
    public List<Productos> listaProductos() {
        return productosService.obtenerProductos();
    }

    @GetMapping("/{id}")
    public Optional<Productos> listaUsuariosPorId(@PathVariable Long id) {
        return productosService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearProducto(@RequestBody Productos productos) {
        productosService.guardarProducto(productos);
        return ResponseEntity.ok("Producto creado con éxito");
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editarProducto(@PathVariable Long id, @RequestBody Productos producto) {
        productosService.editarProducto(id, producto);
        return ResponseEntity.ok("Producto editado con éxito");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        productosService.eliminarProducto(id);
        return ResponseEntity.ok("Producto eliminado con éxito");
    }


    //* Asignar una categoria a un producto
    @PutMapping("/{idProducto}/categoria/{idCategoria}")
    public ResponseEntity<String> asignarCategoriaAProducto(@PathVariable Long idProducto, @PathVariable Long idCategoria) {
        try {
            productosService.asignarCategoriaAProducto(idProducto, idCategoria);
            return ResponseEntity.ok("Categoría asignada al producto con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
