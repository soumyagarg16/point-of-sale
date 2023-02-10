package com.increff.employee.pojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "pos_day_sales")
public class DailyReportPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    ZonedDateTime date;
    Integer invoiced_orders_count;
    Integer invoiced_items_count;
    Double total_revenue;

}
