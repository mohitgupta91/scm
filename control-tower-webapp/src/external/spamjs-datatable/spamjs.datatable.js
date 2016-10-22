define({
    name: "spamjs.datatable",
    extend: "spamjs.view",
    modules: ["jQuery", "_"]
}).as(function(datatable, jq, _) {
//    events:{
//        "click #saveConfig" : "saveConfig"
//    },
    return {
        _init_: function() {
            var self = this;
            var ele = jq(self.options.ele);
            var columns = self.options.columns;
            var cellOptions = self.options.cellOptions;
            ele.html("<table><thead></thead></table>");
            if(columns) {
                var content = "";
                //_.each()
                ele.find("tr").html(content);
                var p = $.extend({
                    iDisplayLength: 4,
                    bPaginate: false,
                    scrollY: 100,
                    paging: false,
                    bInfo: false,
                    fixedHeader: true,
                    fnInitComplete: function() {
                        if(cellOptions && cellOptions.length) {
                            var rowElements = jq(self.nHead).find("th");
                            _.each(rowElements, function(element, i) {
                                element.innerHTML = "<div class='db-header'>" + 
                                    "<span class='filter-values'></span>" +
                                    "<div class='header-text cooltooltip'>" + element.innerHTML + "</div>" +
                                    "</div";
                                element = jq(element).find('.db-header')[0];
                                if(cellOptions[i]) {
                                    _.each(cellOptions[i], function(val, key) {
                                        element.style[key] = val;
                                    });
                                }
                            });
                        }
                        jq(".dataTables_scrollBody").off("scroll");
                        jq(".dataTables_scrollBody").scroll(function(e) {
                            var header = jq(this).parent().find('.dataTables_scrollHead');
                            header[0].scrollLeft = this.scrollLeft;
                        });
                    },
                    "fnHeaderCallback": function(nHead, aData, iStart, iEnd, aiDisplay) {
                        self.nHead = nHead;
                    },
                    fnRowCallback: function(row) {
                        if(cellOptions && cellOptions.length) {
                            var rowElements = jq(row).find("td");
                            _.each(rowElements, function(element, i) {
                                var content = jq(element).find(".row-content").html();
                                if(!content) {
                                    content = element.innerHTML;
                                }
                                jq(element).find(".row-content").remove();
                                element.innerHTML = "<div class='row-content'>" + content + "</div";
                                element = jq(element).find(".row-content")[0];
                                if(cellOptions[i]) {
                                    _.each(cellOptions[i], function(val, key) {
                                        element.style[key] = val;
                                    });
                                }
                            });
                        }
                    },
                    "columnDefs": [],
                    bLengthChange: false,
                    "autoWidth": true,
                    "columns": [],
                    bFilter: false,
                    bSortCellsTop: true
                }, self.options);
                ele.find("table").dataTable(p);
            } else {
                console.error("Columns fields is mandatory");
            }
        }
        //jqtag.trigger(self.$$[0], "spamjs.model.closed");
    };
});