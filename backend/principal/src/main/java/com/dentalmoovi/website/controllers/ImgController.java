package com.dentalmoovi.website.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.responses.ImgResponse;
import com.dentalmoovi.website.services.ImgSer;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping
public class ImgController {

    private ImgSer imgSer;

    @GetMapping("/api/public/carousel")
    public ResponseEntity<ImgResponse> getAllCategories() {
        try{
            return ResponseEntity.ok(imgSer.getCarouselImgs());
        } catch (Exception e) {
            Utils.showMessage("error to get caorusel imgs: "+e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/admin/uploadImage")
    public ResponseEntity<MessageDTO> handleFileUpload(@RequestPart("file") MultipartFile file) throws java.io.IOException {
        if (file.isEmpty()) {
            Utils.showMessage("Archivo vacío");
            return ResponseEntity.badRequest().body(new MessageDTO("Archivo vacío"));
        }
        try {
            return ResponseEntity.ok(imgSer.uploadImage(file));
        } catch (IOException e) {
            Utils.showMessage(e.getMessage());
            return ResponseEntity.status(500).body(new MessageDTO("Error al procesar el archivo: " + e.getMessage()));
        }
    }

    @DeleteMapping("/api/admin/deleteImage/{parameter}")
    public ResponseEntity<MessageDTO> deleteImage(@PathVariable("parameter") Long parameter) {
        try{
            return ResponseEntity.ok(imgSer.deleteImg(parameter));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ImgController(ImgSer imgSer) {
        this.imgSer = imgSer;
    }

    
}
