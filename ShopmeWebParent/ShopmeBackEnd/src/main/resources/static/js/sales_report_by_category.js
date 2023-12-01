var data;
var chartOptions;

$(document).ready(function () {
    setUpButtonEventHandlers("_category", loadSalesReportByDateForCategory);
});

function loadSalesReportByDateForCategory(period){
    if(period == "custom"){
        startDate = $("#startDate_category").val();
        endDate = $("#endDate_category").val();
        requestURL = contextPath + "reports/category/" + startDate + "/" + endDate;
    }else{
        requestURL = contextPath + "reports/category/" + period;
    }
    $.get(requestURL,function (responseJSON) {
        prepareChartDataForSalesReportByCategory(responseJSON);
        customizeChartForSalesReportByCategory();
        formatChartData(data,1);
        drawChartForSalesReportByCategory();
        setSalesAmount(period,'_category',"Total Products");
    });
}

function prepareChartDataForSalesReportByCategory(responseJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string','Category');
    data.addColumn('number','Gross Sales');

    totalGrossSales = 0.0;
    totalItems = 0.0;

    $.each(responseJSON,function (index,reportItem) {
        data.addRows([[reportItem.identifier,reportItem.grossSales]]);
        totalGrossSales += parseFloat(reportItem.grossSales);
        totalItems += parseInt(reportItem.orderCount);
    });
}

function customizeChartForSalesReportByCategory() {
    chartOptions = {
        height: 360, legend: {position: 'right'}
    };
}

function drawChartForSalesReportByCategory() {
    var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
    salesChart.draw(data,chartOptions);

}
