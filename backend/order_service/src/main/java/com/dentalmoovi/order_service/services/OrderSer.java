package com.dentalmoovi.order_service.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.dentalmoovi.order_service.models.OrderFormat;
import com.dentalmoovi.order_service.repositories.OrderRep;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class OrderSer {

    private static final String FOLDER = "reports";
    private static final String EXTENSION = ".jasper";
    private static final String NAME_REPORT = "orderReport";
    private final OrderRep orderRep;

    public void generateOrder(OrderFormat orderData) throws IOException, JRException{
        byte[] pdfContent = generateOrderReport(orderData);
        orderRep.updateOrderFileById(pdfContent, orderData.getOrderNumber());
    }

    public OrderSer(OrderRep orderRep) {
        this.orderRep = orderRep;
    }

    public byte[] generateOrderReport(OrderFormat order) throws JRException {
        // load .jasper file
        InputStream reportStream = getClass().getResourceAsStream("/"+FOLDER+"/"+NAME_REPORT+EXTENSION);

        if (reportStream == null) {
            throw new IllegalArgumentException("El archivo .jasper no se encuentra");
        }

        // Create report data
        Map<String, Object> parameters = new HashMap<>();

        String firstNameCustomer = capitalizeFirstLetters(order.getCustomerName());
        String lastNameCustomer = capitalizeFirstLetters(order.getCustomerLastName());

        parameters.put("orderNumber", order.getOrderNumber());
        parameters.put("customerName", firstNameCustomer);
        parameters.put("customerLastName", lastNameCustomer);
        parameters.put("celPhone", order.getCelPhone());
        parameters.put("date", order.getDate());
        parameters.put("hour", order.getHour());
        parameters.put("departament", order.getDepartament());
        parameters.put("location", order.getLocation());
        parameters.put("address", order.getAddress());
        parameters.put("enterprise", order.getEnterprise());
        parameters.put("total", order.getTotal());

        // Pass products as a DataSource
        JRBeanCollectionDataSource productDataSource = new JRBeanCollectionDataSource(order.getProducts());

        parameters.put("productDataSource", productDataSource);

        // Fill report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, new JREmptyDataSource());

        // Export the pdf report
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public String capitalizeFirstLetters(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // use a regular expression to find the beginning of words
        Pattern pattern = Pattern.compile("\\b[a-zA-Z]");
        Matcher matcher = pattern.matcher(input.toLowerCase());

        StringBuffer formattedName = new StringBuffer();

        while (matcher.find()) {
            // Replace each match with its uppercase version
            matcher.appendReplacement(formattedName, matcher.group().toUpperCase());
        }
        matcher.appendTail(formattedName);

        return formattedName.toString();
    }
}
