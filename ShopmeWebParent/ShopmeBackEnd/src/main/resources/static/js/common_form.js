$(document).ready(function (){
    $("#buttonCancel").on("click", function (){
        window.location = modelURL;
    });

    $("#noButton").on("click", function () {
        $("#modalDialog").modal("hide");
    });

    $(".modal-header button.close").on("click", function () {
        $("#modalDialog").modal("hide");
    });

    $("#fileImage").change(function (){
        fileSize = this.files[0].size;
        if(fileSize > 1000000000) {
            this.setCustomValidity("You must choose an image less than 1GB!");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
        showImageThumbnail(this);
    });
});

function showImageThumbnail(fileInput){
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e){
        $(".thumbnail").attr("src",e.target.result);
    };
    reader.readAsDataURL(file);
}

function showModalDialog(title, message){
    $("#modalTittle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal("show");
}

function showErrorModal(message){
    showModalDialog("Error", message);
}

function showWarningModal(message){
    showModalDialog("Warning",message);
}