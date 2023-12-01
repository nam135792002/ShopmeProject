package com.shopme.admin.report;

import com.shopme.admin.order.OrderDetailRepository;
import com.shopme.common.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailReportService extends AbstractReportService{

    @Autowired private OrderDetailRepository repository;
    @Override
    protected List<ReportItem> getReportDateByDateRangeInternal(Date startDate, Date endDate, ReportType reportType) {
        List<OrderDetail> listOrderDetails = null;

        if (reportType.equals(ReportType.CATEGORY)){
            listOrderDetails = repository.findWithCategoryAndTimeBetween(startDate,endDate);
        } else if (reportType.equals(ReportType.PRODUCT)){
            listOrderDetails = repository.findWithProductAndTimeBetween(startDate, endDate);
        }
//        printRawDate(listOrderDetails);
        List<ReportItem> listReportItems = new ArrayList<>();
        for (OrderDetail detail : listOrderDetails){
            String identifier = "";
            if (reportType.equals(ReportType.CATEGORY)){
                identifier = detail.getProduct().getCategory().getName();
            }else if (reportType.equals(ReportType.PRODUCT)){
                identifier = detail.getProduct().getShortName();
            }
            ReportItem reportItem = new ReportItem(identifier);
            float grossSales = detail.getSubtotal() + detail.getShippingCost();
            float netSales = detail.getSubtotal() - detail.getProductCost();

            int itemIndex = listReportItems.indexOf(reportItem);
            if(itemIndex >= 0){
                reportItem = listReportItems.get(itemIndex);
                reportItem.addGrossSales(grossSales);
                reportItem.addNetSales(netSales);
                reportItem.increasProductCount(detail.getQuantity());
            }else {
                listReportItems.add(new ReportItem(identifier, grossSales, netSales, detail.getQuantity()));
            }
        }

//        printReportDate(listReportItems);
        return listReportItems;
    }

    private void printReportDate(List<ReportItem> listReportItems) {
        for (ReportItem item : listReportItems){
            System.out.printf("%-20s, %10.2f, %10.2f, %d \n", item.getIdentifier(), item.getGrossSales(), item.getNetSales(), item.getProductCount());
        }
    }

    private void printRawDate(List<OrderDetail> listOrderDetails) {
        for (OrderDetail detail : listOrderDetails){
            System.out.printf("%d, %-20s, %10.2f, %10.2f, %10.2f \n",detail.getQuantity(), detail.getProduct().getShortName().substring(0,20), detail.getSubtotal(), detail.getProductCost(), detail.getShippingCost());
        }
    }
}
