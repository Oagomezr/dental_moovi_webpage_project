package com.dentalmoovi.order_service.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.dentalmoovi.order_service.models.OrderFormat;
import com.dentalmoovi.order_service.repositories.OrderRep;
import com.itextpdf.text.DocumentException;

@Service
public class OrderSer {

    private final SpringTemplateEngine ste;
    private final OrderRep orderRep;

    public void generateOrder(OrderFormat orderData) throws IOException, DocumentException{
        byte[] pdfContent = generatePdf(orderData);
        orderRep.updateOrderFileById(pdfContent, orderData.orderNumber());
    }

    private byte[] generatePdf(OrderFormat orderData) throws IOException, DocumentException{

        Context context = new Context();
        context.setVariable("order", orderData);

        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }

    private String loadAndFillTemplate(Context context) {
        return ste.process("order", context);
    }

    private byte[] renderPdf(String html) throws IOException, DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public OrderSer(SpringTemplateEngine ste, OrderRep orderRep) {
        this.ste = ste;
        this.orderRep = orderRep;
    }
}
