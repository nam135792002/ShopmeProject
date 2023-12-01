package com.shopme.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {
    protected DateFormat dateFormat;

    public List<ReportItem> getReportDateLast7Days(ReportType reportType){
        return getReportDateLastXDays(7,reportType);
    }

    public List<ReportItem> getReportDateLast28Days(ReportType reportType){
        return getReportDateLastXDays(28,reportType);
    }

    protected List<ReportItem> getReportDateLastXDays(int days, ReportType reportType){
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,-(days-1));
        Date startTime = cal.getTime();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDateByDateRangeInternal(startTime,endTime,reportType);
    }

    public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
        return getReportDateLastXMonths(6,reportType);
    }

    public List<ReportItem> getReportDataLastYear(ReportType reportType) {
        return getReportDateLastXMonths(12,reportType);
    }

    protected List<ReportItem> getReportDateLastXMonths(int months, ReportType reportType){
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,-(months-1));
        Date startTime = cal.getTime();

        dateFormat = new SimpleDateFormat("yyyy-MM");
        return getReportDateByDateRangeInternal(startTime,endTime,reportType);
    }

    public List<ReportItem> getReportDateByDateRange(Date startTime, Date endTime, ReportType reportType){
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDateByDateRangeInternal(startTime, endTime, reportType);
    }

    protected abstract List<ReportItem> getReportDateByDateRangeInternal(Date startDate, Date endDate, ReportType reportType);
}
