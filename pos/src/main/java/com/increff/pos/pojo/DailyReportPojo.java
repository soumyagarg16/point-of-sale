package com.increff.pos.pojo;
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
    private Integer id;
    private ZonedDateTime date;
    //TODO remove snake casing, use camel case
    private Integer invoiced_orders_count;
    private Integer invoiced_items_count;
    private Double total_revenue;

}
