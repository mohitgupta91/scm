define({
    name: "spamjs.datatable.test",
    extend: "spamjs.view",
    modules: ["jQuery", "spamjs.datatable"]
}).as(function(test, jq, datatable) {
    return {
        _init_: function() {
            //datatable
            //this.$$
            var dummyEditor = function() {
                return "Ayuhs";
            };
            this.$$.html("<div id='something'></div>");
            this.add(datatable.instance({
                id: "mytesttable",
                cellOptions: [{
                    'width': '100px',
                    'text-align': 'left'
                }, {
                    'width': '200px',
                    'text-align': 'left'
                }, {
                    'width': '200px',
                    'text-align': 'left'
                }, {
                    'width': '200px',
                    'text-align': 'left'
                }, {
                    'width': '200px',
                    'text-align': 'left'
                }, {
                    'width': '200px',
                    'text-align': 'left'
                }],
                ele: jq("#something"),
                data: [
                    {
                        "name":       "Tiger Nixon",
                        "position":   "System Architect",
                        "salary":     "$3,120",
                        "office":     "Edinburgh",
                        "office1":     "Edinburgh",
                        "office2":     "Edinburgh"
                    },{
                        "name":       "Tiger Nixon",
                        "position":   "System Architect",
                        "salary":     "$3,120",
                        "office":     "Edinburgh",
                        "office1":     "Edinburgh",
                        "office2":     "Edinburgh"
                    },{
                        "name":       "Tiger Nixon",
                        "position":   "System Architect",
                        "salary":     "$120",
                        "office":     "Edinburgh",
                        "office1":     "Edinburgh",
                        "office2":     "Edinburgh"
                    },{
                        "name":       "Tiger Nixon",
                        "position":   "System Architect",
                        "salary":     "$3,120",
                        "office":     "Edinburgh",
                        "office1":     "Edinburgh",
                        "office2":     "Edinburgh"
                    },{
                        "name":       "Tiger Nixon",
                        "position":   "System Architect",
                        "salary":     "$3,120",
                        "office":     "Edinburgh",
                        "office1":     "Edinburgh",
                        "office2":     "Edinburgh"
                    },
                    {
                        "name":       "Tiger Nixon",
                        "position":   "System Architect",
                        "salary":     "$3,120",
                        "office":     "Edinburgh" ,
                        "office1":     "Edinburgh",
                        "office2":     "Edinburgh"
                    }
                ],
                rowTemplate: "<div></div>",
                "iDisplayLength": 4,
                "bPaginate": false,
                "autoWidth": false,
                "columns": [],
                "bInfo": false,
                "bLengthChange": false,
                "bFilter": false,
                columns: [
                    {
                        data: "name",
                        title: "Name",
                        formatter: dummyEditor
                    },
                    {
                        data: "position",
                        title: "Position"
                    },
                    {
                        data: "salary",
                        title: "Salary"
                    },
                    {
                        data: "office",
                        title: "Office"
                    },
                    {
                        data: "office1",
                        title: "Office1"
                    },
                    {
                        data: "office2",
                        title: "Office2"
                    }
                ]
            }));
        }
    };
});