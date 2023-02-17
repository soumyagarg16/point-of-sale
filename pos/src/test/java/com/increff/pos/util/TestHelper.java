package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.*;
import org.junit.jupiter.api.Order;

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

}
