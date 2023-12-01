function clearFilter(){
    window.location = modelURL;
}

function showDeleteConfirmModal(link,entityName,idDelete){
    let entityId = link.attr(idDelete);

    $("#yesButton").attr("href",link.attr("href"));
    $("#confirmText").text("Are you sure you want to delete this " + entityName + " ID " + entityId + "?");
    $("#confirmModal").modal();
}