$(document).ready(function (){
    $("#logoutLink").on("click",function (e){
        e.preventDefault();
        document.logoutForm.submit();
    });
    customizeDropDownMenu();
    customizeTabs();
});

function customizeDropDownMenu(){
    $(".navbar .dropdown").hover(
        function (){
            $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
        },
        function (){
            $(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp();
        }
    );
    $(".dropdown > a").click(function (){
        location.href = this.href;
    });
}

function customizeTabs() {
    var url = document.location.toString();

    // Check if the URL contains a fragment identifier
    if (url.indexOf('#') !== -1) {
        $('.nav-tabs a[href="' + url + '"]').tab('show');
    }

    $('.nav-tabs a').on('shown.bs.tab', function (e) {
        // Update the URL hash when a tab is shown
        window.location.hash = e.target.hash;
    });
}
