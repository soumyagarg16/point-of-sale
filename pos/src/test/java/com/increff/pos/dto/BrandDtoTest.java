package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
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


public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    BrandDto brandDto;
    @Autowired
    BrandDao brandDao;

    @Test
    public void NormalizeAddTest() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm(" brAnd 1", "   caTegOry 5  ");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandDao.selectAll().get(0);
        assertEquals("brand 1", brandPojo.getBrand());
        assertEquals("category 5", brandPojo.getCategory());
    }

    @Test(expected = ApiException.class)
    public void EmptyBrandAddTest() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm("","cat1");
        brandDto.add(brandForm);
    }

    @Test(expected = ApiException.class)
    public void EmptyCategoryAddTest() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm("br1","");
        brandDto.add(brandForm);
    }

    @Test(expected = ApiException.class)
    public void DuplicateAddTest() throws ApiException {
        BrandForm brandForm = TestHelper.createBrandForm("b1","c1");
        brandDto.add(brandForm);
        brandDto.add(brandForm);
    }

    @Test
    public void AddAllTest() throws ApiException {
        List<BrandForm> brandForms = new ArrayList<>();
        for(int i = 1; i<=5;i++){
            BrandForm brandForm = TestHelper.createBrandForm("b"+i,"c"+i);
            brandForms.add(brandForm);
        }
        brandDto.addAll(brandForms);
        List<BrandPojo> brandPojos = brandDao.selectAll();
        int i = 1;
        for(BrandPojo brandPojo: brandPojos){
            assertEquals("b"+i, brandPojo.getBrand());
            assertEquals("c"+i, brandPojo.getCategory());
            i++;
        }
    }

    @Test
    public void getTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        BrandData existing = brandDto.get(brandPojo.getId());
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
        List<BrandData> brandDataList = brandDto.getAll();
        int i = 1;
        for(BrandData brandData: brandDataList){
            assertEquals("b"+i,brandData.getBrand());
            assertEquals("c"+i,brandData.getCategory());
            i++;
        }
    }

    @Test
    public void UpdateTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        BrandForm brandForm = TestHelper.createBrandForm("b2","c2");
        brandDao.insert(brandPojo);
        brandDto.update(brandPojo.getId(),brandForm);
        assertEquals("b2",brandPojo.getBrand());
        assertEquals("c2",brandPojo.getCategory());
    }



}
