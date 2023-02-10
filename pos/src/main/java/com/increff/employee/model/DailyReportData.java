package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
public class DailyReportData{
    String date;
    Integer invoicedOrderCount;
    Integer invoicedItemCount;
    Double totalRevenue;
}
