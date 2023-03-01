package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {

    @Autowired
    private ProductDao dao;

    public void add(ProductPojo productPojo) throws ApiException {
        ProductPojo existingPojo = getProductPojoByBarcode(productPojo.getBarcode());
        if(existingPojo==null){
            dao.insert(productPojo);
        }
        else throw new ApiException("Product with this barcode already exists!");
    }

    public void addAll(List<ProductPojo> productPojos) throws ApiException {

        int count = 1;
        List<String> errors = new ArrayList<>();
        //check for existing barcode
        for(ProductPojo productPojo: productPojos){
            ProductPojo existingPojo = getProductPojoByBarcode(productPojo.getBarcode());
            if(existingPojo!=null){
                errors.add("Product already exists with barcode "+ productPojo.getBarcode() + " in row "+count);
            }
            count++;
        }
        if(!errors.isEmpty()){
            throw new ApiException(Helper.convertListToString(errors));
        }
        for(ProductPojo productPojo: productPojos){
            dao.insert(productPojo);
        }
    }

    public ProductPojo get(Integer id) throws ApiException {
        return dao.select(id);
    }

    public List<ProductPojo> getAll() throws ApiException {
        return dao.selectAll();
    }

    public void update(Integer id, ProductPojo productPojo) throws ApiException {
        ProductPojo toBeChangedPojo = get(id);
        ProductPojo existingPojo = getProductPojoByBarcode(productPojo.getBarcode());
        if(existingPojo==null || Objects.equals(existingPojo.getId(), id)){
            toBeChangedPojo.setBarcode(productPojo.getBarcode());
            toBeChangedPojo.setMrp(productPojo.getMrp());
            toBeChangedPojo.setName(productPojo.getName());
            toBeChangedPojo.setBrandCategory(productPojo.getBrandCategory());
        }
        else throw new ApiException("Given Barcode already exists!");
    }

    public ProductPojo getProductPojoByBarcode(String barcode){
        return dao.select(barcode);
    }

    public List<ProductPojo> getAllByBrandCategoryId(Integer id){
        return dao.selectAll(id);
    }

 //TODO wRong method
    public ProductPojo getCheck(ProductPojo productPojo) throws ApiException {
        if (productPojo == null) {
            throw new ApiException("No Product exists with this id");
        }
        return productPojo;
    }



}
