package com.increff.employee.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import com.increff.employee.model.InvoiceData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.OrderItemPojo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderDao dao;

    public void add(OrderPojo p) throws ApiException {
        dao.insert(p);
    }

    public void delete(Integer id) throws ApiException {
        getCheck(id);
        dao.delete(id);
    }

    public OrderPojo get(Integer id) throws ApiException {
        OrderPojo orderPojo = dao.select(id);
        if(orderPojo==null){
            throw new ApiException("No order exists with the given id");
        }
        return orderPojo;
    }

    public Integer get(String time) throws ApiException {
        OrderPojo p = dao.select(time);
        if(p==null){
            throw new ApiException("No order exists in the given time.");
        }
        return p.getId();
    }

    public List<OrderPojo> getAllByDate(String startDate, String endDate){
        return dao.selectAllByDate(startDate,endDate);
    }

    public List<OrderPojo> getAll() {
        return dao.selectAll();
    }

    public OrderPojo getCheck(Integer id) throws ApiException {
        OrderPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("No order exists with the given id: " + id);
        }
        return p;
    }

    private static String generateInvoicePdf(Integer orderId, String base64) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64.getBytes());
        File pdfDir = new File("src/main/resources/PdfFiles");
        pdfDir.mkdirs();
        String pdfFileName = "invoice_" + orderId + ".pdf";
        File pdfFile = new File(pdfDir, pdfFileName);
        FileOutputStream fos = new FileOutputStream(pdfFile);
        fos.write(decodedBytes);
        fos.flush();
        fos.close();

        return pdfDir + "/" + pdfFileName;
    }

    public void getPDFBase64(Integer orderId, InvoiceData invoiceData) throws ApiException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String apiUrl = "http://localhost:8000/fop/api/fop";
        RestTemplate RestTemplate = new RestTemplate();
        ResponseEntity<String> apiResponse = RestTemplate.postForEntity(apiUrl, invoiceData, String.class);
        String base64 = apiResponse.getBody();
//        InvoicePojo invoicePojo = new InvoicePojo();
//        invoicePojo.setOrderId(orderId);
//        invoicePojo.setInvoiceLocation(generateInvoicePdf(orderId, base64));
//        invoiceDao.insert(invoicePojo);
        generateInvoicePdf(orderId,base64);
    }


}
