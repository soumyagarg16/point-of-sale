package com.increff.pos.controller;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductControllerTest extends AbstractUnitTest {

    @Autowired
    ProductApiController productApiController;
    @Autowired
    ProductDao productDao;
    @Autowired
    BrandDao brandDao;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("brand10", "category10");
        brandDao.insert(brandPojo);
        ProductForm productForm = TestHelper.createProductForm(" BraNd10 ", "CatEgory10  ", "xD12", "p1", 50.512);
        productApiController.add(productForm);
        ProductPojo productPojo = productDao.selectAll().get(0);
        assertEquals(brandPojo.getId(), productPojo.getBrandCategory());
        assertEquals("xD12", productPojo.getBarcode());
        assertEquals("p1", productPojo.getName());
        assertEquals(50.51, productPojo.getMrp(), 0.01);
    }

    @Test(expected = ApiException.class)
    public void testAddWithoutBrandExistence() throws ApiException {
        ProductForm productForm = TestHelper.createProductForm("brand1", "cat1", "xD12", "p1", 50.512);
        productApiController.add(productForm);

    }

    @Test(expected = ApiException.class)
    public void testAddExistingBarcode() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("brand1", "cat1");
        brandDao.insert(brandPojo);

        ProductForm productForm = TestHelper.createProductForm("brand1", "cat1", "xD12", "p1", 50.512);
        productApiController.add(productForm);

        ProductForm productForm2 = TestHelper.createProductForm("brand1", "cat1", "xD12", "p1", 50.512);
        productApiController.add(productForm2);

    }

    @Test
    public void testAddAll() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            BrandPojo brandPojo = TestHelper.createBrandPojo("b" + i, "c" + i);
            brandDao.insert(brandPojo);
            brandPojos.add(brandPojo);
        }
        List<ProductForm> productForms = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ProductForm productForm = TestHelper.createProductForm("b" + i, "c" + i, "x" + i, "p" + i, 10.50 + i);
            productForms.add(productForm);
        }
        productApiController.addAll(productForms);
        List<ProductPojo> productPojos = productDao.selectAll();
        int i = 1;
        for (ProductPojo productPojo : productPojos) {
            assertEquals(brandPojos.get(i - 1).getId(), productPojo.getBrandCategory());
            assertEquals("x" + i, productPojo.getBarcode());
            assertEquals("p" + i, productPojo.getName());
            assertEquals(10.50 + i, productPojo.getMrp(), 0.0);
            i++;
        }
    }

    @Test(expected = ApiException.class)
    public void testAddAllEmptyBarcode() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            BrandPojo brandPojo = TestHelper.createBrandPojo("b" + i, "c" + i);
            brandDao.insert(brandPojo);
            brandPojos.add(brandPojo);
        }
        List<ProductForm> productForms = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ProductForm productForm = TestHelper.createProductForm("b" + i, "c" + i, " ", "p" + i, 10.50 + i);
            productForms.add(productForm);
        }
        productApiController.addAll(productForms);
    }

    @Test(expected = ApiException.class)
    public void testAddAllExistingBarcode() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            BrandPojo brandPojo = TestHelper.createBrandPojo("b" + i, "c" + i);
            brandDao.insert(brandPojo);
            brandPojos.add(brandPojo);
        }
        List<ProductForm> productForms = new ArrayList<>();
        ProductPojo productPojo = TestHelper.createProductPojo("x2",brandPojos.get(0).getId(),"pp",123.50);
        productDao.insert(productPojo);
        for (int i = 1; i < 5; i++) {
            ProductForm productForm = TestHelper.createProductForm("b" + i, "c" + i, "x"+i, "p" + i, 10.50 + i);
            productForms.add(productForm);
        }
        productApiController.addAll(productForms);
    }

    @Test(expected = ApiException.class)
    public void testAddAllWithoutBrandExisting() throws ApiException {
        List<ProductForm> productForms = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ProductForm productForm = TestHelper.createProductForm("b" + i, "c" + i, "x" + i, "p" + i, 10.50 + i);
            productForms.add(productForm);
        }
        productApiController.addAll(productForms);
    }

    @Test(expected = ApiException.class)
    public void testAddLargeFileTest() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            BrandPojo brandPojo = TestHelper.createBrandPojo("b" + i, "c" + i);
            brandDao.insert(brandPojo);
            brandPojos.add(brandPojo);
        }
        List<ProductForm> productForms = new ArrayList<>();
        for (int i = 1; i <= 5001; i++) {
            ProductForm productForm = TestHelper.createProductForm("b" + i, "c" + i, "x" + i, "p" + i, 10.50 + i);
            productForms.add(productForm);
        }
        productApiController.addAll(productForms);
    }

    @Test
    public void getTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1", "c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1", 50.50);
        productDao.insert(productPojo);
        ProductData existing = productApiController.get(productPojo.getId());
        assertEquals("b1", existing.getBrand());
        assertEquals("c1", existing.getCategory());
        assertEquals("x1", existing.getBarcode());
        assertEquals("p1", existing.getName());
        assertEquals(50.50, existing.getMrp(), 0.0);
        assertEquals(brandPojo.getId(), existing.getBrandCategory());

    }

    @Test
    public void getAllTest() throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            BrandPojo brandPojo = TestHelper.createBrandPojo("b" + i, "c" + i);
            brandDao.insert(brandPojo);
            brandPojos.add(brandPojo);
        }

        List<ProductPojo> productPojos = new ArrayList<>();
        for (Integer i = 1; i <= 5; i++) {
            ProductPojo productPojo = TestHelper.createProductPojo("x" + i, brandPojos.get(i - 1).getId(), "p" + i, 10.50 + i);
            productPojos.add(productPojo);
            productDao.insert(productPojo);
        }
        List<ProductData> productDatas = productApiController.getAll();
        Integer i = 1;
        for (ProductData productData : productDatas) {
            assertEquals("b" + i, productData.getBrand());
            assertEquals("c" + i, productData.getCategory());
            assertEquals(brandPojos.get(i - 1).getId(), productData.getBrandCategory());
            assertEquals("x" + i, productData.getBarcode());
            assertEquals("p" + i, productData.getName());
            assertEquals(10.50 + i, productData.getMrp(), 0.0);
            i++;
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1", "c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1", 15.40);
        productDao.insert(productPojo);
        ProductForm productForm = TestHelper.createProductForm(brandPojo.getBrand(), brandPojo.getCategory(), "x2", "p3", 14.5);
        productApiController.update(productPojo.getId(), productForm);
        assertEquals("x2", productPojo.getBarcode());
        assertEquals("p3", productPojo.getName());
        assertEquals(new Double(14.5), productPojo.getMrp(), 0.01);
    }

    @Test(expected = ApiException.class)
    public void testUpdateWithoutBrandExistence() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1", "c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1", 15.40);
        productDao.insert(productPojo);
        ProductForm productForm = TestHelper.createProductForm("b10", brandPojo.getCategory(), "x2", "p3", 14.5);
        productApiController.update(productPojo.getId(), productForm);
    }

    @Test(expected = ApiException.class)
    public void testUpdateWithExistingBarcode() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1", "c1");
        brandDao.insert(brandPojo);
        ProductPojo productPojo = TestHelper.createProductPojo("x1", brandPojo.getId(), "p1", 15.40);
        productDao.insert(productPojo);
        ProductPojo productPojo2 = TestHelper.createProductPojo("x2", brandPojo.getId(), "p1", 15.40);
        productDao.insert(productPojo2);
        ProductForm productForm = TestHelper.createProductForm(brandPojo.getBrand(), brandPojo.getCategory(), "x2", "p3", 14.5);
        productApiController.update(productPojo.getId(), productForm);
    }

}
