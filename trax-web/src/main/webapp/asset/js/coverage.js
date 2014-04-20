$(document).ready(function() {
	
	var appListUrl = 'http://localhost:8080/applications/'
	var buildUrl = 'http://localhost:8080/builds/'
	var url = 'http://localhost:8080/coverage/summary/';
	var selectedApp;
	var selectedBuild;
	var testType = 'UNIT';
	var coverageTable ;
	var coverageAnOpen = [];
	var coverageParentRow;
	var coveragePackageRow = false;
	var coverageClassRow = false;
	var testResultTable ;
	var testResultAnOpen = [];
	var testResultParentRow;
	var testResultPackageRow = false;
	var testResultClassRow = false;
	
	$(function(){
		
		$("#title").html("Code Coverage Summary")
		$("#coverageTabs").removeClass("hide");
		$("#testResultTabs").addClass("hide"); 
		
	    $("ul li").click(function(){
	        $("ul li").removeClass("on");
	        $(this).addClass("on");
	        showHide($(this).attr("id"));
	    });
	});
	
	function showHide(id){
		
		if(id=='codeCoverage'){
			$("#title").html("Code Coverage Summary")
			$("#coverageTabs").removeClass("hide");
			$("#testResultTabs").addClass("hide"); 
		} else {
			$("#title").html("Test Result Summary")
			$("#testResultTabs").removeClass("hide");
			$("#coverageTabs").addClass("hide"); 
		}
	}

	$.ajax({
		dataType : "json",
		type : 'POST',
		async : false,
		url : appListUrl + testType,
		success : function(responseObject) {
			var applications = $("#applicationType");
			applications.empty();
			$.each(responseObject, function(index, value) {
				applications.append('<option value="' + value + '">'
						+ value + '</option>');
			});

			if (selectedApp == undefined) {
				selectedApp = $("#applicationType option:first").val();
			}
			loadBuild();
		},
		error : function(xhr, status, error) {
			alert('failed : ' + err.Message);
		}
	});

	$("#applicationType").change(function() {

		$("#applicationType option:selected").each(function() {
			selectedApp = $(this).text();
		});
		loadBuild();
	})

	function loadBuild() {
		
		$.ajax({
			dataType : "json",
			type : 'POST',
			async : false,
			url : buildUrl + selectedApp + '/' + testType,
			success : function(responseObject) {
				var builds = $("#builds");
				builds.empty();
				$.each(responseObject, function(index, value) {
					builds.append('<option value="' + value + '">'
							+ value + '</option>');
				});

				if (selectedBuild == undefined) {
					selectedBuild = $("#builds option:first").val();
				}
				refreshCoverage();
				refreshTestResults();
			}
		});
	}

	$("#builds").change(function() {

		$("#builds option:selected").each(function() {
			selectedBuild = $(this).text();
			refreshCoverage();
			refreshTestResults();
		});
	})

	fnOpenNewRow = function(nTr, myArray, sClass, table) {

		var oSettings = table.fnSettings();
		table.fnClose(nTr);
		for ( var i in myArray) {
			var nNewRow = document.createElement("tr");
			if (typeof mHtml === "string") {
				nNewRow.innerHTML = myArray[i];
			} else {
				$(nNewRow).html(myArray[i]);
			}

			var nTrs = $('tr', oSettings.nTBody);
			if ($.inArray(nTr, nTrs) != -1) {
				$(nNewRow).insertAfter(nTr);
			}

			oSettings.aoOpenRows.push({
				"nTr" : nNewRow,
				"nParent" : nTr
			});
		}
		return nNewRow;
	};

	fnCloseOpenedRow = function(nTr, table) {

		var oSettings = table.fnSettings();
		var index = oSettings.aoOpenRows.length - 1;
		for ( var i = index; i >= 0; i--) {
			if (oSettings.aoOpenRows[i].nParent == nTr) {
				var nTrParent = oSettings.aoOpenRows[i].nTr.parentNode;
				if (nTrParent) {
					nTrParent.removeChild(oSettings.aoOpenRows[i].nTr);
				}
				oSettings.aoOpenRows.splice(i, 1);
			}
		}
		return 1;
	};
	
	function refreshCoverage() {
		
		coveragePackageRow = false;
		coverageClassRow = false;
		
		coverageTable = $('#coverage').dataTable({
			"bJQueryUI" : true,
			"bDestroy": true,
			"bPaginate" : false,
			"bServerSide" : true,
			"sAjaxSource" :  url + selectedApp + '/' + testType + '/' + selectedBuild,
			"sServerMethod" : "POST",
			"aoColumns" : [ 
			{
			     "mDataProp": null,
			     "sClass": "coveragePackage left",
			     "sDefaultContent": '<img src="../asset/image/details_open.png">',
			     "sWidth": "10%"
			},
			{
				"mData" : "id"
			},
			{
				"mData" : "name"
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
			} ],
			"aoColumnDefs" : [ 
			          {"sClass" : "center","aTargets" : [2,3,4,5,6,7]},
			          { "bVisible": false, "aTargets": [ 1 ] }]
		});
		
		$("#coverage").removeAttr("style");
	}
	
	
	function refreshTestResults() {
		
		testResultPackageRow = false;
		testResultClassRow = false;
		
		testResultTable = $('#testResult').dataTable({
			"bJQueryUI" : true,
			"bDestroy": true,
			"bPaginate" : false,
			"bServerSide" : true,
			"sAjaxSource" :  url + selectedApp + '/' + testType + '/' + selectedBuild,
			"sServerMethod" : "POST",
			"aoColumns" : [ 
			{
			     "mDataProp": null,
			     "sClass": "testResultPackage left",
			     "sDefaultContent": '<img src="../asset/image/details_open.png">',
			     "sWidth": "10%"
			},{
				"mData" : "id"
			},{
				"mData" : "name"
			},{
				"mData" : "duration"
			},{
				"mData" : "pass"
			}, {
				"mData" : "fail"
			}, {
				"mData" : "skip"
			}, {
				"mData" : "success"
			}, {
				"mData" : "startTime"
			},{
				"mData" : "endTime"
			}],
			"aoColumnDefs" : [ 
			          {"sClass" : "center","aTargets" : [2,3,4,5,6,7]},
			          { "bVisible": false, "aTargets": [ 1 ] }]
		});
		
		$("#testResult").removeAttr("style");
	}
	
	$('#coverage td.coveragePackage').live( 'click', function () {
		
		if(coveragePackageRow || coverageClassRow){
			return false;
		}
		var nTr = this.parentNode;
		coverageParentRow = nTr;
		var i = $.inArray( nTr, coverageAnOpen );
	    
		if ( i === -1 ) {
			$('img', this).attr( 'src', "../asset/image/details_close.png" );
			fnOpenNewRow( nTr, fnClazzesDetails(coverageTable, nTr), 'details', coverageTable);
			coverageAnOpen.push( nTr );
	    } else {
	    	$('img', this).attr( 'src', "../asset/image/details_open.png" );
	    	fnCloseOpenedRow( nTr,  coverageTable);
	    	coverageAnOpen.splice( i, 1 );
	    }
		
	});
		 
	function fnClazzesDetails( coverageTable, nTr )
	{
	
	  var oData = coverageTable.fnGetData( nTr );
	  var clazzes = oData.clazzes;
	
	  var myArray = [];
	  var i= 0; 
	  $.each(clazzes, function(key,value){
		  var sOut ='';
		  sOut = sOut + '<td class="clazzes center" style="background-color:#F9F9F9"><img src="../asset/image/details_open.png"></td>';
		  sOut = sOut + '<td id="id" align="center" style="display:none">'+value.id +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.name +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.coverage +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.line +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.missedLine +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.branch +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.missedBranch +'</td>';
		  myArray[i] = sOut;
		  i++;
		});
	
	 return myArray;
	}
	
	$('#coverage td.clazzes').live( 'click', function () {
		
		if(coverageClassRow){
			return false;
		}
		
		var nTr = this.parentNode;
		var id = $(this).closest('td').next('td').html();    
		var i = $.inArray( nTr, coverageAnOpen );
		    
	   if ( i === -1 ) {
	      $('img', this).attr( 'src', "../asset/image/details_close.png" );
	      coveragePackageRow = true;
	      fnOpenNewRow( nTr, fnMethodsDetails(coverageTable, id), 'details', coverageTable);
	      coverageAnOpen.push( nTr );
	    }
	    else {
	      $('img', this).attr( 'src', "../asset/image/details_open.png" );
	      coveragePackageRow = false;
	      fnCloseOpenedRow( nTr, coverageTable);
	      coverageAnOpen.splice( i, 1 );
	    }
	});
	
	function fnMethodsDetails(coverageTable, id) {
		
	  var oData = coverageTable.fnGetData( coverageParentRow );
	  var clazzes = oData.clazzes;
	  var myArray = [];
	  
	  $.each(clazzes, function(key,value){
		if(value.id == id){
			var i= 0; 
			if(this.methods){
				$.each(this.methods, function(key,value){
				 var sOut ='';
				 sOut = sOut + '<td class="classes" align="right" style="background-color:#EAEAEA"></td>';
				 sOut = sOut + '<td id="id" align="center" style="display:none;background-color:#EAEAEA">'+value.id +'</td>';
				 sOut = sOut + '<td align="center" style="background-color:#EAEAEA">'+value.name +'</td>';
				 sOut = sOut + '<td align="center" style="background-color:#EAEAEA">'+value.coverage +'</td>';
				 sOut = sOut + '<td align="center" style="background-color:#EAEAEA">'+value.line +'</td>';
				 sOut = sOut + '<td align="center" style="background-color:#EAEAEA">'+value.missedLine +'</td>';
				 sOut = sOut + '<td align="center" style="background-color:#EAEAEA">'+value.branch +'</td>';
				 sOut = sOut + '<td align="center" style="background-color:#EAEAEA">'+value.missedBranch +'</td>';
				 myArray[i] = sOut;
				 i++;
				});
			}
		}
	  });
		 
	return myArray;
	}
	
	
	$('#testResult td.testResultPackage').live( 'click', function () {
		
		if(testResultPackageRow || testResultClassRow){
			return false;
		}
		var nTr = this.parentNode;
		testResultParentRow = nTr;
		var i = $.inArray( nTr, testResultAnOpen );
	    
		if ( i === -1 ) {
			$('img', this).attr( 'src', "../asset/image/details_close.png" );
			fnOpenNewRow( nTr, fnTestSuiteDetails(testResultTable, nTr), 'details', testResultTable);
			testResultAnOpen.push( nTr );
	    } else {
	    	$('img', this).attr( 'src', "../asset/image/details_open.png" );
	    	fnCloseOpenedRow( nTr,  testResultTable);
	    	testResultAnOpen.splice( i, 1 );
	    }
		
	});
		 
	function fnTestSuiteDetails( testResultTable, nTr )
	{
	
	  var oData = testResultTable.fnGetData( nTr );
	  var testSuites = oData.testSuites;
	
	  var myArray = [];
	  var i= 0; 
	  $.each(testSuites, function(key,value){
		  var sOut ='';
		  sOut = sOut + '<td class="testSuite center" style="background-color:#F9F9F9"><img src="../asset/image/details_open.png"></td>';
		  sOut = sOut + '<td id="id" align="center" style="display:none">'+value.id +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.name +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.duration +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.pass +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.fail +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.skip +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.success +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.startTime +'</td>';
		  sOut = sOut + '<td align="center" style="background-color:#F9F9F9">'+value.endTime +'</td>';
		  myArray[i] = sOut;
		  i++;
		});
	
	 return myArray;
	}
	
	$('#testResult td.testSuite').live( 'click', function () {
		
		if(testResultClassRow){
			return false;
		}
		
		var nTr = this.parentNode;
		var id = $(this).closest('td').next('td').html();    
		var i = $.inArray( nTr, testResultAnOpen );
		    
	   if ( i === -1 ) {
	      $('img', this).attr( 'src', "../asset/image/details_close.png" );
	      testResultPackageRow = true;
	      fnOpenNewRow( nTr, fnTestCaseDetails(testResultTable, id), 'details', testResultTable);
	      testResultAnOpen.push( nTr );
	    }
	    else {
	      $('img', this).attr( 'src', "../asset/image/details_open.png" );
	      testResultPackageRow = false;
	      fnCloseOpenedRow( nTr, testResultTable);
	      testResultAnOpen.splice( i, 1 );
	    }
	});
	
	function fnTestCaseDetails(testResultTable, id) {
		
	  var oData = testResultTable.fnGetData( testResultParentRow );
	  var testSuites = oData.testSuites;
	  var myArray = [];
	  
	  $.each(testSuites, function(key,value){
		if(value.id == id){
			var i= 0; 
			if(this.testCases){
				$.each(this.testCases, function(key,value){
				
				 var color ='';
				
				 if(value.status =='PASS'){
					 color = 'green';
				 } else if(value.status =='FAIL'){
					 color = 'red';
				 } else {
					 color = 'yellow';
				 }
					 
				 var sOut ='';
				 sOut = sOut + '<td class="testcases" align="right" style= "background-color:'+ color +'">';
				 sOut = sOut + '<td id="id" align="center" style= "display:none; background-color:'+ color +'">'+ value.id +'</td>';
				 sOut = sOut + '<td align="center" style= "background-color:'+ color +'">'+ value.name +'</td>';
				 sOut = sOut + '<td align="center" style= "background-color:'+ color +'">'+ value.duration +'</td>';
				 sOut = sOut + '<td align="left" style= "background-color:'+ color +'" colspan="6">'+ value.log +'</td>';
				 myArray[i] = sOut;
				 i++;
				});
			}
		}
	  });
		 
	return myArray;
	}
});