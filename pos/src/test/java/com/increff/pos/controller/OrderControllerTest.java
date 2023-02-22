package com.increff.pos.controller;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.controller.OrderApiController;
import com.increff.pos.dao.*;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderControllerTest extends AbstractUnitTest {
    @Autowired
    OrderApiController orderApiController;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    InventoryDao inventoryDao;

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
        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i-1).getId(),10+i);
            inventoryDao.insert(inventoryPojo);
            inventoryPojos.add(inventoryPojo);
        }

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        for(int i =1; i<=3; i++){
            OrderItemForm orderItemForm = TestHelper.createOrderItemForm("x"+i,10+i,10.50+i);
            orderItemFormList.add(orderItemForm);
        }
        orderApiController.addAll(orderItemFormList);
        List<OrderPojo> orderPojos = orderDao.selectAll();
        List<OrderItemPojo> orderItemPojos = orderItemDao.selectAll(orderPojos.get(0).getId());
        int i = 1;
        assertEquals(3,orderItemPojos.size());
    }

    @Test
    public void getAllByOrderIdTest() throws ApiException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        List<ProductPojo> productPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(), "p"+i,10.50+i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }
        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i-1).getId(),10+i);
            inventoryDao.insert(inventoryPojo);
            inventoryPojos.add(inventoryPojo);
        }
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime("2023-02-16");
        orderPojo.setIsInvoiceGenerated(0);
        orderDao.insert(orderPojo);

        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for(int i =1; i<=3; i++){
            OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(), productPojos.get(i-1).getId(),10+i,10.50+i);
            orderItemDao.insert(orderItemPojo);
            orderItemPojos.add(orderItemPojo);
        }
        List<OrderItemData> orderItemDataList = orderApiController.getAllByOrderId(orderPojo.getId());
        assertEquals(3,orderItemDataList.size());
    }

    @Test
    public void getAllOrdersTest() throws ApiException, InterruptedException {
        for(int i=1; i<=3;i++){
            String time = LocalDateTime.now().toString();
            OrderPojo orderPojo = TestHelper.createOrderPojo(time);
            orderDao.insert(orderPojo);
            Thread.sleep(2000);
        }
        List<OrderData> orderDataList = orderApiController.getAllOrders();
        assertEquals(3,orderDataList.size());

    }

    @Test
    public void testGenerateInvoice() throws ApiException, IOException {
        BrandPojo brandPojo = TestHelper.createBrandPojo("b1","c1");
        brandDao.insert(brandPojo);

        List<ProductPojo> productPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            ProductPojo productPojo = TestHelper.createProductPojo("x"+i, brandPojo.getId(), "p"+i,10.50+i);
            productDao.insert(productPojo);
            productPojos.add(productPojo);
        }
        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productPojos.get(i-1).getId(),10+i);
            inventoryDao.insert(inventoryPojo);
            inventoryPojos.add(inventoryPojo);
        }
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime("2023-02-22 11:00:00");
        orderPojo.setIsInvoiceGenerated(0);
        orderDao.insert(orderPojo);

        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for(int i =1; i<=3; i++){
            OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderPojo.getId(), productPojos.get(i-1).getId(),10+i,10.50+i);
            orderItemDao.insert(orderItemPojo);
            orderItemPojos.add(orderItemPojo);
        }
        String base64 = "";
        base64 = orderApiController.generateInvoice(orderPojo.getId());
        assertNotEquals("",base64);
    }

}
