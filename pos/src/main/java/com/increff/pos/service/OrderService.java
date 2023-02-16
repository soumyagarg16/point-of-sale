package com.increff.pos.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.model.InvoiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderDao dao;

    public void add(OrderPojo p) throws ApiException {
        dao.insert(p);
    }

    public void delete(Integer id) throws ApiException {
        getCheck(id);
        dao.delete(id);
    }

    public OrderPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    public List<OrderPojo> getAllByDate(String startDate, String endDate){
        return dao.selectAllByDate(startDate,endDate);
    }

    public List<OrderPojo> getAll() {
        return dao.selectAll();
    }

    private OrderPojo getCheck(Integer id) throws ApiException {
        OrderPojo orderPojo = dao.select(id);
        if (orderPojo == null) {
            throw new ApiException("No order exists with the given id " + id);
        }
        return orderPojo;
    }



}
