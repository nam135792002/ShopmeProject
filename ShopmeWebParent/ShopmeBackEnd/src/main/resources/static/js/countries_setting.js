var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function (){
   buttonLoad = $("#buttonLoadCountries");
   dropDownCountry = $("#dropDownCountries");
   buttonAddCountry = $("#buttonAddCountry");
   buttonUpdateCountry = $("#buttonUpdateCountry");
   buttonDeleteCountry = $("#buttonDeleteCountry");
   labelCountryName = $("#labelCountryName");
   fieldCountryName = $("#fieldCountryname");
   fieldCountryCode = $("#fieldCountrycode");

   buttonLoad.click(function (){
      loadCountries();
   });
   dropDownCountry.on("change",function (){
       changeFormStateToSelectedCountry();
   });
   buttonAddCountry.click(function (){
       if(buttonAddCountry.val() === "Add"){
           addCountry();
       }else{
           changeFormCountryToNew();
       }
   });

   buttonUpdateCountry.click(function (){
        updateCountry();
   });

   buttonDeleteCountry.click(function (){
       deleteCountry();
   });
});

function deleteCountry(){
    optionValue = dropDownCountry.val();
    countryId = optionValue.split("-")[0];
    url = "http://localhost:8080/ShopmeAdmin/countries/delete/" + countryId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        }
    }).done(function (){
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormCountryToNew();
        showToastmessage("The country has been deleted");
    }).fail(function (){
        showToastmessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function updateCountry(){
    if(!validateFormCountry()) return;

    url = "http://localhost:8080/ShopmeAdmin/countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropDownCountry.val().split("-")[0];
    jsonData = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId){
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        showToastmessage("The country has been updated");

        changeFormCountryToNew();
    }).fail(function (){
        showToastmessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function validateFormCountry(){
    formCountry = document.getElementById("formCountry");
    if(!formCountry.checkValidity()){
        formCountry.reportValidity();
        return false;
    }
    return true;
}

function addCountry(){
    if(!validateFormCountry()) return;

    url = "http://localhost:8080/ShopmeAdmin/countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code: countryCode};

    $.ajax({
       type: 'POST',
       url: url,
       beforeSend: function (xhr) {
           xhr.setRequestHeader(csrfHeaderName,csrfValue);
       },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId){
        selectNewlyAddedCountry(countryId,countryCode,countryName);
        showToastmessage("The new country has been added");
    }).fail(function (){
        showToastmessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function selectNewlyAddedCountry(countryId,countryCode,countryName){
    optionValue = countryId + "-" +countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected",true);
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormCountryToNew(){
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name: ");

    buttonUpdateCountry.prop("disabled",true);
    buttonDeleteCountry.prop("disabled",true)

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToSelectedCountry(){
    buttonAddCountry.prop("value","New");
    buttonUpdateCountry.prop("disabled",false);
    buttonDeleteCountry.prop("disabled",false)

    labelCountryName.text("Selected Country:");
    selectedCountryname = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryname);

    countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}

function loadCountries(){
    url = "http://localhost:8080/ShopmeAdmin/countries/list";
    $.get(url,function (responseJSON){
       dropDownCountry.empty();

       $.each(responseJSON,function (index,country){
          optionValue = country.id + "-" + country.code;
          $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
       });
    }).done(function (){
       buttonLoad.val("Refresh Country List");
        showToastmessage("All countries have been loaded");
    }).fail(function (){
        showToastmessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function showToastmessage(message){
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}