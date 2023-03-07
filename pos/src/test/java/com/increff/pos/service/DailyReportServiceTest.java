package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.*;
import com.increff.pos.pojo.*;
import com.increff.pos.util.Helper;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void testGetByDate() {
        DailyReportPojo dailyReportPojo = TestHelper.createDailyReportPojo(ZonedDateTime.now(),5,10,650.45);
        dailyReportDao.insert(dailyReportPojo);

        DailyReportPojo dailyReportPojo1 = dailyReportService.getByDate(dailyReportPojo.getDate());
        assertEquals(dailyReportPojo.getTotalRevenue(),dailyReportPojo1.getTotalRevenue());
    }

    @Test
    public void testGetAll() {
        DailyReportPojo dailyReportPojo = TestHelper.createDailyReportPojo(ZonedDateTime.now(),5,10,650.45);
        dailyReportDao.insert(dailyReportPojo);

        List<DailyReportPojo> dailyReportPojos = dailyReportService.getAll();
        assertEquals(1,dailyReportPojos.size());
    }

    @Test
    public void testGetAllByDate(){
        for(int i = 1; i<=4; i++){
            DailyReportPojo dailyReportPojo = TestHelper.createDailyReportPojo(ZonedDateTime.now().minusDays(i-1),5+i,10+i,650.45+i);
            dailyReportDao.insert(dailyReportPojo);
        }
        List<DailyReportPojo> dailyReportPojos = dailyReportService.getAllByDate(ZonedDateTime.now().minusDays(2).minusMinutes(2),ZonedDateTime.now().plusMinutes(2));
        assertEquals(3,dailyReportPojos.size());
    }
}
