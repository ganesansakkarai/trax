
var TaskNameFormatter = function (row, cell, value, columnDef, dataContext) {
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
        return spacer + " <span class='toggle'></span>&nbsp;" + value;
    }
};

var dataView;
var grid;
var data = [];

var columns = [
    {id: "name", name: "Name", field: "name", cssClass: "cell-title", formatter: TaskNameFormatter, width:500},
    {id: "type", name: "Type", field: "type"},
    {id: "coverage", name: "Coverage", field: "coverage"},
    {id: "line", name: "Line", field: "line"},
    {id: "missedLine", name: "Missed Line", field: "missedLine"},
    {id: "branch", name: "Branch", field: "branch"},
    {id: "missedBranch", name: "Missed Branch", field: "missedBranch", width: 100}
];

var options = {};

var searchString = "";

function myFilter(item) {

    if (searchString != "" && item["name"].indexOf(searchString) == -1) {
        return false;
    }

    if (item.parent != null) {// initialize the model
        var parent = data[item.parent];

        while (parent) {
            if (parent._collapsed || (searchString != "" && parent["name"].indexOf(searchString) == -1)) {
                return false;
            }

            parent = data[parent.parent];
        }
    }

    return true;
}

$(function () {

    var appUrl = 'http://localhost:8080/apps/'
    var buildUrl = 'http://localhost:8080/app/'
    var coverageUrl = 'http://localhost:8080/build/';
    var selectedApp;
    var selectedBuild;
    var response;

    dataView = new Slick.Data.DataView({ inlineFilters: true });
    dataView.setFilter(myFilter);

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
    $("#txtSearch").keyup(function (e) {
        Slick.GlobalEditorLock.cancelCurrentEdit();

        // clear on Esc
        if (e.which == 27) {
            this.value = "";
        }

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
                    loadCoverage();
                }


                //refreshTestResults();
            }
        });
    }

    $("#build").change(function() {

        $("#build option:selected").each(function() {
            selectedBuild = $(this).val();
            loadCoverage();
            //refreshTestResults();
        });
    })

    function loadCoverage() {
        $.ajax({
            type : 'GET',
            url : coverageUrl + selectedBuild + '/coverage',
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
});