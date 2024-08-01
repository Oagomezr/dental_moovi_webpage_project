package com.dentalmoovi.website.services;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
import com.dentalmoovi.website.repositories.EnterprisesRep;
import com.dentalmoovi.website.repositories.OrdersRep;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;

@Service
public class OrdersSer {
    private final OrdersRep ordersRep;
    private final ProductsSer productsSer;
    private final AddressesRep addressesRep;
    private final DepartamentsRep departamentsRep;
    private final MunicipalyRep municipalyRep;
    private final UserSer userSer;
    private final ActivityLogsRep activityLogsRep;
    private final RestTemplate restTemplate;
    private final EnterprisesRep enterprisesRep;
    private final OrdersSer orderSer;

    @Value("${server.orderService}")
    private String orderServiceUrl;
    private boolean admin = false;

    Orders order;
    Users user;
    
    public OrdersSer(OrdersRep ordersRep, ProductsSer productsSer, AddressesRep addressesRep,
            DepartamentsRep departamentsRep, MunicipalyRep municipalyRep, UserSer userSer,
            ActivityLogsRep activityLogsRep, RestTemplate restTemplate, EnterprisesRep enterprisesRep,
            @Lazy OrdersSer orderSer) {
        this.ordersRep = ordersRep;
        this.productsSer = productsSer;
        this.addressesRep = addressesRep;
        this.departamentsRep = departamentsRep;
        this.municipalyRep = municipalyRep;
        this.userSer = userSer;
        this.activityLogsRep = activityLogsRep;
        this.restTemplate = restTemplate;
        this.enterprisesRep = enterprisesRep;
        this.orderSer = orderSer;
    }

    public MessageDTO generateOrder(CartRequest req, long idAddress, boolean admin){
        this.admin = admin;
        orderSer.generatePdf(req, idAddress); //Call async methods via an injected dependency instead of directly via 'this'.sonarlint(java:S6809)
        return new MessageDTO("Order generated successfully");
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

        ActivityLogs log = new ActivityLogs(null, logMessage, Utils.getNow(), userAdmin.id());
        activityLogsRep.save(log);

        return new MessageDTO("Status Order updated");
    }

    @Async("taskExecutor")
    public void generatePdf(CartRequest req, long idAddress) /* throws IOException, DocumentException */{
        OrderFormat orderData = getOrderData(req, idAddress);
        restTemplate.postForEntity(orderServiceUrl+"/generate", orderData, Void.class);
    }

    private OrderFormat getOrderData(CartRequest req, long idAddress){

        user = admin ? userSer.getUser(req.idUser()) : userSer.getUserAuthenticated();
        
        order = Utils.setOrder(StatusOrderList.PENDING, Utils.getNow(), user.id(), idAddress, req, ordersRep);

        Addresses address = Utils.getAddress(idAddress, addressesRep);

        MunicipalyCity municipaly = Utils.getMunicipalyCity(address.idMunicipalyCity(), municipalyRep);

        Departaments departament = Utils.getDepartament(municipaly.id_departament(), departamentsRep);

        CartResponse cartResponse = productsSer.getShoppingCartProducts(req, admin, true);
        
        String date = Utils.getNow().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String hour = Utils.getNow().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        String enterpriseName = user.idEnterprise() != null ? Utils.getEnterprise(user.idEnterprise(), enterprisesRep).name() : "";
        
        return new OrderFormat(
                order.id(), user.firstName(), user.lastName(), address.phone(), date, hour, departament.name(), 
                municipaly.name(), address.address(), enterpriseName, cartResponse.data(), 
                String.format("%,.2f", cartResponse.total()));
    }
}
