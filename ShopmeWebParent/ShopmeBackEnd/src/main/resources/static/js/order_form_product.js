var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function () {

    fieldProductCost = $("#productCost");
    fieldSubtotal = $("#subtotal");
    fieldShippingCost = $("#shippingCost")
    fieldTax = $("#tax");
    fieldTotal = $("#total");

    $("#productList").on("change",".quantity-input", function (e) {
        updateSubtotalWhenQuantityChange($(this));
        updateOrderAmounts();
    });

    $("#productList").on("change",".price-input", function (e) {
        updateSubtotalWhenPriceChange($(this));
        updateOrderAmounts();
    });

    $("#productList").on("change",".cost-input", function (e) {
        updateOrderAmounts();
    });

    $("#productList").on("change",".ship-input", function (e) {
        updateOrderAmounts();
    });
});

function updateOrderAmounts() {
    totalCost = 0.0;
    $(".cost-input").each(function (e) {
        costInputField = $(this);
        rowNumber = costInputField.attr("rowNumber");
        quantityValue = $("#quantity" + rowNumber).val();

        productCost = costInputField.val().replaceAll(".","");
        totalCost += parseFloat(productCost) * parseInt(quantityValue);
    });
    $("#productCost").val(totalCost);

    orderSubtotal = 0.0;
    $(".subtotal-output").each(function (e) {
        productSubtotal = parseFloat($(this).val().replaceAll(".",""));
        orderSubtotal += productSubtotal;
    });
    $("#subtotal").val(orderSubtotal);

    shippingCost = 0.0;
    $(".ship-input").each(function (e){
        productShip = parseFloat($(this).val().replaceAll(".",""));
        shippingCost += productShip;
    });
    $("#shippingCost").val(shippingCost);

    tax = parseFloat($("#tax").val().replaceAll(".",""));
    orderTotal = orderSubtotal + tax + shippingCost;
    $("#total").val(orderTotal);
}

function updateSubtotalWhenQuantityChange(input) {
    quantityValue = input.val();
    rowNumber = input.attr("rowNumber");
    priceField = $("#price"+rowNumber);
    priceValue = parseFloat(priceField.val().replaceAll(".",""));
    newSubtotal = parseFloat(quantityValue)*priceValue;
    subtotalField = $("#subtotal"+rowNumber);
    subtotalField.val(newSubtotal);
}

function updateSubtotalWhenPriceChange(input) {
    priceValue = input.val().replaceAll(".","");
    rowNumber = input.attr("rowNumber");

    quantityField = $("#quantity"+rowNumber);
    quantityValue = quantityField.val();
    newSubtotal = parseFloat(quantityValue) * parseFloat(priceValue);

    subtotalField = $("#subtotal"+rowNumber);
    subtotalField.val(newSubtotal);
}

function formatCurrency(input) {
    // Parsing input value
    let value = input.value.replace(/\D/g, '');
    // Formatting value
    value = value.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    // Updating input value
    input.value = value;
}

function processFormBeForSubmit(){
    removeThousandSeparatorForField(fieldProductCost);
    removeThousandSeparatorForField(fieldSubtotal);
    removeThousandSeparatorForField(fieldShippingCost);
    removeThousandSeparatorForField(fieldTax);
    removeThousandSeparatorForField(fieldTotal);

    $(".cost-input").each(function (e) {
       removeThousandSeparatorForField($(this));
    });

    $(".price-input").each(function (e) {
        removeThousandSeparatorForField($(this));
    });

    $(".subtotal-output").each(function (e) {
        removeThousandSeparatorForField($(this));
    });

    $(".ship-input").each(function (e) {
        removeThousandSeparatorForField($(this));
    });
}

function removeThousandSeparatorForField(fieldRef){
    fieldRef.val(fieldRef.val().replace(".",""));
}

function setCountryName(){
    selectedCountry = $("#country option:selected");
    countryName = selectedCountry.text();
    $("#countryName").val(countryName);
}
