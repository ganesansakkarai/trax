
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
    {id: "mline", name: "Missed Line", field: "mline"},
    {id: "branch", name: "Branch", field: "branch"},
    {id: "mbranch", name: "Missed Branch", field: "mbranch", width: 100}
];

var options = {};

var searchString = "";

function myFilter(item) {

    if (searchString != "" && item["name"].indexOf(searchString) == -1) {
        return false;
    }

    if (item.parent != null) {
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

    var response = null;
    $.post("http://localhost:8080/app/single/builds", function(data) {
       response = data;
       alert("Data : " + response);
    });

    // prepare the data
    data[0] = {id: 'id_' + 1, indent:0, parent:null, type: "Unit", name:"Sample", coverage:80.0, line:10, mline:0, branch:3, mbranch:2};
    data[1] = {id: 'id_' + 2, indent:1, parent:0, type: "Unit", name:"Util", coverage:80.0, line:10, mline:0, branch:3, mbranch:2};
    data[2] = {id: 'id_' + 3, indent:2, parent:1, type: "Unit", name:"org.kits.trax.HttpUtil", coverage:80.0, line:10, mline:0, branch:3, mbranch:2};

    // initialize the model
    dataView = new Slick.Data.DataView({ inlineFilters: true });
    dataView.beginUpdate();
    dataView.setItems(data);
    dataView.setFilter(myFilter);
    dataView.endUpdate();

    // initialize the grid
    grid = new Slick.Grid("#myGrid", dataView, columns, options);

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
});