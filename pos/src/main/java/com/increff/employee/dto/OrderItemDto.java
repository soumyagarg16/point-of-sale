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
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
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

    @Transactional(rollbackOn = ApiException.class)
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

    @Transactional(rollbackOn = ApiException.class)
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
            orderData.setIsInvoiceGenerated(orderPojo.getIsInvoiceGenerated());
            orderDatas.add(orderData);
        }
        return orderDatas;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void generateInvoice(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderService.get(orderId);
        if(orderPojo.getIsInvoiceGenerated()==1){
            throw new ApiException("Invoice can only be created once!");
        }
        List<OrderItemPojo> orderItemPojos = orderItemService.getAll(orderId);
        InvoiceData invoiceData = new InvoiceData();

        List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
        Integer sno = 1;
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            invoiceLineItem.setSno(sno++);
            invoiceLineItem.setBarcode(productPojo.getBarcode());
            invoiceLineItem.setName(productPojo.getName());
            invoiceLineItem.setQuantity(orderItemPojo.getQuantity());
            invoiceLineItem.setSellingPrice(orderItemPojo.getSellingPrice());
            invoiceLineItem.setTotal(invoiceLineItem.getTotal());
            invoiceLineItems.add(invoiceLineItem);
        }
        String[] time = orderPojo.getTime().split(" ");
        invoiceData.setInvoiceNumber("order_"+orderPojo.getId());
        invoiceData.setInvoiceDate(time[0]);
        invoiceData.setInvoiceTime(time[1]);
        invoiceData.setLineItems(invoiceLineItems);
        invoiceData.setTotal(invoiceData.getTotal());

        String base64;
        try{
            RestTemplate restTemplate = new RestTemplate();
            base64 = restTemplate.postForEntity("http://localhost:8000/fop/api/pdf", invoiceData, String.class).getBody();
        } catch (Exception e){
            throw new ApiException("Unable to create invoice as invoice-app is not running.");
        }

        File pdfDir = new File("src/main/resources./pdf");
        pdfDir.mkdirs();
        String pdfFileName = "invoice_" + invoiceData.getInvoiceNumber() + ".pdf";
        File file = new File(pdfDir, pdfFileName);


        try (FileOutputStream fos = new FileOutputStream(file) ) {
            byte[] decoder = Base64.getDecoder().decode(base64);

            fos.write(decoder);
            System.out.println("PDF File Saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderPojo.setIsInvoiceGenerated(1);
    }

    @Transactional(rollbackOn = ApiException.class)
    public String downloadInvoice(Integer orderId) throws IOException, ApiException {
        OrderPojo orderPojo = orderService.get(orderId);
        if(orderPojo.getIsInvoiceGenerated()==0){
            throw new ApiException("Error downloading Invoice!");
        }

        String filePath = "src/main/resources/pdf/invoice_order_"+orderId + ".pdf";
        File file = new File(filePath);
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();
        return Base64.getEncoder().encodeToString(bytesArray);
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
            else{
                orderItemPojo.setQuantity(orderItemForm.getQuantity());
                inventoryPojo.setQuantity(inventoryPojo.getQuantity()-orderItemForm.getQuantity());
            }

        }
        return orderItemPojo;

    }


}
