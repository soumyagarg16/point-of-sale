package com.increff.employee.dto;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.util.Helper;
import com.increff.employee.util.StringUtil;
import com.increff.employee.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BrandDto {
    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        Validate.validateBrandForm(brandForm);
        BrandPojo brandPojo = Helper.convertBrandFormToPojo(brandForm);
        brandService.add(brandPojo);
    }


    public void addAll(List<BrandForm> brandForms) throws ApiException {

        if(brandForms.size()>5000){
            throw new ApiException("File size cannot exceed 5000 rows!");
        }

        List<String> errors = Validate.validateBrandForms(brandForms);
        if(!errors.isEmpty()){
            throw new ApiException(errors.toString());
        }

        List<BrandPojo> brandPojos = new ArrayList<>();
        for(BrandForm brandForm: brandForms){
            BrandPojo brandPojo = Helper.convertBrandFormToPojo(brandForm);
            brandPojos.add(brandPojo);
        }
        brandService.addAll(brandPojos);

    }

    public List<BrandData> getAll(){
        List<BrandPojo> brandPojos = brandService.getAll();
        return Helper.convertBrandPojosToDatas(brandPojos);
    }

    public BrandData get(Integer id) throws ApiException {
        BrandPojo brandPojo = brandService.getById(id);
        return Helper.convert(brandPojo);
    }

    public void update(Integer id, BrandForm brandForm) throws ApiException{
        Validate.validateBrandForm(brandForm);
        brandService.update(id,brandForm);
    }

}