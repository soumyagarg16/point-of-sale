package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.model.OrderItemData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.util.StringUtil;


@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void addAll(List<OrderItemPojo> orderItemPojos) throws ApiException {
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            dao.insert(orderItemPojo);
        }
    }

    //Get all items by orderId
    public List<OrderItemPojo> getAll(Integer id) {
        return dao.selectAll(id);
    }


}
