package com.increff.pos.service;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DailyReportService {
    @Autowired
    DailyReportDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(DailyReportPojo dailyReportPojo) throws ApiException {
        DailyReportPojo existing = getByDate(dailyReportPojo.getDate());
        if (existing == null) {
            dao.insert(dailyReportPojo);
        } else
            update(existing, dailyReportPojo);
    }

    public DailyReportPojo getByDate(ZonedDateTime date) {
        return dao.selectByDate(date);
    }

    public List<DailyReportPojo> getAll() {
        return dao.selectAll();
    }

    public List<DailyReportPojo> getAllByDate(ZonedDateTime startDate, ZonedDateTime endDate) {
        return dao.selectAllByDate(startDate, endDate);
    }

    public void update(DailyReportPojo existing, DailyReportPojo updates) {
        existing.setInvoicedOrdersCount(updates.getInvoicedOrdersCount());
        existing.setInvoicedItemsCount(updates.getInvoicedItemsCount());
        existing.setTotalRevenue(updates.getTotalRevenue());
    }
}
