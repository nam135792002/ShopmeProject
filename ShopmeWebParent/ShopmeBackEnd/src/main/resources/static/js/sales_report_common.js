var MILISECONDS_A_DAY = 24 * 60 * 60 * 1000;

function setUpButtonEventHandlers(reportType, callbackFunction){
   $(".button-sales-by"+reportType).on("click",function () {
        $(".button-sales-by"+reportType).each(function (e) {
            $(this).removeClass('btn-primary').addClass('btn-light');
        });

        $(this).removeClass('btn-light').addClass('btn-primary');
        period = $(this).attr("period");
        if(period){
            callbackFunction(period);
            $("#divCustomDateRange"+reportType).addClass("d-none");
        }else{
            $("#divCustomDateRange"+reportType).removeClass("d-none");
        }

    });

    initCustomDateRange(reportType);
    $("#buttonViewReportByDateRange"+reportType).on("click",function (e) {
        validateDateRange(reportType,callbackFunction);
    });
}

function initCustomDateRange(reportType){
    startDateField = document.getElementById('startDate'+reportType);
    endDateField = document.getElementById('endDate'+reportType);

    toDate = new Date();
    endDateField.valueAsDate = toDate;

    fromDate = new Date();
    fromDate.setDate(toDate.getDate() - 30);
    startDateField.valueAsDate = fromDate;
}

function validateDateRange(reportType, callbackFunction){
    startDateField = document.getElementById('startDate'+reportType);
    days = calculateDays(reportType);
    startDateField.setCustomValidity("");

    if(days >= 7 && days <= 30){
        loadSalesReportByDate("custom");
    }else{
        startDateField.setCustomValidity("Dates must be in the range of 7..30 days");
        startDateField.reportValidity();
    }
}

function calculateDays(reportType){
    startDateField = document.getElementById('startDate'+reportType);
    endDateField = document.getElementById('endDate'+reportType);

    startDate = startDateField.valueAsDate;
    endDate = endDateField.valueAsDate;

    differenceInMilliseconds = endDate - startDate;
    return differenceInMilliseconds / MILISECONDS_A_DAY;
}

function getChartTitle(period){
    if(period == "last_7_days") return "Sales in Last 7 Days";
    if(period == "last_28_days") return "Sales in Last 28 Days";
    if(period == "last_6_months") return "Sales in Last 6 Months";
    if(period == "last_year") return "Sales in Last Year";
    if(period == "custom") return "Custom Date Range";
    return "";
}

function getDenominator(period, reportType){
    if(period == "last_7_days") return 7;
    if(period == "last_28_days") return 28;
    if(period == "last_6_months") return 6;
    if(period == "last_year") return 12;
    if(period == "custom") return calculateDays(reportType);
    return 7;
}

function setSalesAmount(period, reportType, labelTotalItems){
    $("#textTotalGrossSales"+reportType).text(totalGrossSales+"đ");

    denominator = getDenominator(period,reportType);
    $("#textAvgGrossSales"+reportType).text(totalGrossSales / denominator +"đ");
    $("#labelTotalItems"+reportType).text(labelTotalItems);
    $("#textTotalItems"+reportType).text(totalOrders);
}

function formatChartData(data, columnIndex1){
    var formatter = new google.visualization.NumberFormat({
        prefix: 'đ'
    });

    formatter.format(data,columnIndex1);
}