var data;
var chartOptions;

$(document).ready(function () {
    setUpButtonEventHandlers("_date", loadSalesReportByDate);
});

function loadSalesReportByDate(period){
    if(period == "custom"){
        startDate = $("#startDate_date").val();
        endDate = $("#endDate_date").val();
        requestURL = contextPath + "reports/sales_by_date/" + startDate + "/" + endDate;
    }else{
        requestURL = contextPath + "reports/sales_by_date/" + period;
    }
    $.get(requestURL,function (responseJSON) {
        prepareChartDataForSalesReportByDate(responseJSON);
        customizeChartForSalesReportByDate(period);
        formatChartData(data,1);
        drawChartForSalesReportByDate();
        setSalesAmount(period,'_date',"Total Orders");
    });
}

function prepareChartDataForSalesReportByDate(responseJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string','Date');
    data.addColumn('number','Gross Sales');
    data.addColumn('number','Orders');

    totalGrossSales = 0.0;
    totalOrders = 0.0;

    $.each(responseJSON,function (index,reportItem) {
        data.addRows([[reportItem.identifier,reportItem.grossSales, reportItem.orderCount]]);
        totalGrossSales += parseFloat(reportItem.grossSales);
        totalOrders += parseInt(reportItem.orderCount);
    });
}

function customizeChartForSalesReportByDate(period) {
    chartOptions = {
        title: getChartTitle(period),
        'height': 360,
        legend: {position: 'top'},
        series: {
            0: {targetAxisIndex: 0},
            1: {targetAxisIndex: 1},
        },
        vAxes: {
            0: {title: 'Sales Amount', format: 'đ'},
            1: {title: 'Number of Orders'}
        }
    };
}

function drawChartForSalesReportByDate() {
    var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
    salesChart.draw(data,chartOptions);

}
