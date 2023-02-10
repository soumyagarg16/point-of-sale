package com.increff.employee.util;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.ProductForm;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.model.UserForm;
import com.increff.employee.pojo.UserPojo;

public class Normalize {
    public static void normalize(BrandForm brandForm){
        brandForm.setBrand(StringUtil.toLowerCase(brandForm.getBrand()));
        brandForm.setCategory(StringUtil.toLowerCase(brandForm.getCategory()));
    }
    public static void normalize(ProductForm productForm){
        productForm.setBrand(StringUtil.toLowerCase(productForm.getBrand()));
        productForm.setCategory(StringUtil.toLowerCase(productForm.getCategory()));
        productForm.setName(StringUtil.toLowerCase(productForm.getName()));
    }
    public static void normalizeDouble(ProductForm productForm){
        productForm.setMrp(Math.floor(productForm.getMrp() * 100) / 100);
    }

    public static void normalizeDateTime(SalesReportForm salesReportForm){
        salesReportForm.setStartDate(salesReportForm.getStartDate()+ " 00:00:00");
        salesReportForm.setEndDate(salesReportForm.getEndDate()+ " 23:59:59");
    }

    public static void normalizeEmail(UserPojo userPojo){
        userPojo.setEmail(userPojo.getEmail().toLowerCase().trim());
        userPojo.setRole(userPojo.getRole().toLowerCase().trim());
    }

    public static void normalizeUserForm(UserForm userForm){
        userForm.setEmail(StringUtil.toLowerCase(userForm.getEmail()));
        userForm.setRole(StringUtil.toLowerCase(userForm.getRole()));

    }
}
