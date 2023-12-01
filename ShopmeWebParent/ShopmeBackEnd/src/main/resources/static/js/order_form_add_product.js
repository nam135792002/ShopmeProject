$(document).ready(function () {
    $("#products").on("click","#linkAddProduct",function (e) {
        e.preventDefault();
        link = $(this);
        url = link.attr("href");
        $("#addProductModal").on("show.bs.modal", function (){
           $(this).find("iframe").attr("src",url);
        });
        $("#addProductModal").modal()
    });
});

function isProductAlreadyAdded(productId) {
    productExists = false;
    $(".hiddenProductId").each(function (e) {
        aProductId = $(this).val();
        if(aProductId == productId){
            productExists = true;
            return;
        }
    });
    return productExists;
}

function addProduct(productId, productName){
    $("#addProductModal").modal("hide");
    getShippingCost(productId);
}

function getShippingCost(productId){
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    state = $("#state").val();
    if(state.length === 0){
        state = $("#city").val();
    }

    requestUrl = contextPath + "get_shipping_cost";
    params = {productId: productId, countryId: countryId, state: state};

    $.ajax({
        type: 'POST',
        url: requestUrl,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data: params
    }).done(function (shippingCost){
        getProductInfo(productId, shippingCost);
    }).fail(function (err){
        showWarningModal(err.responseJSON.message);
        shippingCost = 0.0;
        getProductInfo(productId, shippingCost);
    }).always(function () {
        $("#addProductModal").modal("hide");
    });
}

function getProductInfo(productId, shippingCost) {
    var requestUrl = contextPath + "products/get/" + productId;
    $.get(requestUrl, function(productJson) {
        console.log(productJson);
        console.log(productJson.name);
        var productName = productJson.name;
        var mainImage = productJson.mainImage;
        var productCost = productJson.cost;
        var productPrice = productJson.price;
        var htmlCode = insertProductCode(productId, productName, mainImage, productCost, productPrice, shippingCost);
        $("#productList").append(htmlCode);
        updateOrderAmounts();
    }).fail(function(err) {
        showWarningModal(err.responseJSON.message);
    });
}

function insertProductCode(productId, productName, mainImage, productCost, productPrice, shippingCost) {
    var nextCount = $(".hiddenProductId").length + 1;
    rowId = "row"+nextCount;
    var quantityId = "quantity" + nextCount;
    var priceId = "price" + nextCount;
    var subtotalId = "subtotal" + nextCount;

    var htmlCode = `<div class="border rounded p-1" id="${rowId}">
          <input type="hidden" name="productId" value="${productId}" class="hiddenProductId" />
          <div class="row">
            <div class="col-1">
              <div class="divCount">${nextCount}</div>
              <div><a class="fas fa-trash icon-dark linkRemove" href="" rowNUmber="${nextCount}"></a></div>
            </div>

            <div class="col-3">
              <img src="${mainImage}" class="img-fluid" />
            </div>
          </div>

          <div class="row m-2">
            <b>${productName}</b>
          </div>

          <div class="row m-2">
            <table>
              <tr>
                <td>Product Cost:</td>
                <td>
                  <input type="text" required class="form-control m-1 cost-input" value="${productCost}" style="max-width: 140px;" rowNumber="${nextCount}" />
                </td>
              </tr>

              <tr>
                <td>Quantity:</td>
                <td>
                  <input type="number" step="1" min="1" max="5" required class="form-control m-1 quantity-input" value="1" style="max-width: 140px;" rowNumber="${nextCount}" id="${quantityId}" />
                </td>
              </tr>

              <tr>
                <td>Unit Price:</td>
                <td>
                  <input type="text" required class="form-control m-1 price-input" value="${productPrice}" style="max-width: 140px;" rowNumber="${nextCount}" id="${priceId}" oninput="formatCurrency(this)" />
                </td>
              </tr>

              <tr>
                <td>Subtotal:</td>
                <td>
                  <input type="text" readonly class="form-control m-1 subtotal-output" value="${productPrice}" style="max-width: 140px;" id="${subtotalId}" />
                </td>
              </tr>

              <tr>
                <td>Shipping Cost:</td>
                <td>
                  <input type="text" required class="form-control m-1 ship-input" value="${shippingCost}" style="max-width: 140px;" />
                </td>
              </tr>
            </table>
          </div>

        </div>
        <div class="row">&nbsp;</div>`;
    return htmlCode;
}
