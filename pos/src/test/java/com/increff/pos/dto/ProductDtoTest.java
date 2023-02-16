package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    ProductDto productDto;
    @Autowired
    ProductDao productDao;
    @Autowired
    BrandDao brandDao;

    @Test
    public void addTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("brand10","category10");
        brandDao.insert(brandPojo);
        ProductForm productForm = TestHelper.createProductForm(" BraNd10 ", "CatEgory10  ","xD12","p1",50.512);
        productDto.add(productForm);
        ProductPojo productPojo = productDao.selectAll().get(0);
        assertEquals(brandPojo.getId(), productPojo.getBrand_category());
        assertEquals("xD12",productPojo.getBarcode());
        assertEquals("p1",productPojo.getName());
        assertEquals(50.51,productPojo.getMrp(),0.01);
    }

    @Test
    public void AddAllTest() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for(int i = 1; i<=5;i++){
            BrandPojo brandPojo = TestHelper.createBrandPojo("b"+i,"c"+i);
            brandDao.insert(brandPojo);
            brandPojos.add(brandPojo);
        }
        List<ProductForm> productForms = new ArrayList<>();
        for(int i = 1; i<=5;i++){
            ProductForm productForm = TestHelper.createProductForm("b"+i,"c"+i,"x"+i,"p"+i,10.50+i);
            productForms.add(productForm);
        }
        productDto.addAll(productForms);
        List<ProductPojo> productPojos = productDao.selectAll();
        int i = 1;
        for(ProductPojo productPojo: productPojos){
            assertEquals(brandPojos.get(i - 1).getId(), productPojo.getBrand_category());
            assertEquals("x"+i, productPojo.getBarcode());
            assertEquals("p"+i, productPojo.getName());
            assertEquals(10.50+i, productPojo.getMrp(),0.0);
            i++;
        }
    }

    @Test
    public void getTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1",50.50);
        productDao.insert(productPojo);
        ProductData existing = productDto.get(productPojo.getId());
        assertEquals("b1",existing.getBrand());
        assertEquals("c1",existing.getCategory());
        assertEquals("x1",existing.getBarcode());
        assertEquals("p1",existing.getName());
        assertEquals(50.50,existing.getMrp(),0.0);
        assertEquals(brandPojo.getId(),existing.getBrand_category());

    }

    @Test
    public void getAllTest() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for(int i = 1; i<=5;i++){
            BrandPojo brandPojo = TestHelper.createBrandPojo("b"+i,"c"+i);
            brandDao.insert(brandPojo);
            brandPojos.add(brandPojo);
        }

        List<ProductPojo> productPojos = new ArrayList<>();
        for(Integer i = 1; i<=5;i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojos.get(i-1).getId(),"p"+i,10.50+i);
            productPojos.add(productPojo);
            productDao.insert(productPojo);
        }
        List<ProductData> productDatas = productDto.getAll();
        Integer i =1;
        for(ProductData productData: productDatas){
            assertEquals("b"+i,productData.getBrand());
            assertEquals("c"+i,productData.getCategory());
            assertEquals(brandPojos.get(i-1).getId(),productData.getBrand_category());
            assertEquals("x"+i,productData.getBarcode());
            assertEquals("p"+i,productData.getName());
            assertEquals(10.50+i,productData.getMrp(),0.0);
            i++;
        }
    }

    @Test
    public void UpdateTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1",15.40);
        ProductForm productForm = TestHelper.createProductForm(brandPojo.getBrand(), brandPojo.getCategory(), "x2","p3",14.5);
        productDto.update(brandPojo.getId(),productForm);
        assertEquals("x2",productPojo.getBarcode());
        assertEquals("p3",productPojo.getName());
        assertEquals(new Double(14.5),productPojo.getMrp(),0.01);
    }

}
