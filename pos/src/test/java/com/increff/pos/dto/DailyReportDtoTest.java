package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DailyReportDtoTest extends AbstractUnitTest {
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
    DailyReportDto dailyReportDto;
    @Autowired
    DailyReportDao dailyReportDao;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1", "c1");
        brandDao.insert(brandPojo);
        List<ProductPojo> productPojos = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            ProductPojo productPojo = TestHelper.createProductPojo("x" + i, brandPojo.getId(), "p" + i, 10.50 + i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }

        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i - 1).getId(), 10 + i);
            inventoryPojoList.add(inventoryPojo);
            inventoryDao.insert(inventoryPojo);
        }
        ZonedDateTime date = ZonedDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = date.format(formatter);
        OrderPojo orderPojo = TestHelper.createOrderPojo(currentDate + " 10:00:00");
        orderDao.insert(orderPojo);
        for (int i = 1; i <= 3; i++) {
            OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(), productPojos.get(i - 1).getId(), 5 + i, 10.50);
            orderItemDao.insert(orderItemPojo);
        }
        dailyReportDto.add();

        List<DailyReportPojo> dailyReportPojos = dailyReportDao.selectAll();
        assertEquals(1, dailyReportPojos.size());
    }

    @Test
    public void testAddExisting() throws ApiException {
        ZonedDateTime dt = ZonedDateTime.now();
        dt = dt.truncatedTo(ChronoUnit.DAYS);
        DailyReportPojo dailyReportPojo = TestHelper.createDailyReportPojo(dt, 2, 5, 1000.00);
        dailyReportDao.insert(dailyReportPojo);

        BrandPojo brandPojo = TestHelper.createBrandPojo("b1", "c1");
        brandDao.insert(brandPojo);
        List<ProductPojo> productPojos = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            ProductPojo productPojo = TestHelper.createProductPojo("x" + i, brandPojo.getId(), "p" + i, 10.50 + i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }

        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i - 1).getId(), 10 + i);
            inventoryPojoList.add(inventoryPojo);
            inventoryDao.insert(inventoryPojo);
        }
        ZonedDateTime date = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = date.format(formatter);
        OrderPojo orderPojo = TestHelper.createOrderPojo(currentDate + " 10:00:00");
        orderDao.insert(orderPojo);
        for (int i = 1; i <= 3; i++) {
            OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(), productPojos.get(i - 1).getId(), 5 + i, 10.50);
            orderItemDao.insert(orderItemPojo);
        }
        dailyReportDto.add();
        List<DailyReportPojo> dailyReportPojos = dailyReportDao.selectAll();
        assertEquals(1, dailyReportPojos.size());
    }

}
