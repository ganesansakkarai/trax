$(document).ready(function() {
    oTable = $('#coverage').dataTable({
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": "http://traxtest.herokuapp.com/coverages/"
    });
} );