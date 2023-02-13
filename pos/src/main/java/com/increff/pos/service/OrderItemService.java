package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;


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
