package com.increff.pos.dto;

import com.google.protobuf.Api;
import com.increff.pos.AbstractUnitTest;
import com.increff.pos.controller.ReportApiController;
import com.increff.pos.dao.*;
import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportControllerTest extends AbstractUnitTest {

    @Autowired
    BrandDao brandDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    InventoryDao inventoryDao;
    @Autowired
    ReportApiController reportApiController;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    DailyReportDao dailyReportDao;


    @Test
    public void getBrandReportOnNullFormTest() throws ApiException {
        for(int i=1; i<=3; i++){
            BrandPojo brandPojo = TestHelper.createBrandPojo("b"+i,"c"+i);
            brandDao.insert(brandPojo);
        }
        BrandForm brandForm = TestHelper.createBrandForm("","");
        List<BrandData> brandDataList = reportApiController.getBrandReport(brandForm);
        assertEquals(3,brandDataList.size());
    }

    @Test
    public void getBrandReportTest() throws ApiException {
        for(int i=1; i<=3; i++){
            BrandPojo brandPojo = TestHelper.createBrandPojo("b"+i,"c"+i);
            brandDao.insert(brandPojo);
        }
        BrandForm brandForm = TestHelper.createBrandForm("b2","c2");
        List<BrandData> brandDataList = reportApiController.getBrandReport(brandForm);
        assertEquals(1,brandDataList.size());
    }

    @Test
    public void getInventoryReportTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        List<ProductPojo> productPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(), "p"+i,10.50+i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }

        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i-1).getId(),10+i);
            inventoryPojoList.add(inventoryPojo);
            inventoryDao.insert(inventoryPojo);
        }
        BrandForm brandForm = TestHelper.createBrandForm("b1","");
        List<InventoryReportData> inventoryReportDataList = reportApiController.getInventoryReport(brandForm);
        assertEquals(1,inventoryReportDataList.size());
        assertEquals(new Integer(11+12+13),inventoryReportDataList.get(0).getQuantity());
    }

    @Test
    public void getSalesReportTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        List<ProductPojo> productPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(), "p"+i,10.50+i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }

        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i-1).getId(),10+i);
            inventoryPojoList.add(inventoryPojo);
            inventoryDao.insert(inventoryPojo);
        }
        SalesReportForm salesReportForm = TestHelper.createSalesReportForm("b1","","2023-02-01","2023-02-21");
        LocalDateTime ldt = LocalDateTime.now();
        OrderPojo orderPojo =  TestHelper.createOrderPojo(ldt.toString());
        orderDao.insert(orderPojo);
        for(int i=1; i<=3; i++){
            OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(),productPojos.get(i-1).getId(),5+i,10.50);
            orderItemDao.insert(orderItemPojo);
        }
        List<SalesReportData> salesReportDatas = reportApiController.getSalesReport(salesReportForm);
        assertEquals(1,salesReportDatas.size());
    }

    @Test
    public void getDailyReportTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        List<ProductPojo> productPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(), "p"+i,10.50+i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }

        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i-1).getId(),10+i);
            inventoryPojoList.add(inventoryPojo);
            inventoryDao.insert(inventoryPojo);
        }
        DailyReportForm dailyReportForm = TestHelper.createDailyReportForm("2023-02-01","2023-02-21");
        LocalDateTime ldt = LocalDateTime.now();
        OrderPojo orderPojo =  TestHelper.createOrderPojo(ldt.toString());
        orderDao.insert(orderPojo);
        for(int i=1; i<=3; i++){
            OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(),productPojos.get(i-1).getId(),5+i,10.50);
            orderItemDao.insert(orderItemPojo);
        }
        ZonedDateTime dt = ZonedDateTime.now();
        DailyReportPojo dailyReportPojo = TestHelper.createDailyReportPojo(dt,1,3,40.0);
        dailyReportDao.insert(dailyReportPojo);
        List<DailyReportData> dailyReportDatas = reportApiController.getDailyReport(dailyReportForm);
        assertEquals(1,dailyReportDatas.size());
    }

}
