package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DailyReportServiceTest extends AbstractUnitTest {
    @Autowired
    BrandDao brandDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    InventoryDao inventoryDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    DailyReportService dailyReportService;
    @Autowired
    DailyReportDao dailyReportDao;

    @Test
    public void testAdd() throws ApiException {
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
        ZonedDateTime date = ZonedDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = date.format(formatter);
        System.out.println(currentDate.toString());
        OrderPojo orderPojo =  TestHelper.createOrderPojo(currentDate.toString()+" 10:00:00");
        orderDao.insert(orderPojo);
        for(int i=1; i<=3; i++){
            OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(),productPojos.get(i-1).getId(),5+i,10.50);
            orderItemDao.insert(orderItemPojo);
        }
        dailyReportService.add();

        List<DailyReportPojo> dailyReportPojos = dailyReportDao.selectAll();
        assertEquals(1,dailyReportPojos.size());
    }

    @Test
    public void testGetAll() throws ApiException {

        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(ZonedDateTime.now());
        dailyReportPojo.setInvoiced_orders_count(5);
        dailyReportPojo.setInvoiced_items_count(10);
        dailyReportPojo.setTotal_revenue(650.45);
        dailyReportDao.insert(dailyReportPojo);

        List<DailyReportPojo> dailyReportPojos = dailyReportService.getAll();
        assertEquals(1,dailyReportPojos.size());
    }

    @Test
    public void testGetAllByDate(){
        for(int i = 1; i<=4; i++){
            DailyReportPojo dailyReportPojo = new DailyReportPojo();
            dailyReportPojo.setDate(ZonedDateTime.now().minusDays(i-1));
            dailyReportPojo.setInvoiced_orders_count(5+i);
            dailyReportPojo.setInvoiced_items_count(10+i);
            dailyReportPojo.setTotal_revenue(650.45+i);
            dailyReportDao.insert(dailyReportPojo);
        }
        List<DailyReportPojo> dailyReportPojos = dailyReportService.getAllByDate(ZonedDateTime.now().minusDays(2).minusMinutes(2),ZonedDateTime.now().plusMinutes(2));
        assertEquals(3,dailyReportPojos.size());
    }
}
