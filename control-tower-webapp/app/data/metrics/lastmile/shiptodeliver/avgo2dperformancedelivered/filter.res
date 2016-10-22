{
    "data": [{
        "name": "durationtype",
        "dataUrl": "",
        "dependentOptions": {"air":["a","b","c"],"surface":["x","y","z"]},
        "default": "",
        "options": [],
        "dependsOn": "mode",
        "mandatory": null
    }, {
        "name": "mode",
        "dataUrl": "",
        "dependentOptions": [],
        "default": "",
        "options": ["air","surface"],
        "mandatory": null,
        "dependsOn": null
    }, {
        "name": "type",
        "dataUrl": "",
        "dependentOptions": {"MY":["a","b","c"],"MY1":["x","y","z"],"MY2":["test1","test2","test3"]},
        "default": "",
        "options": [],
        "mandatory": null,
        "dependsOn": "metro"
    }, {
        "name": "metro",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "options": ["MY","MY2","MY1"],
        "mandatory": null,
        "dependsOn": null
    }, {
        "name": "paidndd",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "options": ["MY","MY2","MY1"],
        "mandatory": null,
        "dependsOn": null
    }, {
        "name": "fulfilmentmodel",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "options": ["MY","MY2","MY1"],
        "mandatory": null,
        "dependsOn": null
    }, {
        "name": "lanetype",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "mandatory": null,
        "options": ["metrotometro","ROI","samecity", "zonal"],
        "dependsOn": null
    }, {
        "name": "lane",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "mandatory": null,
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "originstate",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "mandatory": null,
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "destinationregion",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "mandatory": null,
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "destinationtier",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "mandatory": null,
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "couriergroup",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "",
        "mandatory": null,
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "couriertype",
        "dataUrl": "",
        "dependentOptions": null,
        "type": "multiselect",
        "default": "",
        "mandatory": null,
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "delivereddate",
        "dataUrl": "",
        "type": "date",
        "dependentOptions": null,
        "default": "",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null,
        "mandatory": null,
        "range": "30-0"
    },{
        "name": "state",
        "dataUrl": "",
        "type": "",
        "dependentOptions": null,
        "default": "",
        "mandatory": null,
        "options": ["Punjab", "Haryana"],
        "dependsOn": null
    },{
        "name": "City",
        "dataUrl": "metrics/lastmile/shiptodeliver/avgo2dperformancedelivered/multiselectCity",
        "type": "multiselect",
        "dependentOptions": null,
        "default": null,
        "mandatory": null,
        "options": null,
        "dependsOn": "state"
    }],
    "defaultFilters": [{
        "name": "something",
        "value": ""
    }]
}