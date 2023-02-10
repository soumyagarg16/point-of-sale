package com.increff.employee.service;

import com.increff.employee.dao.DailyReportDao;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DailyReportService {
    @Autowired
    DailyReportDao dao;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @Scheduled(cron = "01 00 00 * * *")
    @Transactional(rollbackOn = ApiException.class)
    public void add() throws ApiException {
        //TODO add minusdays(1) before delivering final code.
        ZonedDateTime date = ZonedDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = date.format(formatter);
        List<OrderPojo> orderPojos = orderService.getAllByDate(currentDate+" 00:00:00",currentDate+" 23:59:59");
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(date); // Setting Date
        dailyReportPojo.setInvoiced_orders_count(orderPojos.size()); // Setting order count
        Double revenue = 0.0;
        Integer itemCount = 0;

        for(OrderPojo orderPojo: orderPojos){
            List<OrderItemPojo> orderItemPojos = orderItemService.getAll(orderPojo.getId());
            itemCount += orderItemPojos.size();
            for(OrderItemPojo orderItemPojo: orderItemPojos) {
                revenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
            }
        }
        dailyReportPojo.setInvoiced_items_count(itemCount);
        dailyReportPojo.setTotal_revenue(revenue);

        dao.insert(dailyReportPojo);
    }

    public List<DailyReportPojo> getAll(){
        return dao.selectAll();
    }

    public List<DailyReportPojo> getAllByDate(ZonedDateTime startDate, ZonedDateTime endDate){
        return dao.selectAllByDate(startDate, endDate);
    }
}
