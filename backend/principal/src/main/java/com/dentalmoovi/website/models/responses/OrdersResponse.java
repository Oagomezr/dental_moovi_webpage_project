package com.dentalmoovi.website.models.responses;

import java.util.List;
import com.dentalmoovi.website.models.dtos.OrderDTO;

public record OrdersResponse(
    List<OrderDTO> pending,
    List<OrderDTO> complete,
    List<OrderDTO> cancel
) {
} 
