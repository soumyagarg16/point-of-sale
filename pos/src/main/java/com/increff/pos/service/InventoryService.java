package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;


@Service
public class InventoryService {

    @Autowired
    private InventoryDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo existingPojo = getInventoryPojoById(inventoryPojo.getId());
        if(existingPojo==null){
            dao.insert(inventoryPojo);
       }
       else{
           Integer qty = inventoryPojo.getQuantity()+existingPojo.getQuantity();
           if(qty>10000000){
               throw new ApiException("Quantity cannot exceed 10000000");
           }
           existingPojo.setQuantity(qty);
       }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addAll(List<InventoryPojo> inventoryPojos) throws ApiException{
        for(InventoryPojo inventoryPojo: inventoryPojos){
            add(inventoryPojo);
        }
    }

    public InventoryPojo getInventoryPojoById(Integer id){
        return dao.select(id);
    }

    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo existingPojo = getCheck(id);
        existingPojo.setQuantity(inventoryPojo.getQuantity());

    }

    public InventoryPojo getCheck(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = dao.select(id);
        if (inventoryPojo == null) {
            throw new ApiException("No Inventory exists with the given id");
        }
        return inventoryPojo;
    }



}
