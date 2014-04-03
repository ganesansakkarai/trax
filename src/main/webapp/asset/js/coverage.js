$(document).ready(function() {
    oTable = $('#coverage').dataTable({
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "bServerSide": true,
        "sAjaxSource": "http://traxtest.herokuapp.com/coverages/",
        "sServerMethod": "POST",
        "aoColumns": [
            { "mData": "name" },
            { "mData": "timestamp" },
            { "mData": "lines" },
            { "mData": "missedLines" },
            { "mData": "branch" },
            { "mData": "missedBranch" },
        ]
    });
} );