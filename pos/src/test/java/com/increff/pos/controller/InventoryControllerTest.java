package com.increff.pos.controller;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.controller.InventoryApiController;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryControllerTest extends AbstractUnitTest {
    @Autowired
    InventoryApiController inventoryApiController;
    @Autowired
    BrandDao brandDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    InventoryDao inventoryDao;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1",10.50);
        productDao.insert(productPojo);

        InventoryForm inventoryForm = TestHelper.createInventoryForm("x1",10);
        inventoryApiController.add(inventoryForm);
        InventoryPojo inventoryPojo = inventoryDao.selectAll().get(0);
        assertEquals(productPojo.getId(), inventoryPojo.getId());
        assertEquals(new Integer(10), inventoryPojo.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testAddWithoutProductExistence() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        InventoryForm inventoryForm = TestHelper.createInventoryForm("x1",10);
        inventoryApiController.add(inventoryForm);
    }

    @Test(expected = ApiException.class)
    public void testAddLimitExceed() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1",10.50);
        productDao.insert(productPojo);

        InventoryForm inventoryForm = TestHelper.createInventoryForm("x1",100);
        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojo.getId(), 999999999);
        inventoryDao.insert(inventoryPojo);
        inventoryApiController.add(inventoryForm);

    }

    @Test
    public void addAllTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        List<ProductPojo> productPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(), "p"+i,10.50+i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }
        List<InventoryForm> inventoryFormList = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryForm inventoryForm = TestHelper.createInventoryForm("x"+i,10+i);
            inventoryFormList.add(inventoryForm);
        }

        inventoryApiController.addAll(inventoryFormList);
        List<InventoryPojo> inventoryPojos = inventoryDao.selectAll();
        for(int i=1; i<=3; i++){
            assertEquals(productPojos.get(i-1).getId(), inventoryPojos.get(i-1).getId());
            assertEquals(new Integer(10+i), inventoryPojos.get(i-1).getQuantity());
        }
    }

    @Test(expected = ApiException.class)
    public void testAddAllWithoutProductExistence() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryForm inventoryForm = TestHelper.createInventoryForm("x"+i,10+i);
            inventoryFormList.add(inventoryForm);
        }
        inventoryApiController.addAll(inventoryFormList);

    }

    @Test(expected = ApiException.class)
    public void testAddAllWithDuplicateBarcode() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryForm inventoryForm = TestHelper.createInventoryForm("x"+i,10+i);
            inventoryFormList.add(inventoryForm);
        }
        InventoryForm inventoryForm = TestHelper.createInventoryForm("x3",10);
        inventoryFormList.add(inventoryForm);
        inventoryApiController.addAll(inventoryFormList);

    }

    @Test(expected = ApiException.class)
    public void testLargeFileAddAll() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        List<ProductPojo> productPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(), "p"+i,10.50+i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }
        List<InventoryForm> inventoryFormList = new ArrayList<>();
        for(int i=1; i<=5001; i++){
            InventoryForm inventoryForm = TestHelper.createInventoryForm("x"+i,10+i);
            inventoryFormList.add(inventoryForm);
        }

        inventoryApiController.addAll(inventoryFormList);
    }
    @Test
    public void getTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1",10.50);
        productDao.insert(productPojo);

        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojo.getId(),10);
        inventoryDao.insert(inventoryPojo);
        InventoryData existing = inventoryApiController.get(inventoryPojo.getId());
        assertEquals(productPojo.getId(), existing.getId());
        assertEquals("x1",existing.getBarcode());
        assertEquals(new Integer(10),existing.getQuantity());
    }

    @Test
    public void getAllTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b","c");
        brandDao.insert(brandPojo);

        List<ProductPojo> productPojos = new ArrayList<>();
        for(Integer i = 1; i<=5;i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(),"p"+i,10.50+i);
            productPojos.add(productPojo);
            productDao.insert(productPojo);
        }

        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for(Integer i = 1; i<=5;i++){
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i-1).getId(),10+i);
            inventoryDao.insert(inventoryPojo);
            inventoryPojos.add(inventoryPojo);

        }

        List<InventoryData> inventoryDataList = inventoryApiController.getAll();
        Integer i =1;
        for(InventoryData inventoryData: inventoryDataList){
            assertEquals(productPojos.get(i-1).getId(), inventoryData.getId());
            assertEquals("x"+i,inventoryData.getBarcode());
            assertEquals(new Integer(10+i),inventoryData.getQuantity());
            i++;
        }

    }

    @Test
    public void updateTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1",15.40);
        productDao.insert(productPojo);
        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojo.getId(),10);
        inventoryDao.insert(inventoryPojo);

        InventoryForm inventoryForm = TestHelper.createInventoryForm("x1",12);
        inventoryApiController.update(inventoryPojo.getId(), inventoryForm);
        assertEquals(productPojo.getId(),inventoryPojo.getId());
        assertEquals(new Integer(12),inventoryPojo.getQuantity());
    }

}
