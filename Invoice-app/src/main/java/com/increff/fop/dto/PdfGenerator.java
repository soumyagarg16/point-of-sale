package com.increff.fop.dto;

import com.increff.fop.model.InvoiceData;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Base64;

@Service
public class PdfGenerator {
    @Autowired
    JavaToXml javaToXml;
    public String xmlToPdfConverter(InvoiceData invoiceData) {
        try {
            File xmlfile = new File("src/main/resources/invoice.xml");
            File xsltfile = new File("src/main/resources/style.xsl");

            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                Source src = new StreamSource(xmlfile);
                Result res = new SAXResult(fop.getDefaultHandler());

                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                e.printStackTrace();
            } finally {
                byte[] pdf = out.toByteArray();
                String base64 = Base64.getEncoder().encodeToString(pdf);
                return base64;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return "";
    }
}

