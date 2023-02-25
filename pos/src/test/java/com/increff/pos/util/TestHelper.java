package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import org.junit.jupiter.api.Order;

import java.time.ZonedDateTime;

public class TestHelper {
    public static BrandForm createBrandForm(String brand, String category){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }
    public static BrandPojo createBrandPojo(String brand, String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }
    public static ProductForm createProductForm(String brand, String category, String barcode, String name, Double mrp){
        ProductForm productForm = new ProductForm();
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setBarcode(barcode);
        productForm.setName(name);
        productForm.setMrp(mrp);
        return productForm;
    }
    public static ProductPojo createProductPojo(String barcode, Integer bcId,  String name, Double mrp){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(barcode);
        productPojo.setBrand_category(bcId);
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        return productPojo;
    }

    public static InventoryForm createInventoryForm(String barcode, Integer quantity){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }

    public static InventoryPojo createInventoryPojo(Integer id, Integer quantity){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(id);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    public static OrderItemForm createOrderItemForm(String barcode, Integer quantity, Double sellingPrice){
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(barcode);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setSellingPrice(sellingPrice);
        return orderItemForm;
    }
    public static OrderItemPojo createOrderItemPojo(Integer orderId,Integer productId, Integer quantity, Double sellingPrice){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setSellingPrice(sellingPrice);
        return orderItemPojo;
    }

    public static OrderPojo createOrderPojo(String time){
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime(time);
        orderPojo.setIsInvoiceGenerated(0);
        return orderPojo;
    }

    public static SalesReportForm createSalesReportForm(String brand, String category, String startDate, String endDate){
        SalesReportForm salesReportForm = new SalesReportForm();
        salesReportForm.setBrand(brand);
        salesReportForm.setCategory(category);
        salesReportForm.setStartDate(startDate);
        salesReportForm.setEndDate(endDate);
        return salesReportForm;
    }

    public static DailyReportForm createDailyReportForm(String startDate, String endDate){
        DailyReportForm dailyReportForm = new DailyReportForm();
        dailyReportForm.setStartDate(startDate);
        dailyReportForm.setEndDate(endDate);
        return dailyReportForm;
    }

    public static DailyReportPojo createDailyReportPojo(ZonedDateTime dt, Integer orderCount, Integer itemCount, Double rev){
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(dt);
        dailyReportPojo.setInvoiced_orders_count(orderCount);
        dailyReportPojo.setInvoiced_items_count(itemCount);
        dailyReportPojo.setTotal_revenue(rev);
        return dailyReportPojo;
    }

    public static UserForm createUserForm(String email, String pass, String role){
        UserForm userForm = new UserForm();
        userForm.setEmail(email);
        userForm.setPassword(pass);
        userForm.setRole(role);
        return userForm;
    }
    public static UserPojo createUserPojo(String email, String pass, String role){
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(email);
        userPojo.setPassword(pass);
        userPojo.setRole(role);
        return userPojo;
    }

}
