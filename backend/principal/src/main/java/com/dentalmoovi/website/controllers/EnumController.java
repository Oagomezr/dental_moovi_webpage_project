package com.dentalmoovi.website.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentalmoovi.website.services.EnumSer;

@RestController
@RequestMapping
public class EnumController {

    private final EnumSer enumSer;

    @GetMapping("/api/user/departaments/{name}")
    public ResponseEntity<Object> getDepartamentsByContaining(@PathVariable("name") String name){
        try{
            return ResponseEntity.ok(enumSer.getDepartamentsByContaining(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    } 

    @GetMapping("/api/user/municipalies/{name}/{id}")
    public ResponseEntity<Object> getLineasVehiculo(@PathVariable("name") String name, @PathVariable("id") int id) {
        try{
            return ResponseEntity.ok(enumSer.getMunicipalyByContaining(name, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/admin/categories/{name}")
    public ResponseEntity<Object> getCategoriesByAdmin(@PathVariable("name") String name) {
        try{
            return ResponseEntity.ok(enumSer.getCategoriesAdmin(name));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    public EnumController(EnumSer enumSer) {
        this.enumSer = enumSer;
    }
}
