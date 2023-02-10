package com.increff.employee.controller;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import com.increff.employee.dto.InventoryDto;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {

    @Autowired
    private InventoryDto dto;

    @ApiOperation(value = "Adds Inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm inventoryForm) throws ApiException {
        dto.add(inventoryForm);
    }

    @ApiOperation(value = "Upload Inventory")
    @RequestMapping(path = "/api/inventorys", method = RequestMethod.POST)
    public void addAll(@RequestBody List<InventoryForm> inventoryForms) throws ApiException {
        dto.addAll(inventoryForms);
    }


    @ApiOperation(value = "Gets the inventory by id")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException {
        return dto.get(id);

    }

    @ApiOperation(value = "Gets list of whole inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return dto.getAll();

    }

    @ApiOperation(value = "Updates Inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody InventoryForm inventoryForm) throws ApiException {
        dto.update(id,inventoryForm);
    }





}
