var extraImageCount = 0;
var deletedImageIds = [];
$(document).ready(function (){
    $("input[name='extraImage']").each(function (index){
        extraImageCount++;
        $(this).change(function (){
            showExtraImageThumbnail(this,index);
        });
    });

    $("a[name='linkRemoveExtraImage']").each(function (index){
        $(this).click(function (){
            var imageId = $("#imageUrl" + index).val();
            deletedImageIds.push(imageId);
            var deletedImageIdsJSON = JSON.stringify(deletedImageIds);
            $("#deletedImageIdsInput").val(deletedImageIdsJSON);
            removeExtraImage(index);
        });
    });

    $("a[name='linkRemoveDetail']").each(function (index){
        $(this).click(function (){
            removeDetailSectionByIndex(index);
        });
    });
});

function showExtraImageThumbnail(fileInput,index){
    var file = fileInput.files[0];

    fileName = file.name;
    imageNameHiddenField = $("#imageName" + index);
    if(imageNameHiddenField.length){
        imageNameHiddenField.val(fileName);
    }
    var reader = new FileReader();
    reader.onload = function (e){
        $("#extraThumbnail"+index).attr("src",e.target.result);
    };
    reader.readAsDataURL(file);
    if(index >= extraImageCount-1){
        addExtraImageSection(index+1);
    }

}

function removeExtraImage(index){
    $("#divExtraImage" + index).remove();
}

function addExtraImageSection(index) {
    html = '<div class="col border m-3 p-2" id="divExtraImage' + index + '">\n' +
        '                <div id="extraImageHeader' + index + '"><label>Extra Image #' + (index + 1) + ': </label></div>\n' +
        '                <div class="m-2">\n' +
        '                    <img id="extraThumbnail' + index + '" alt="Extra image #' + (index + 1) + ' preview" class="img-fluid" src="' + defaultImageThumbnailImage + '"/>\n' +
        '                </div>\n' +
        '                <div>\n' +
        '                    <input type="file"  name="extraImage" accept="image/png, image/jpeg" onchange="showExtraImageThumbnail(this,' + index + ')" />\n' +
        '                </div>\n' +
        '            </div>';

    htmlLinkRemove = '<a class="btn fas fa-times-circle fa-2x icon-dark float-right" title="Remove this image" href="javascript:removeExtraImage(' + (index-1) + ')"></a>';
    $("#divProductImages").append(html);
    $("#extraImageHeader" + (index-1)).append(htmlLinkRemove);
    extraImageCount++;
}

function addNextDetailSection(){
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    htmlDetailDection = '<div class="form-inline" id="divDetail' + divDetailsCount + '">\n' +
        '            <input type="hidden" name="detailIDs" value="0">\n' +
        '            <label class="m-3">Name:</label>\n' +
        '            <input type="text" class="form-control w-25" name="detailNames" maxlength="255" />\n' +
        '            <label class="m-3">Value:</label>\n' +
        '            <input type="text" class="form-control w-25" name="detailValues" maxlength="255" />\n' +
        '        </div>';

    $("#divProductDetails").append(htmlDetailDection);
    previousDivDetailSection = allDivDetails.last();
    previousDivDetailId = previousDivDetailSection.attr("id");
    previousDivDetailIdString = previousDivDetailId.toString();

    htmlLinkRemove = '<a class="btn fas fa-times-circle fa-2x icon-dark" href="javascript:removeDetailSectionById(\'' + previousDivDetailIdString + '\')" title="Remove this detail" ></a>';

    previousDivDetailSection.append(htmlLinkRemove);

    $("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id){
    $("#" + id).remove();
}

function removeDetailSectionByIndex(index){
    $("#divDetail" + index).remove();
}
