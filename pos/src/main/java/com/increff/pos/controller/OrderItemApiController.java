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
public class OrderItemApiController {
//TODO change name to OrderController
    @Autowired
    private OrderItemDto dto;

    @ApiOperation(value = "Creates the Customer order")
    @RequestMapping(path = "/api/order", method = RequestMethod.POST)
    public void addAll(@RequestBody List<OrderItemForm> orderItemForms) throws ApiException {
        dto.addAll(orderItemForms);
    }

    // Will be called when clicked on view orders
    @ApiOperation(value = "Gets the order items by order id")
    @RequestMapping(path = "/api/order/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getAllByOrderId(@PathVariable Integer orderId) throws ApiException {
        return dto.getAllByOrderId(orderId);
    }

    @ApiOperation(value = "Gets all orders")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() throws ApiException {
        return dto.getAllOrders();
    }

    @ApiOperation(value = "Generate invoice by orderId")
    @RequestMapping(path = "/api/order/{orderId}/generate", method = RequestMethod.GET)
    public void generateInvoice(@PathVariable Integer orderId) throws ApiException{
        dto.generateInvoice(orderId);
    }


    //TODO Remove controller
    @ApiOperation(value = "Download invoice by orderId")
    @RequestMapping(path = "/api/order/{orderId}/download", method = RequestMethod.GET)
    public String downloadInvoice(@PathVariable Integer orderId) throws IOException, ApiException {
        return dto.downloadInvoice(orderId);
    }








}
