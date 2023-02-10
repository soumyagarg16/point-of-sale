package com.increff.employee.controller;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.increff.employee.dto.OrderItemDto;
import com.increff.employee.model.*;

import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemApiController {

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

    @ApiOperation(value = "get Invoice of an order by Id")
    @RequestMapping(path = "/get-invoice/{orderId}", method = RequestMethod.GET)
    public void update(@PathVariable Integer orderId) throws ApiException, IOException {
        dto.getInvoice(orderId);
    }




}
