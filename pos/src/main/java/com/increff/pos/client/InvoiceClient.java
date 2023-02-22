package com.increff.pos.client;
import com.increff.pos.model.InvoiceData;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class InvoiceClient {
    //todo you autowired rest template but it shows error
    private RestTemplate restTemplate;

    public String generateInvoice(InvoiceData invoiceData){
        //ResponseEntity<String> apiResponse = restTemplate.postForEntity("http://localhost:8000/fop/api/pdf", invoiceData, String.class);
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> httpEntity = new HttpEntity<>(invoiceData,headers);

        System.out.println(httpEntity.getBody());
        System.out.println(httpEntity);
        String invoice_base64 = restTemplate.exchange("http://localhost:8000/fop/api/pdf",HttpMethod.POST,httpEntity,String.class).getBody();
        return invoice_base64;
    }

}
