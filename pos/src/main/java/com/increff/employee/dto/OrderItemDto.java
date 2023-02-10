package com.increff.employee.dto;

import com.increff.employee.model.*;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.*;
import com.increff.employee.util.Helper;
import com.increff.employee.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    public void addAll(List<OrderItemForm> orderItemForms) throws ApiException {
        if(orderItemForms.isEmpty()){
            throw new ApiException("Cannot place an empty order!");
        }
        List<String> errors = Validate.validateOrderItemForms(orderItemForms);
        //Creating an order id
        if(!errors.isEmpty()){
            throw new ApiException(errors.toString());
        }
        OrderPojo orderPojo = Helper.createOrderPojo();
        orderService.add(orderPojo);
        //Converting OrderItemForms to pojos
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemForms){
            OrderItemPojo orderItemPojo = AddOrderItem(orderItemForm,errors);
            orderItemPojo.setOrderId(orderPojo.getId());
            orderItemPojos.add(orderItemPojo);
        }

        if(errors.isEmpty()){
            orderItemService.addAll(orderItemPojos);
        }
        else{
            throw new ApiException(errors.toString());
        }
    }

    public List<OrderItemData> getAllByOrderId(Integer id) throws ApiException{
        orderService.get(id);
        List<OrderItemPojo> orderItemPojos = orderItemService.getAll(id);
        List<OrderItemData> orderItemDatas = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            OrderItemData orderItemData = Helper.convertOrderItemPojoToData(orderItemPojo);
            orderItemData.setBarcode(productService.get(orderItemPojo.getProductId()).getBarcode());
            orderItemDatas.add(orderItemData);
        }
        return orderItemDatas;
    }

    public List<OrderData> getAllOrders(){
        List<OrderPojo> orderPojos = orderService.getAll();
        List<OrderData> orderDatas = new ArrayList<>();
        for(OrderPojo orderPojo: orderPojos){
            OrderData orderData = new OrderData();
            orderData.setId(orderPojo.getId());
            orderData.setTime(orderPojo.getTime());
            orderDatas.add(orderData);
        }
        return orderDatas;
    }

    //Existing product and inventory check
    private OrderItemPojo AddOrderItem(OrderItemForm orderItemForm, List<String> errors) throws ApiException{
        OrderItemPojo orderItemPojo = Helper.convertOrderItemFormToPojo(orderItemForm);
        ProductPojo productPojo = productService.getProductPojoByBarcode(orderItemForm.getBarcode());
        if(productPojo==null){
            errors.add("No Product exists with the given barcode: " + orderItemForm.getBarcode());
        }
        else{
            orderItemPojo.setProductId(productPojo.getId());
            InventoryPojo inventoryPojo = inventoryService.getInventoryPojoById(productPojo.getId());
            if(inventoryPojo==null || inventoryPojo.getQuantity()<orderItemForm.getQuantity()){
                errors.add("Product with the barcode " + productPojo.getBarcode() + " has insufficient inventory!");
            }
            else
                orderItemPojo.setQuantity(orderItemForm.getQuantity());
                inventoryPojo.setQuantity(inventoryPojo.getQuantity()-orderItemForm.getQuantity());
        }
        return orderItemPojo;

    }
}
