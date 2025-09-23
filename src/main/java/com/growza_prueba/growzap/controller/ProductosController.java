package com.growza_prueba.growzap.controller;

import com.growza_prueba.growzap.dto.ProductoDTO;
import com.growza_prueba.growzap.model.Productos;
import com.growza_prueba.growzap.service.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/growza/productos")
public class ProductosController {

    private final ProductosService productosService;

    @Autowired
    public ProductosController(ProductosService productosService) {
        this.productosService = productosService;
    }

    // Listar todos
    @GetMapping
    public List<Productos> listaProductos() {
        return productosService.obtenerProductos();
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Productos> obtenerPorId(@PathVariable Long id) {
        Optional<Productos> producto = productosService.obtenerPorId(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear producto con DTO
    @PostMapping("/crearProductoCategoria")
    public ResponseEntity<String> crearProducto(
            @RequestParam("nombreProducto") String nombreProducto,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("idCategoria") Long idCategoria,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) {
        try {
            String rutaImagen = null;

            // 👇 Si llega archivo, lo guardamos en carpeta local
            if (imagen != null && !imagen.isEmpty()) {
                String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path ruta = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

                Files.createDirectories(ruta.getParent()); // crea la carpeta si no existe
                Files.copy(imagen.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

                // ruta relativa para servir la imagen luego
                rutaImagen = "/uploads/" + nombreArchivo;
            }

            // Construimos el DTO con la info recibida
            ProductoDTO productoDto = new ProductoDTO();
            productoDto.setNombreProducto(nombreProducto);
            productoDto.setDescripcion(descripcion);
            productoDto.setPrecio(precio);
            productoDto.setStock(stock);
            productoDto.setIdCategoria(idCategoria);
            productoDto.setImagenUrl(rutaImagen);

            // Guardamos en la BD
            productosService.crearProducto(productoDto);

            return ResponseEntity.ok("✅ Producto creado con éxito y categoría asignada.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error al guardar la imagen: " + e.getMessage());
        }
    }



    // Editar producto con DTO
    @PutMapping("/{id}")
    public ResponseEntity<Productos> editarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDto) {
        try {
            Productos actualizado = productosService.editarProducto(id, productoDto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar producto por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productosService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminar todos (para tu botón "Eliminar todos")
    @DeleteMapping("/todos")
    public ResponseEntity<Void> eliminarTodos() {
        productosService.eliminarTodos();
        return ResponseEntity.noContent().build();
    }

    // Asignar categoría a un producto
    @PutMapping("/{idProducto}/categoria/{idCategoria}")
    public ResponseEntity<String> asignarCategoriaAProducto(
            @PathVariable Long idProducto,
            @PathVariable Long idCategoria) {
        try {
            productosService.asignarCategoriaAProducto(idProducto, idCategoria);
            return ResponseEntity.ok("Categoría asignada al producto con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
