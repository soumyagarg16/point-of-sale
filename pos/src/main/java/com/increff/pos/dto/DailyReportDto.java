package com.increff.pos.dto;

import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DailyReportService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DailyReportDto {

    @Autowired
    private DailyReportService dailyReportService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional(rollbackOn = ApiException.class)
    public void add() throws ApiException {
        ZonedDateTime date = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = date.format(formatter);
        List<OrderPojo> orderPojos = orderService.getAllByDate(currentDate + " 00:00:00", currentDate + " 23:59:59");
        double revenue = 0.0;
        int itemCount = 0;

        for (OrderPojo orderPojo : orderPojos) {
            List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(orderPojo.getId());
            itemCount += orderItemPojos.size();
            revenue += orderItemPojos.stream().mapToDouble(orderItemPojo -> orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice()).sum();
        }

        DailyReportPojo dailyReportPojo = Helper.createDailyReportPojo(date, orderPojos.size(), itemCount, revenue);
        dailyReportService.add(dailyReportPojo);
    }
}

