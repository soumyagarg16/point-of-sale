package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.util.StringUtil;


@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

    @Autowired
    private InventoryDao dao;

    public void add(InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo existingPojo = getInventoryPojoById(inventoryPojo.getId());
        if(existingPojo==null){
            dao.insert(inventoryPojo);
       }
       else{
           existingPojo.setQuantity(inventoryPojo.getQuantity()+existingPojo.getQuantity());
       }
    }
    public void addAll(List<InventoryPojo> inventoryPojos) throws ApiException{
        for(InventoryPojo inventoryPojo: inventoryPojos){
            add(inventoryPojo);
        }
    }

    public void delete(Integer id) throws ApiException {
        getCheck(id);
        dao.delete(id);
    }


    public InventoryPojo getInventoryPojoById(Integer id) throws ApiException {
        return dao.select(id);
    }

    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }


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
