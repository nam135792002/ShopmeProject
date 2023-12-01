package com.shopme.admin.report;

import com.shopme.admin.order.OrderRepository;
import com.shopme.common.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MasterOrderReportService extends AbstractReportService{
    @Autowired private OrderRepository repository;

    protected List<ReportItem> getReportDateByDateRangeInternal(Date startTime, Date endTime, ReportType reportType){
        List<Order> listOrders = repository.findByOrderTimeBetween(startTime,endTime);
        List<ReportItem> listReportTimes = createReportDate(startTime,endTime, reportType);
        calculateSalesForReportData(listOrders,listReportTimes);
        printReportData(listReportTimes);
        return listReportTimes;
    }

    private void printReportData(List<ReportItem> listReportTimes){
        listReportTimes.forEach(item->{
            System.out.printf("%s, %10.2f, %10.2f, %d \n", item.getIdentifier(), item.getGrossSales(), item.getNetSales(), item.getOrderCount());
        });
    }

    private void calculateSalesForReportData(List<Order> listOrders, List<ReportItem> listReportTimes){
        for (Order order : listOrders){
            String orderDateString = dateFormat.format(order.getOrderTime());

            ReportItem reportItem = new ReportItem(orderDateString);

            int itemIndex = listReportTimes.indexOf(reportItem);
            if (itemIndex >= 0){
                reportItem = listReportTimes.get(itemIndex);

                reportItem.addGrossSales(order.getTotal());
                reportItem.addNetSales(order.getSubtotal() - order.getProductCost());
                reportItem.increaseOrderCount();
            }
        }
    }

    private List<ReportItem> createReportDate(Date startTime, Date endTime, ReportType reportType) {
        List<ReportItem> listReportItems = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);

        Date currentDate = startDate.getTime();
        String dateString = dateFormat.format(currentDate);
        listReportItems.add(new ReportItem(dateString));

        do {
            if(reportType.equals(ReportType.DAY)){
                startDate.add(Calendar.DAY_OF_MONTH,1);
            } else if (reportType.equals(ReportType.MONTH)) {
                startDate.add(Calendar.MONTH,1);
            }
            currentDate = startDate.getTime();
            dateString = dateFormat.format(currentDate);

            listReportItems.add(new ReportItem(dateString));
        }while (startDate.before(endDate));

        return listReportItems;
    }
}
