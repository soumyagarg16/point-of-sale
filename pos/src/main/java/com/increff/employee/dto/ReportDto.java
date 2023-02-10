package com.increff.employee.dto;

import com.increff.employee.model.*;
import com.increff.employee.pojo.*;
import com.increff.employee.service.*;
import com.increff.employee.util.Helper;
import com.increff.employee.util.Normalize;
import com.increff.employee.util.StringUtil;
import com.increff.employee.util.Validate;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DailyReportService dailyReportService;

    public List<BrandData> getBrandReport(BrandForm brandForm) throws ApiException {
        Normalize.normalize(brandForm);
        if(brandForm.getBrand().isEmpty() && brandForm.getCategory().isEmpty()){
            return brandDto.getAll();
        }
        else{
            List<BrandPojo> brandPojos = brandService.getAllByBrandCategory(brandForm.getBrand(), brandForm.getCategory());
            return Helper.convertBrandPojosToDatas(brandPojos);
        }
    }

    public List<InventoryReportData> getInventoryReport(BrandForm brandForm) throws ApiException {
        List<BrandData> brandDatas = getBrandReport(brandForm); // List of desirable brand-category pairs
        List<InventoryReportData> inventoryReportDatas = new ArrayList<>();
        for(BrandData brandData: brandDatas){
            List<ProductPojo> productPojos = productService.getAllByBrandCategoryId(brandData.getId());// Get all products with the same brand-cat
            Integer qty = 0;
            boolean flag = false;

            for(ProductPojo productPojo: productPojos){
                InventoryPojo inventoryPojo = inventoryService.getInventoryPojoById(productPojo.getId());
                if(inventoryPojo!=null){
                    qty+=inventoryPojo.getQuantity();
                    flag = true;
                }
            }
            if(flag){
                InventoryReportData inventoryReportData = new InventoryReportData();
                inventoryReportData.setBrand(brandData.getBrand());
                inventoryReportData.setCategory(brandData.getCategory());
                inventoryReportData.setQuantity(qty);
                inventoryReportDatas.add(inventoryReportData);
            }
        }

        return inventoryReportDatas;
    }

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException{
        Validate.validateSalesReportForm(salesReportForm);
        Normalize.normalizeDateTime(salesReportForm);
        List<SalesReportData> salesReportDatas = new ArrayList<>();

        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(salesReportForm.getBrand());
        brandForm.setCategory(salesReportForm.getCategory());
        List<BrandData> brandDatas = getBrandReport(brandForm); // List of desirable brand-category pairs
        if(brandDatas.isEmpty()){
            throw new ApiException("No order has been placed for the given brand-category in the date range!");
        }

        Set<Integer> brandCategorySet = setOfBrandCategoryId(brandDatas);

        List<OrderPojo> orderPojos = orderService.getAllByDate(salesReportForm.getStartDate(),salesReportForm.getEndDate());// desirable orders
        if(orderPojos.isEmpty()){
            throw new ApiException("No order exists in the given range!");
        }
        Map<Integer,SalesReportData> map = new HashMap<>();//map {brandCatId : {brand,category,quantity,revenue}}
        for(OrderPojo orderPojo: orderPojos){
            List<OrderItemPojo> orderItemPojos = orderItemService.getAll(orderPojo.getId()); // Items with same orderID
            for(OrderItemPojo orderItemPojo: orderItemPojos){
                ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                Integer key = productPojo.getBrand_category();
                if(brandCategorySet.contains(key)){
                    SalesReportData salesReportData;
                    if(map.containsKey(key)){
                        salesReportData = map.get(key);
                        salesReportData.setQuantity(salesReportData.getQuantity()+orderItemPojo.getQuantity());
                        salesReportData.setRevenue(salesReportData.getRevenue()+(orderItemPojo.getQuantity()*orderItemPojo.getSellingPrice()));
                    }
                    else{
                        salesReportData = new SalesReportData();
                        BrandPojo brandPojo = brandService.getById(key);
                        salesReportData.setBrand(brandPojo.getBrand());
                        salesReportData.setCategory(brandPojo.getCategory());
                        salesReportData.setQuantity(orderItemPojo.getQuantity());
                        salesReportData.setRevenue(orderItemPojo.getSellingPrice()*orderItemPojo.getQuantity());
                    }
                    map.put(key,salesReportData);
                }
            }
        }
        //traverse in map
        for(Integer i: map.keySet()){
            salesReportDatas.add(map.get(i));
        }

        return salesReportDatas;
    }

    public List<DailyReportData> getDailyReport(DailyReportForm dailyReportForm) throws ApiException{
        Validate.validateDailyReportForm(dailyReportForm);
        ZonedDateTime start = ZonedDateTime.parse(dailyReportForm.getStartDate()+"T00:00:00.000Z");
        ZonedDateTime end = ZonedDateTime.parse(dailyReportForm.getEndDate()+"T23:59:59.000Z");
        List<DailyReportPojo> dailyReportPojos = dailyReportService.getAllByDate(start,end);
        if(dailyReportPojos==null){
            throw new ApiException("No items were sold in the given date range!");
        }
        return Helper.convertDailyReportPojosToDatas(dailyReportPojos);
    }
    public List<DailyReportData> getAllDailyReport() throws ApiException {
        List<DailyReportPojo> dailyReportPojos = dailyReportService.getAll();
        if(dailyReportPojos==null){
            throw new ApiException("No sales made yet :(");
        }
        return Helper.convertDailyReportPojosToDatas(dailyReportPojos);
    }

    public Set<Integer> setOfBrandCategoryId(List<BrandData> brandDatas){
        Set<Integer> set = new HashSet<>();
        for(BrandData brandData: brandDatas){
            set.add(brandData.getId());
        }
        return set;
    }

}