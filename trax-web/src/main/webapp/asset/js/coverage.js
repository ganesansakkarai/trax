$(document).ready(function() {
	var oTable ;
	var slash = '/'
	var url = 'http://localhost:8080/coverage/summary/';
	var appListUrl = 'http://localhost:8080/applications/'
	var buildUrl = 'http://localhost:8080/builds/'
	var selectedApp;
	var selectedBuild;
	var testType = 'Unit';	
	var anOpen = [];
	var parentRow;
	var packageRow = false;
	var classRow = false;
	
	$(function() {
		$("#testTabs").tabs();
	});
	
	$.ajax({
		dataType : "json",
		type : 'POST',
		async : false,
		url : appListUrl + testType,
		success : function(responseObject) {
			var applications = $("#applicationType");
			applications.empty();
			$.each(responseObject, function( index, value ) {
				applications.append('<option value="' + value + '">'+ value + '</option>');
			});
			
			if(selectedApp == undefined) {
				selectedApp = $("#applicationType option:first").val();
			}
			loadBuild();
		},
		error : function(xhr, status, error) {
			alert('failed : ' + err.Message);
		}
	});
	
	$("#applicationType").change(function () {
		
		$( "#applicationType option:selected" ).each(function() {
			selectedApp = $( this ).text();
		});
		loadBuild();
	})
	
	function loadBuild(){
		$.ajax({
			dataType : "json",
			type : 'POST',
			async : false,
			url : buildUrl + selectedApp + '/' + testType, 
			success : function(responseObject) {
				var builds = $("#builds");
				builds.empty();
				$.each(responseObject, function( index, value ) {
					builds.append('<option value="' + value + '">'+ value + '</option>');
				});
				
				if(selectedBuild == undefined) {
					selectedBuild = $("#builds option:first").val();
				}
			  refreshTable();
			}
		});
	}
	
	$("#builds").change(function () {
		
		$( "#builds option:selected" ).each(function() {
			  selectedBuild = $( this ).text();
			  refreshTable();
		});
	})
	
	function refreshTable() {
		
		packageRow = false;
		classRow = false;
		
		oTable = $('#coverage').dataTable({
			"bJQueryUI" : true,
			"bDestroy": true,
			"sPaginationType" : "full_numbers",
			"bServerSide" : true,
			"sAjaxSource" :  url + selectedApp + '/' + testType + '/' + selectedBuild,
			"sServerMethod" : "POST",
			"aoColumns" : [ 
			{
			     "mDataProp": null,
			     "sClass": "package left",
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
			}, ],
			"aoColumnDefs" : [ 
			          {"sClass" : "center","aTargets" : [2,3,4,5,6,7]},
			          { "bVisible": false, "aTargets": [ 1 ] }]
		});
		
		$("#coverage").removeAttr("style");
	}
	
	$('#coverage td.package').live( 'click', function () {
		
		if(packageRow || classRow){
			return false;
		}
		var nTr = this.parentNode;
		parentRow = nTr;
		var i = $.inArray( nTr, anOpen );
	    
		if ( i === -1 ) {
			$('img', this).attr( 'src', "../asset/image/details_close.png" );
			fnOpenNewRow( nTr, fnClazzesDetails(oTable, nTr), 'details' );
			anOpen.push( nTr );
	    } else {
	    	$('img', this).attr( 'src', "../asset/image/details_open.png" );
	    	fnCloseOpenedRow( nTr );
	    	anOpen.splice( i, 1 );
	    }
		
	});
		 
		function fnClazzesDetails( oTable, nTr )
		{
		
		  var oData = oTable.fnGetData( nTr );
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
			
			if(classRow){
				return false;
			}
			
			var nTr = this.parentNode;
			var id = $(this).closest('td').next('td').html();    
			var i = $.inArray( nTr, anOpen );
			    
		   if ( i === -1 ) {
		      $('img', this).attr( 'src', "../asset/image/details_close.png" );
		      packageRow = true;
		      fnOpenNewRow( nTr, fnMethodsDetails(oTable, id), 'details' );
		      anOpen.push( nTr );
		    }
		    else {
		      $('img', this).attr( 'src', "../asset/image/details_open.png" );
		      packageRow = false;
		      fnCloseOpenedRow( nTr );
		      anOpen.splice( i, 1 );
		    }
		});
		
		function fnMethodsDetails(oTable, id) {
			
		  var oData = oTable.fnGetData( parentRow );
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
		
		fnOpenNewRow = function(nTr, myArray, sClass ) {
			
			var oSettings = oTable.fnSettings();
			oTable.fnClose( nTr );
			for ( var i in myArray ) {
				var nNewRow = document.createElement("tr");
				if (typeof mHtml === "string"){
					nNewRow.innerHTML = myArray[ i ];
				}
				else{
					$(nNewRow).html( myArray[ i ] );
				}
			
				var nTrs = $('tr', oSettings.nTBody);
				if ( $.inArray(nTr, nTrs) != -1  ){
					$(nNewRow).insertAfter(nTr);
				}
				
				oSettings.aoOpenRows.push( {
					"nTr": nNewRow,
					"nParent": nTr
				} );
			}
			return nNewRow;
		};
		
		
		fnCloseOpenedRow = function( nTr ) {
			
			var oSettings = oTable.fnSettings();
			var index = oSettings.aoOpenRows.length - 1;
			for ( var i=index; i>=0 ; i-- )
			{
				if ( oSettings.aoOpenRows[i].nParent == nTr )
				{
					var nTrParent = oSettings.aoOpenRows[i].nTr.parentNode;
					if ( nTrParent )
					{
						nTrParent.removeChild( oSettings.aoOpenRows[i].nTr );
					}
					oSettings.aoOpenRows.splice( i, 1 );
				}
			}
			return 1;
		};

});