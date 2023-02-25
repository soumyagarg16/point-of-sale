package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.BrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api")
public class BrandApiController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a Brand-Category pair")
    @RequestMapping(path = "/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm brandForm) throws ApiException {
        dto.add(brandForm);
    }

    @ApiOperation(value = "Adds list of brands")
    @RequestMapping(path = "/brands", method = RequestMethod.POST)
    public void addAll(@RequestBody List<BrandForm> brandForms) throws ApiException {
        dto.addAll(brandForms);
    }

    @ApiOperation(value = "Gets the Brand-Category pair by id")
    @RequestMapping(path = "/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody BrandForm brandForm) throws ApiException {
        dto.update(id,brandForm);
    }


}
