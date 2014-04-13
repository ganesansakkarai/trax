var oTable ;
function loadApplication(buildId)
	{
	if(buildId==undefined)
	{
		// Change this default value as per your record in db.
		buildId='2014-04-13T21:47:39:874+0530';
	}	
	oTable = $('#coverage').dataTable({
			"bJQueryUI" : true,
			"bDestroy": true,
			"sPaginationType" : "full_numbers",
			"bServerSide" : true,
			"sAjaxSource" : "http://171.76.154.222:8080/coverage/summary/Sample/Unit/"+buildId,
			"sServerMethod" : "POST",
			"aoColumns" : [ 
			{
			     "mDataProp": null,
			     "sClass": "application left",
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
	}

$(document).ready(function() {
	
	var defaultApplication = '';
	$.ajax({
		dataType : "json",
		type : 'POST',
		async : false,
		url : 'http://171.76.154.222:8080/applications/Unit/',
		success : function(responseObject) {
			coverage = responseObject[0];
			var sel = $("#applicationType");
			sel.empty();
			for ( var i = 0; i < responseObject.length; i++) {
				if(i==0)
					{
					defaultApplication = responseObject[i];
					}
				sel.append('<option value="' + responseObject[i] + '">'
						+ responseObject[i] + '</option>');
			}
		},
		error : function(e, ts, et) {
			alert('fail' + ts);
		}
	});
	
	loadApplication(); 
	
	
		
	$.ajax({
		dataType : "json",
		type : 'POST',
		async : false,
		url : 'http://171.76.154.222:8080/builds/'+defaultApplication+'/Unit/',
		success : function(responseObject) {
			coverage = responseObject[0];
			var sel = $("#builds");
			sel.empty();
			for ( var i = 0; i < responseObject.length; i++) {
				sel.append('<option value="' + responseObject[i] + '">'
						+ responseObject[i] + '</option>');
			}
		},
		error : function(e, ts, et) {
			alert('fail::' + ts);
		}
	});
	
	var anOpen = [];
	var parentRow;
	var packageRow = false;
	var classRow = false;
	
	
	
	$("#coverage").removeAttr("style");
	
	$('#coverage td.application').live( 'click', function () {
			if(packageRow || classRow){
				return false;
			}
		   var nTr = this.parentNode;
		   parentRow = nTr;
		   var i = $.inArray( nTr, anOpen );
		    
		   if ( i === -1 ) {
		      $('img', this).attr( 'src', "../asset/image/details_close.png" );
		      fnOpenNewRow( nTr, fnPackageDetails(oTable, nTr), 'details' );
		      anOpen.push( nTr );
		    }
		    else {
		      $('img', this).attr( 'src', "../asset/image/details_open.png" );
		      fnCloseOpenedRow( nTr );
		      anOpen.splice( i, 1 );
		    }
		} );
		 
		function fnPackageDetails( oTable, nTr )
		{
		
		  var oData = oTable.fnGetData( nTr );
		  var packages = oData.packages;
		
		  var myArray = [];
		  var i= 0; 
		  $.each(packages, function(key,value){
			  var sOut ='';
			  sOut = sOut + '<td class="package center" style="background-color:#F9F9F9"><img src="../asset/image/details_open.png"></td>';
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
		
		$('#coverage td.package').live( 'click', function () {
			if(classRow){
				return false;
			}
			
			var nTr = this.parentNode;
			var id = $(this).closest('td').next('td').html();    
			var i = $.inArray( nTr, anOpen );
			    
		   if ( i === -1 ) {
		      $('img', this).attr( 'src', "../asset/image/details_close.png" );
		      packageRow = true;
		      fnOpenNewRow( nTr, fnClassDetails(oTable, id), 'details' );
		      anOpen.push( nTr );
		    }
		    else {
		      $('img', this).attr( 'src', "../asset/image/details_open.png" );
		      packageRow = false;
		      fnCloseOpenedRow( nTr );
		      anOpen.splice( i, 1 );
		    }
		});
		
		function fnClassDetails(oTable, id) {
		  var oData = oTable.fnGetData( parentRow );
		  var packages = oData.packages;
		  var myArray = [];
		  
		  $.each(packages, function(key,value){
			if(value.id == id){
				var i= 0; 
				if(this.clazzes){
					$.each(this.clazzes, function(key,value){
					 var sOut ='';
					 sOut = sOut + '<td class="classes" align="right" style="background-color:#EAEAEA"><img src="../asset/image/details_open.png"></td>';
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
		
		
		
		$('#coverage td.classes').live( 'click', function () {
			   
			var nTr = this.parentNode;
			var id = $(this).closest('td').next('td').html();    
			var i = $.inArray( nTr, anOpen );
			    
		   if ( i === -1 ) {
		      $('img', this).attr( 'src', "../asset/image/details_close.png" );
		      classRow = true;
		      fnOpenNewRow( nTr, fnMethodsDetails(oTable, id), 'details' );
		      anOpen.push( nTr );
		    }
		    else {
		      $('img', this).attr( 'src', "../asset/image/details_open.png" );
		      classRow = false;
		      fnCloseOpenedRow( nTr );
		      anOpen.splice( i, 1 );
		    }
		});
		
		function fnMethodsDetails(oTable, id) {
		  var oData = oTable.fnGetData( parentRow );
		  var packages = oData.packages;
		  var myArray = [];
		  
		  $.each(packages, function(key,value){
			if(value.id == id){
				if(this.clazzes){
					$.each(this.clazzes, function(key,value){
						if(value.id == id){
							var i= 0; 
							if(this.methods){
								$.each(this.methods, function(key,value){
								var sOut ='';
								 sOut = sOut + '<td class="methods " style="background-color:#D6D6D6"></td>';
								 sOut = sOut + '<td id="id" align="center" style="display:none;background-color:#D6D6D6">'+value.id +'</td>';
								 sOut = sOut + '<td align="center" style="background-color:#D6D6D6">'+value.name +'</td>';
								 sOut = sOut + '<td align="center" style="background-color:#D6D6D6">'+value.coverage +'</td>';
								 sOut = sOut + '<td align="center" style="background-color:#D6D6D6">'+value.line +'</td>';
								 sOut = sOut + '<td align="center" style="background-color:#D6D6D6">'+value.missedLine +'</td>';
								 sOut = sOut + '<td align="center" style="background-color:#D6D6D6">'+value.branch +'</td>';
								 sOut = sOut + '<td align="center" style="background-color:#D6D6D6">'+value.missedBranch +'</td>';
								 myArray[i] = sOut;
								 i++;
								});
							}	
						}
					});
				}
			}
		  });
			 
		return myArray;
		}
		
		
		fnOpenNewRow = function(nTr, myArray, sClass )
		{
			var oSettings = oTable.fnSettings();
			
			oTable.fnClose( nTr );
			
			for ( var i in myArray ) {
				var nNewRow = document.createElement("tr");
				
				if (typeof mHtml === "string")
				{
					nNewRow.innerHTML = myArray[ i ];
				}
				else
				{
					$(nNewRow).html( myArray[ i ] );
				}
			
				var nTrs = $('tr', oSettings.nTBody);
				if ( $.inArray(nTr, nTrs) != -1  )
				{
					$(nNewRow).insertAfter(nTr);
				}
				
				oSettings.aoOpenRows.push( {
					"nTr": nNewRow,
					"nParent": nTr
				} );
			}
			return nNewRow;
		};
		
		
		fnCloseOpenedRow = function( nTr )
		{
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