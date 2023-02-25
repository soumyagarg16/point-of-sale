package com.increff.pos.controller;

import java.io.IOException;
import java.util.List;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.model.*;

import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/order")
public class OrderApiController {
    @Autowired
    private OrderItemDto dto;

    @ApiOperation(value = "Creates the Customer order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void addAll(@RequestBody List<OrderItemForm> orderItemForms) throws ApiException {
        dto.addAll(orderItemForms);
    }

    // Will be called when clicked on view orders
    @ApiOperation(value = "Gets the order items by order id")
    @RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getAllByOrderId(@PathVariable Integer orderId) throws ApiException {
        return dto.getAllByOrderId(orderId);
    }

    @ApiOperation(value = "Gets all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() throws ApiException {
        return dto.getAllOrders();
    }

    @ApiOperation(value = "Generate invoice by orderId")
    @RequestMapping(path = "/{orderId}/generate", method = RequestMethod.GET)
    public String generateInvoice(@PathVariable Integer orderId) throws ApiException, IOException {
        return dto.generateInvoice(orderId);
    }
}
