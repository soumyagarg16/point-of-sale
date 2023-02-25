package com.increff.pos.controller;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.controller.BrandApiController;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BrandControllerTest extends AbstractUnitTest {

    @Autowired
    BrandApiController brandApiController;

    @Autowired
    BrandDao brandDao;

    @Test
    public void testAddBrand() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm(" brAnd 1", "   caTegOry 5  ");
        brandApiController.add(brandForm);
        BrandPojo brandPojo = brandDao.selectAll().get(0);
        assertEquals("brand 1", brandPojo.getBrand());
        assertEquals("category 5", brandPojo.getCategory());
    }

    @Test(expected = ApiException.class)
    public void emptyBrandAddTest() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm("","cat1");
        brandApiController.add(brandForm);
    }

    @Test(expected = ApiException.class)
    public void emptyCategoryAddTest() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm("br1","");
        brandApiController.add(brandForm);
    }

    @Test(expected = ApiException.class)
    public void DuplicateAddTest() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm("b1","c1");
        brandApiController.add(brandForm);
        brandApiController.add(brandForm);
    }

    @Test
    public void addAllTest() throws ApiException {
        List<BrandForm> brandForms = new ArrayList<>();
        for(int i = 1; i<=5;i++){
            BrandForm brandForm = TestHelper.createBrandForm("b"+i,"c"+i);
            brandForms.add(brandForm);
        }
        brandApiController.addAll(brandForms);
        List<BrandPojo> brandPojos = brandDao.selectAll();
        int i = 1;
        for(BrandPojo brandPojo: brandPojos){
            assertEquals("b"+i, brandPojo.getBrand());
            assertEquals("c"+i, brandPojo.getCategory());
            i++;
        }
    }

    @Test(expected = ApiException.class)
    public void addAllLargeFileTest() throws ApiException {
        List<BrandForm> brandForms = new ArrayList<>();
        for(int i = 1; i<=5001;i++){
            BrandForm brandForm = TestHelper.createBrandForm("b"+i,"c"+i);
            brandForms.add(brandForm);
        }
        brandApiController.addAll(brandForms);
    }

    @Test(expected = ApiException.class)
    public void addAllInvalidBrandTest() throws ApiException {
        List<BrandForm> brandForms = new ArrayList<>();
        for(int i = 1; i<=2;i++){
            BrandForm brandForm = TestHelper.createBrandForm("","c"+i);
            brandForms.add(brandForm);
        }
        brandApiController.addAll(brandForms);
    }


    @Test
    public void getTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        BrandData existing = brandApiController.get(brandPojo.getId());
        assertEquals("b1",existing.getBrand());
        assertEquals("c1",existing.getCategory());
    }

    @Test
    public void getAllTest() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for(int i=1; i<=5; i++){
            BrandPojo brandPojo = TestHelper.createBrandPojo("b"+i,"c"+i);
            brandDao.insert(brandPojo);
        }
        List<BrandData> brandDataList = brandApiController.getAll();
        int i = 1;
        for(BrandData brandData: brandDataList){
            assertEquals("b"+i,brandData.getBrand());
            assertEquals("c"+i,brandData.getCategory());
            i++;
        }
    }

    @Test
    public void updateTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        BrandForm brandForm = TestHelper.createBrandForm("b2","c2");
        brandDao.insert(brandPojo);
        brandApiController.update(brandPojo.getId(),brandForm);
        assertEquals("b2",brandPojo.getBrand());
        assertEquals("c2",brandPojo.getCategory());
    }



}
