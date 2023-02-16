package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportApiController {


    @Autowired
    private ReportDto dto;


    @ApiOperation(value = "Fetch Brand Report")
    @RequestMapping(path = "/api/brandReport", method = RequestMethod.POST)
    public List<BrandData> getBrandReport(@RequestBody BrandForm brandForm) throws ApiException {
        return dto.getBrandReport(brandForm);
    }


    @ApiOperation(value = "Fetch Inventory Report")
    @RequestMapping(path = "/api/inventoryReport", method = RequestMethod.POST)
    public List<InventoryReportData> getInventoryReport(@RequestBody BrandForm brandForm) throws ApiException {
        return dto.getInventoryReport(brandForm);
    }

    @ApiOperation(value = "Fetch Sales Report")
    @RequestMapping(path = "/api/salesReport", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm salesReportForm) throws ApiException {
        return dto.getSalesReport(salesReportForm);
    }

    @ApiOperation(value = "Fetch Daily Report by date range")
    @RequestMapping(path = "/api/dailyReport", method = RequestMethod.POST)
    public List<DailyReportData> getDailyReport(@RequestBody DailyReportForm dailyReportForm) throws ApiException {
        return dto.getDailyReport(dailyReportForm);
    }

}
