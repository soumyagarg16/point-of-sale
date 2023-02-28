package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.Helper;
import com.increff.pos.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;


    public void add(ProductForm productForm) throws ApiException {
        Validate.validateProductForm(productForm);
        ProductPojo productPojo = Helper.convertProductFormToPojo(productForm);
        BrandPojo brandPojo = brandService.getByBrandCategory(productForm.getBrand(), productForm.getCategory());
        if(brandPojo==null){
            throw new ApiException("The given brand-category pair does not exist.");
        }
        productPojo.setBrand_category(brandPojo.getId());
        productService.add(productPojo);
    }

    public void addAll(List<ProductForm> productForms) throws ApiException {
        if(productForms.size()>5000){
            throw new ApiException("File size cannot exceed 5000 rows!");
        }

        List<String> errors = Validate.validateProductForms(productForms);
        if(!errors.isEmpty()){
            throw new ApiException(Helper.convertListToString(errors));
        }

        List<ProductPojo> productPojos = new ArrayList<>();
        Integer count = 1;
        //check if given brand category exists
        for(ProductForm productForm: productForms){
            BrandPojo brandPojo = brandService.getByBrandCategory(productForm.getBrand(), productForm.getCategory());
            if(brandPojo==null){
                errors.add("Brand "+productForm.getBrand()+" and category "+productForm.getCategory()+" pair does not exist for row "+count);
            }
            else{
                ProductPojo productPojo = Helper.convertProductFormToPojo(productForm);
                productPojo.setBrand_category(brandPojo.getId());
                productPojos.add(productPojo);
            }
            count++;
        }
        if(!errors.isEmpty()){
            throw new ApiException(Helper.convertListToString(errors));
        }
        productService.addAll(productPojos);
    }

    @Transactional
    public ProductData get(Integer id) throws ApiException {
        ProductPojo productPojo = productService.getCheck(productService.get(id));
        ProductData productData = Helper.convertProductPojoToData(productPojo);
        productData.setBrand(brandService.getById(productPojo.getBrand_category()).getBrand());
        productData.setCategory(brandService.getById(productPojo.getBrand_category()).getCategory());
        return productData;
    }

    @Transactional
    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> productPojos = productService.getAll();
        List<ProductData> productDatas = new ArrayList<>();
        for(ProductPojo productPojo: productPojos){
            ProductData productData = Helper.convertProductPojoToData(productPojo);
            productData.setBrand(brandService.getById(productPojo.getBrand_category()).getBrand());
            productData.setCategory(brandService.getById(productPojo.getBrand_category()).getCategory());
            productDatas.add(productData);
        }
         return productDatas;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, ProductForm productForm) throws ApiException {
        Validate.validateProductForm(productForm);
        BrandPojo brandPojo = brandService.getByBrandCategory(productForm.getBrand(),productForm.getCategory());
        if(brandPojo==null){
            throw new ApiException("Given brand-category pair does not exist!");
        }
        ProductPojo productPojo = Helper.convertProductFormToPojo(productForm);
        productPojo.setBrand_category(brandPojo.getId());
        productService.update(id,productPojo);

    }







}
