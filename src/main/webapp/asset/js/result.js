
var NameFormatter = function (row, cell, value, columnDef, dataContext) {
    value = value.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
    var spacer = "<span style='display:inline-block;height:1px;width:" + (15 * dataContext["indent"]) + "px'></span>";
    var idx = dataView.getIdxById(dataContext.id);

    if (data[idx + 1] && data[idx + 1].indent > data[idx].indent) {
        if (dataContext._collapsed) {
            return spacer + " <span class='toggle expand'></span>&nbsp;" + value;
        } else {
            return spacer + " <span class='toggle collapse'></span>&nbsp;" + value;
        }
    } else {
        if(dataContext["status"] == 'PASS') {
            return spacer + " <span class='toggle high'></span>&nbsp;" + "<span class='high'>" + value + "</span>";
        } else {
            return spacer + " <span class='toggle low'></span>&nbsp;" + "<span class='low'>" + value + "</span>";
        }
    }
};

var SuccessFormatter = function (row, cell, value, columnDef, dataContext) {

    if(value == null) {
        value = "";
    }

    if (value < 100) {
        return "<span class='low'>" + value + "</span>";
    }
    else {
        return "<span class='high'>" + value + "</span>";
    }
};

var dataView;
var grid;
var data = [];

var columns = [
    {id: "name", name: "Name", field: "name", cssClass: "cell-title", formatter: NameFormatter, width:500},
    {id: "type", name: "Type", field: "type"},
    {id: "duration", name: "Duration", field: "duration"},
    {id: "success", name: "Success %", field: "success", formatter: SuccessFormatter},
    {id: "total", name: "Total", field: "total"},
    {id: "pass", name: "Pass", field: "pass"},
    {id: "fail", name: "Fail", field: "fail"},
    {id: "skip", name: "Skip", field: "skip", width: 100}
];

var options = {};
var searchType = "";
var searchString = "";

function filter(item) {

    if (searchString != "" && ((((searchType == 'class' && item["indent"] == 1) || (searchType == 'method' && item["indent"] == 2)) &&
        item["name"].indexOf(searchString) == -1) || (searchType == 'coverage' && item["coverage"] > new Number(searchString)))) {
        return false;
    }

    return true;
}

$(function () {

    var appUrl = 'http://localhost:8080/apps/'
    var buildUrl = 'http://localhost:8080/app/'
    var resultUrl = 'http://localhost:8080/build/';
    var selectedApp;
    var selectedBuild;
    var response;

    dataView = new Slick.Data.DataView({ inlineFilters: true });
    dataView.setFilter(filter);

    // initialize the grid
    grid = new Slick.Grid("#grid", dataView, columns, options);

    grid.onClick.subscribe(function (e, args) {
        if ($(e.target).hasClass("toggle")) {
            var item = dataView.getItem(args.row);
            if (item) {
                if (!item._collapsed) {
                    item._collapsed = true;
                } else {
                    item._collapsed = false;
                }

                dataView.updateItem(item.id, item);
            }
            e.stopImmediatePropagation();
        }
    });


    // wire up model events to drive the grid
    dataView.onRowCountChanged.subscribe(function (e, args) {
        grid.updateRowCount();
        grid.render();
    });

    dataView.onRowsChanged.subscribe(function (e, args) {
        grid.invalidateRows(args.rows);
        grid.render();
    });

    // wire up the search textbox to apply the filter to the model
    $("#search").keyup(function (e) {
        Slick.GlobalEditorLock.cancelCurrentEdit();

        // clear on Esc
        if (e.which == 27) {
            this.value = "";
        }

        searchType = $('input[name=searchType]:checked').val();
        searchString = this.value;
        dataView.refresh();
    });

    $.ajax({
        dataType : "json",
        type : 'GET',
        async : false ,
        url : appUrl ,
        success : function(responseObject) {
            var applications = $("#app");
            applications.empty();
            $.each(responseObject, function(index, value) {
                applications.append('<option value="' + value + '">' + value + '</option>');
            });

            if (selectedApp == undefined) {
                selectedApp = $("#app option:first").val();
            }
            loadBuild();
        },
        error : function(xhr, status, error) {
            alert('failed : ' + error.Message);
        }
    });

    $("#app").change(function() {

        $("#app option:selected").each(function() {
            selectedApp = $(this).text();
        });
        loadBuild();
    })

    function loadBuild() {

        $.ajax({
            dataType : "json",
            type : 'GET',
            async : false,
            url : buildUrl + selectedApp + '/' + 'builds',
            success : function(responseObject) {
                var builds = $("#build");
                builds.empty();
                $.each(responseObject, function(index, value) {
                    builds.append('<option value="' + value.id + '">' + value.date + '</option>');
                });

                if (selectedBuild == undefined) {
                    selectedBuild = $("#build option:first").val();
                    loadResult();
                }
            }
        });
    }

    $("#build").change(function() {

        $("#build option:selected").each(function() {
            selectedBuild = $(this).val();
            loadResult();
        });
    })

    function loadResult() {
        $.ajax({
            type : 'GET',
            url : resultUrl + selectedBuild + '/result',
            dataType : 'json',
            success : function(responseObject) {
                data = responseObject;
                dataView.setItems(data);
            },
            error : function(xhr, status, error) {
                alert('failed : ' + error.Message);
            }
        });
    }

    var ajaxDataRenderer = function(url, plot, options) {
        var ret = null;
        $.ajax({
            async: false,
            url: url,
            dataType:"json",
            success: function(data) {
                ret = data;
            }
        });
        return ret;
    };

    var plot2 = $.jqplot('chart', buildUrl + selectedApp + '/result/trend',{
        dataRenderer: ajaxDataRenderer,
        axes:{
            xaxis:{
                tickOptions:{showLabel: false}
            }
        }
    });
});