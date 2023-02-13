package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyReportData{
    String date;
    Integer invoicedOrderCount;
    Integer invoicedItemCount;
    Double totalRevenue;
}
