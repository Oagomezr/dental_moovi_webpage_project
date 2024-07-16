package com.dentalmoovi.website.controllers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.entities.enums.StatusOrderList;
import com.dentalmoovi.website.models.responses.OrdersResponse;
import com.dentalmoovi.website.services.OrdersSer;


@Controller
@RequestMapping
public class OrdersController {
    private final OrdersSer ordersSer;

    public OrdersController(OrdersSer ordersSer) {
        this.ordersSer = ordersSer;
    }

    @PostMapping("/api/user/generateOrder/{idAddress}")
    public ResponseEntity<MessageDTO> generateOrderByUser(@RequestBody CartRequest req, @PathVariable("idAddress") long idAddress) {
        try {
            return ResponseEntity.ok(ordersSer.generateOrder(req, idAddress, false));
        } catch (Exception e) {
            Utils.showMessage("error to generate order:"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/api/admin/generateOrder/{idAddress}")
    public ResponseEntity<MessageDTO> generateOrderByAdmin(@RequestBody CartRequest req, @PathVariable("idAddress") long idAddress) {
        try {
            return ResponseEntity.ok(ordersSer.generateOrder(req, idAddress, true));
        } catch (Exception e) {
            Utils.showMessage("error to generate order:"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/admin/order/{status}/{orderBy}")
    public ResponseEntity<OrdersResponse> getOrdersA(@PathVariable("status") StatusOrderList status, @PathVariable("orderBy") boolean orderBy){
        try{
            return ResponseEntity.ok(ordersSer.getAllOrders(true));
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/user/order/{status}/{orderBy}")
    public ResponseEntity<OrdersResponse> getOrdersU(@PathVariable("status") StatusOrderList status, @PathVariable("orderBy") boolean orderBy){
        try{
            return ResponseEntity.ok(ordersSer.getAllOrders(false));
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/admin/order/{idOrder}")
    public ResponseEntity<Resource> getPdfByAdmin(@PathVariable("idOrder") long idOrder){
        byte[] pdfBytes = ordersSer.getPdfOrder(idOrder, true);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=archivo.pdf")
                .contentLength(pdfBytes.length)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/api/user/order/{idOrder}")
    public ResponseEntity<Resource> getPdf(@PathVariable("idOrder") long idOrder){
        byte[] pdfBytes = ordersSer.getPdfOrder(idOrder, false);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=archivo.pdf")
                .contentLength(pdfBytes.length)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
