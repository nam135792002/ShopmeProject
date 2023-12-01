var dropdownCountries;
var dropdownStates;

$(document).ready(function (){
    dropdownCountries = $("#country");
    dropdownStates = $("#listStates");
    dropdownCountries.on("change",function (){
        loadStatesForCountry();
        $("#state").val("").focus();
    });

    $('#buttonCancel').click(function (){
        window.location = modelURL;
    });
});

function loadStatesForCountry(){
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url,function (responseJSON){
        dropdownStates.empty();
        $.each(responseJSON, function (index,state){
            $("<option>").val(state.name).text(state.name).appendTo(dropdownStates);
        });
    }).fail(function (){
        showErrorModal("Error loading states/provinces for the selected country.");
    });
}