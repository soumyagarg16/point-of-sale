package com.increff.fop.controller;

import com.increff.fop.dto.JavaToXml;
import com.increff.fop.dto.PdfGenerator;
import com.increff.fop.model.InvoiceData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class pdfController {
    @Autowired
    private JavaToXml javaToXml;

    @Autowired
    private PdfGenerator pdfGenerator;

    @ApiOperation(value = "Add list of brand")
    @RequestMapping(path = "/fop", method = RequestMethod.POST)
    public void classToPDF(@RequestBody InvoiceData invoiceData) {
        javaToXml.javaToXmlConverter(invoiceData);
        pdfGenerator.xmlToPdfConverter(invoiceData);
    }


}