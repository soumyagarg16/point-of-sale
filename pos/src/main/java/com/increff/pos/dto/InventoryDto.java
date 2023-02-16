package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Helper;
import com.increff.pos.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public void add(InventoryForm inventoryForm) throws ApiException {
        Validate.validateInventoryForm(inventoryForm);
        InventoryPojo inventoryPojo = Helper.convertInventoryFormToPojo(inventoryForm);
        ProductPojo productPojo = productService.getProductPojoByBarcode(inventoryForm.getBarcode());
        if(productPojo==null){
            throw new ApiException("No product exists with the given barcode!");
        }
        inventoryPojo.setId(productPojo.getId());
        inventoryService.add(inventoryPojo);
    }

    public void addAll(List<InventoryForm> inventoryForms) throws ApiException {
        if(inventoryForms.size()>5000){
            throw new ApiException("File size cannot exceed 5000 rows!");
        }
        List<String> errors = Validate.validateInventoryForms(inventoryForms);
        if(!errors.isEmpty()){
            throw new ApiException(errors.toString());
        }
        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for(InventoryForm inventoryForm: inventoryForms){
            ProductPojo productPojo = productService.getProductPojoByBarcode(inventoryForm.getBarcode());
            if(productPojo==null){
                errors.add("No product exists for the barcode " + inventoryForm.getBarcode());
                continue;
            }
            InventoryPojo inventoryPojo = Helper.convertInventoryFormToPojo(inventoryForm);
            inventoryPojo.setId(productPojo.getId());
            inventoryPojos.add(inventoryPojo);
        }
        if(!errors.isEmpty()){
            throw new ApiException(errors.toString());
        }
        inventoryService.addAll(inventoryPojos);


    }


    public InventoryData get(Integer id) throws ApiException {
        InventoryPojo inventoryPojo  = getCheck(id);
        InventoryData inventoryData = Helper.convertInventoryPojoToData(inventoryPojo);
        inventoryData.setBarcode(productService.get(id).getBarcode());
        return inventoryData;
    }

    public List<InventoryData> getAll() throws ApiException{
        List<InventoryPojo> inventoryPojos = inventoryService.getAll();
        List<InventoryData> inventoryDatas = new ArrayList<>();
        for(InventoryPojo inventoryPojo : inventoryPojos){
            InventoryData inventoryData = Helper.convertInventoryPojoToData(inventoryPojo);
            inventoryData.setBarcode(productService.get(inventoryPojo.getId()).getBarcode());
            inventoryDatas.add(inventoryData);
        }
        return inventoryDatas;
    }

    public void update(Integer id, InventoryForm inventoryForm) throws ApiException {
        Validate.validateInventoryForm(inventoryForm);
        InventoryPojo inventoryPojo = Helper.convertInventoryFormToPojo(inventoryForm);
        ProductPojo productPojo = productService.getProductPojoByBarcode(inventoryForm.getBarcode());
        if(productPojo==null){
            throw new ApiException("No product exists with the given barcode!");
        }
        inventoryPojo.setId(productPojo.getId());
        inventoryService.update(id,inventoryPojo);
    }

    private InventoryPojo getCheck(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getInventoryPojoById(id);
        if(inventoryPojo==null){
            throw new ApiException("No inventory exists for the given product!");
        }
        return inventoryPojo;
    }





}