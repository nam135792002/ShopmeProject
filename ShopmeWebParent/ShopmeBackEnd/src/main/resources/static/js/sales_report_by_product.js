var data;
var chartOptions;

$(document).ready(function () {
    setUpButtonEventHandlers("_product", loadSalesReportByDateForProduct);
});

function loadSalesReportByDateForProduct(period){
    if(period == "custom"){
        startDate = $("#startDate_product").val();
        endDate = $("#endDate_product").val();
        requestURL = contextPath + "reports/product/" + startDate + "/" + endDate;
    }else{
        requestURL = contextPath + "reports/product/" + period;
    }
    $.get(requestURL,function (responseJSON) {
        prepareChartDataForSalesReportByProduct(responseJSON);
        customizeChartForSalesReportByProduct();
        formatChartData(data,2);
        drawChartForSalesReportByProduct();
        setSalesAmount(period,'_product',"Total Products");
    });
}

function prepareChartDataForSalesReportByProduct(responseJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string','Product');
    data.addColumn('number','Quantity');
    data.addColumn('number','Gross Sales');

    totalGrossSales = 0.0;
    totalItems = 0.0;

    $.each(responseJSON,function (index,reportItem) {
        data.addRows([[reportItem.identifier, reportItem.productCount, reportItem.grossSales]]);
        totalGrossSales += parseFloat(reportItem.grossSales);
        totalItems += parseInt(reportItem.orderCount);
    });
}

function customizeChartForSalesReportByProduct() {
    chartOptions = {
        height: 360, width: '80%',
        showRowNumber: true,
        page: 'enable',
        sortColumn: 2,
        sortAscending: false
    };
}

function drawChartForSalesReportByProduct() {
    var salesChart = new google.visualization.Table(document.getElementById('chart_sales_by_product'));
    salesChart.draw(data,chartOptions);

}
