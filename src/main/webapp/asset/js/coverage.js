$(document).ready(function() {
	oTable = $('#coverage').dataTable({
		"bJQueryUI" : true,
		"sPaginationType" : "full_numbers",
		"bServerSide" : true,
		"sAjaxSource" : "http://localhost:8080/coverages/",
		"sServerMethod" : "POST",
		"aoColumns" : [ {
			"mData" : "className"
		}, {
			"mData" : "methodName"
		}, {
			"mData" : "coverage"
		}, {
			"mData" : "line"
		}, {
			"mData" : "missedLine"
		}, {
			"mData" : "branch"
		}, {
			"mData" : "missedBranch"
		}, ],
		"aoColumnDefs" : [ {			
			"sClass" : "center",
			"aTargets" : [ 1,2,3,4,5,6 ]
		} ]
	});
});