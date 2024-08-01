package com.dentalmoovi.website.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.cart.CartResponse;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.services.ProductsSer;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping
public class ProductsController {
    private final ProductsSer productsSer;

    public ProductsController(ProductsSer productsSer) {
        this.productsSer = productsSer;
    }

    private static Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @GetMapping("/api/public/products/search/{name}/{limit}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByContaining(
        @PathVariable("name") String name, @PathVariable("limit") boolean limit, 
        @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize)
    {
        try {
            ProductsResponse products = productsSer.getProductsByContaining(name, limit, pageNumber, pageSize, false);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/public/products/{id}")
    public ResponseEntity<ProductsDTO> getProduct(@PathVariable("id") Long id){
        try {
            ProductsDTO products = productsSer.getProduct(id, false);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/public/products/category/{name}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByCategory(
        @PathVariable("name") String name, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize){
        try {
            ProductsResponse products = productsSer.getProductsByCategory(name, pageNumber, pageSize, false);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/admin/products/updateMainImage/{idProduct}")
    public ResponseEntity<MessageDTO> updateMainImage(@PathVariable("idProduct") Long id, @RequestBody long idImage) {
        try{
            return ResponseEntity.ok(productsSer.updateMainImage(idImage, id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/admin/products/uploadImage/{idProduct}")
    public ResponseEntity<MessageDTO> handleFileUpload(@RequestPart("file") MultipartFile file, @PathVariable("idProduct") Long idProduct) throws java.io.IOException {
        if (file.isEmpty()) {
            logger.info("Archivo vacío");
            return ResponseEntity.badRequest().body(new MessageDTO("Archivo vacío"));
        }
        try {
            return ResponseEntity.ok(productsSer.uploadImage(file, idProduct));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new MessageDTO("Error al procesar el archivo: " + e.getMessage()));
        }
    }

    @DeleteMapping("/api/admin/products/deleteImage/{parameter}")
    public ResponseEntity<MessageDTO> deleteImage(@PathVariable("parameter") String parameter) {
        try{
            return ResponseEntity.ok(productsSer.deleteImage(parameter));
        } catch (Exception e) {
            Utils.showMessage("The imagen could not delete because: "+e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/admin/products/deleteProduct/{id}")
    public ResponseEntity<MessageDTO> deleteProduct(@PathVariable("id") Long id) {
        try{
            return ResponseEntity.ok(productsSer.deleteProduct(id));
        } catch (Exception e) {
            Utils.showMessage("The imagen could not delete because: "+e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/admin/products/visibility/{productName}")
    public ResponseEntity<MessageDTO> changeVisibilityProduct(@PathVariable("productName") String productName, @RequestBody boolean visibility) {
        try{
            return ResponseEntity.ok(productsSer.hideOrShowProduct(visibility, productName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/admin/products/updateProductInfo/{idProduct}/{option}")
    public ResponseEntity<MessageDTO> updateProductInfo(@PathVariable("idProduct") Long idProduct, @PathVariable("option") int option, @RequestBody String newInfo) {
        try{
            return ResponseEntity.ok(productsSer.updateProductInfo(option, idProduct, newInfo));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/admin/products/createProduct")
    public ResponseEntity<MessageDTO> createProduct(@RequestBody ProductsDTO product) {
        try{
            
            return ResponseEntity.status(HttpStatus.CREATED).body(productsSer.createProduct(product));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/api/admin/products/search/{name}/{limit}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByContainingA(
        @PathVariable("name") String name, @PathVariable("limit") boolean limit, 
        @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize)
    {
        try {
            ProductsResponse products = productsSer.getProductsByContaining(name, limit, pageNumber, pageSize, true);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/admin/products/{id}")
    public ResponseEntity<ProductsDTO> getProductA(@PathVariable("id") Long id){
        try {
            ProductsDTO products = productsSer.getProduct(id, true);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            Utils.showMessage("Error al obtener el producto: "+e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/admin/products/category/{name}/{pageNumber}/{pageSize}")
    public ResponseEntity<ProductsResponse> getProductsByCategoryA(
        @PathVariable("name") String name, @PathVariable("pageNumber") int pageNumber, 
        @PathVariable("pageSize") int pageSize)
    {
        try {
            ProductsResponse products = productsSer.getProductsByCategory(name, pageNumber, pageSize, true);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/public/shoppingCart")
    public ResponseEntity<CartResponse> getShoppingCartProducts(@RequestBody CartRequest req){
        try{
            return ResponseEntity.ok(productsSer.getShoppingCartProducts(req, false, false));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/admin/shoppingCart")
    public ResponseEntity<CartResponse> getShoppingCartProductsA(@RequestBody CartRequest req){
        try{
            return ResponseEntity.ok(productsSer.getShoppingCartProducts(req, true, false));
        } catch (Exception e) {
            logger.debug("error:", e);
            return ResponseEntity.notFound().build();
        }
    }
}
