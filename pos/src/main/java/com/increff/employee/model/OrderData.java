package com.increff.employee.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {

    private Integer id;
    private String time;
    private Integer isInvoiceGenerated;
}
