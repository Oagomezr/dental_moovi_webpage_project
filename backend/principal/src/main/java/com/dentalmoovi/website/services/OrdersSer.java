package com.dentalmoovi.website.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.cart.CartResponse;
import com.dentalmoovi.website.models.cart.OrderFormat;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.OrderDTO;
import com.dentalmoovi.website.models.entities.ActivityLogs;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Orders;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.Departaments;
import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;
import com.dentalmoovi.website.models.entities.enums.StatusOrderList;
import com.dentalmoovi.website.models.exceptions.IncorrectException;
import com.dentalmoovi.website.models.responses.OrdersResponse;
import com.dentalmoovi.website.repositories.ActivityLogsRep;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.OrdersRep;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class OrdersSer {
    private final OrdersRep ordersRep;
    private final ProductsSer productsSer;
    private final AddressesRep addressesRep;
    private final SpringTemplateEngine ste;
    private final DepartamentsRep departamentsRep;
    private final MunicipalyRep municipalyRep;
    private final UserSer userSer;
    private final ActivityLogsRep activityLogsRep;

    public OrdersSer(OrdersRep ordersRep, ProductsSer productsSer, AddressesRep addressesRep, UserSer userSer,
            SpringTemplateEngine ste, DepartamentsRep departamentsRep, MunicipalyRep municipalyRep, ActivityLogsRep activityLogsRep) {
        this.ordersRep = ordersRep;
        this.productsSer = productsSer;
        this.addressesRep = addressesRep;
        this.ste = ste;
        this.departamentsRep = departamentsRep;
        this.municipalyRep = municipalyRep;
        this.userSer = userSer;
        this.activityLogsRep = activityLogsRep;
    }

    private boolean admin = false;

    Orders order;
    Users user;

    public void downloadOrder(CartRequest req, long idAddress, boolean admin, HttpServletResponse response){
        this.admin = admin;
        try{
            File pdf = generateOrder(req, idAddress);
            Path file = Paths.get(pdf.getAbsolutePath());
            if (Files.exists(file)){
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename="+ file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
                Files.delete(pdf.toPath());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public File generateOrder(CartRequest req, long idAddress) throws IOException, DocumentException{
        File pdfFile = generatePdf(req, idAddress);
        byte[] pdfContent = Files.readAllBytes(pdfFile.toPath());
        order = ordersRep.save(
            new Orders(
                order.id(), pdfContent, order.status(), order.date(), order.idUser(), 
                order.idAddress(), order.products()));

        return pdfFile;
    }

    public OrdersResponse getAllOrders(boolean isAdmin){

        if (isAdmin) {
            List<OrderDTO> pending = ordersRep.findAllOrdersAsc(StatusOrderList.PENDING.name());
            List<OrderDTO> complete = ordersRep.findAllOrdersDesc(StatusOrderList.COMPLETE.name());
            List<OrderDTO> cancel = ordersRep.findAllOrdersDesc(StatusOrderList.CANCEL.name());

            return new OrdersResponse(pending, complete, cancel);
        }
        
        Users theUser = userSer.getUserAuthenticated();
        List<OrderDTO> pending = ordersRep.findOrdersAsc(StatusOrderList.PENDING.name(), theUser.id());
        List<OrderDTO> complete = ordersRep.findOrdersDesc(StatusOrderList.COMPLETE.name(), theUser.id());
        List<OrderDTO> cancel = ordersRep.findOrdersDesc(StatusOrderList.CANCEL.name(), theUser.id());

        return new OrdersResponse(pending, complete, cancel);
    }

    public byte[] getPdfOrder(long idOrder, boolean isAdmin){
        Orders theOrder = ordersRep.findPdfBytes(idOrder);
        if (!isAdmin) {
            Users theUser = userSer.getUserAuthenticated();
            if (!theUser.id().equals(theOrder.idUser())) throw new IncorrectException("Not found");
        }
        return theOrder.orderFile();
    }

    public MessageDTO updateStatus(Long id, StatusOrderList newStatus){
        Orders theOrder = ordersRep.findById(id)
            .orElseThrow(() -> new IncorrectException("Order not found"));
        ordersRep.save(new Orders(theOrder.id(), theOrder.orderFile(), newStatus, theOrder.date(), theOrder.idUser(), theOrder.idAddress(), theOrder.products()));

        Users userAdmin = userSer.getUserAuthenticated();
        Users customerUser = userSer.getUser(theOrder.idUser());

        String logMessage= 
            "El usuario cambio el estado del pedido del cliente "
            +customerUser.firstName()+" "+customerUser.lastName()+
            " de "+theOrder.status().name()+" a "+newStatus.name();

        ActivityLogs log = new ActivityLogs(null, logMessage, LocalDateTime.now(), userAdmin.id());
        activityLogsRep.save(log);

        return new MessageDTO("Status Order updated");
    }

    private File generatePdf(CartRequest req, long idAddress) throws IOException, DocumentException{
        Context context = getOrderContext(req, idAddress);
        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }

    private Context getOrderContext(CartRequest req, long idAddress){

        user = admin ? userSer.getUser(req.idUser()) : userSer.getUserAuthenticated();
        
        order = Utils.setOrder(StatusOrderList.PENDING, LocalDateTime.now(), user.id(), idAddress, req, ordersRep);

        Addresses address = Utils.getAddress(idAddress, addressesRep);

        MunicipalyCity municipaly = Utils.getMunicipalyCity(address.idMunicipalyCity(), municipalyRep);

        Departaments departament = Utils.getDepartament(municipaly.id_departament(), departamentsRep);

        CartResponse cartResponse = productsSer.getShoppingCartProducts(req, admin, true);
        
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String hour = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        OrderFormat orderData = 
            new OrderFormat(
                order.id(), user.firstName(), user.lastName(), address.phone(), date, hour, departament.name(), 
                municipaly.name(), address.address(), null, cartResponse.data(), 
                String.format("%,.2f", cartResponse.total()));

        Context context = new Context();
        context.setVariable(orderName, orderData);
        return context;
    }


    private String loadAndFillTemplate(Context context) {
        return ste.process(orderName, context);
    }

    private File renderPdf(String html) throws IOException, DocumentException{
        File file = File.createTempFile(orderName, ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    private String orderName = "order";
}
