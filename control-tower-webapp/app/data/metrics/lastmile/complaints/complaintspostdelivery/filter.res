{
    "data": [{
        "name": "durationtype",
        "dataUrl": "",
        "dependentOptions": {"air":["a","b","c"],"surface":["x","y","z"]},
        "default": "",
        "options": [],
        "dependsOn": "mode"
    }, {
        "name": "mode",
        "dataUrl": "",
        "dependentOptions": [],
        "default": "air",
        "options": ["air","surface"],
        "dependsOn": null
    }, {
        "name": "type",
        "dataUrl": "",
        "dependentOptions": {"MY":["a","b","c"],"MY1":["x","y","z"],"MY2":["test1","test2","test3"]},
        "default": "",
        "options": [],
        "dependsOn": "metro"
    }, {
        "name": "metro",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "paidndd",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "fulfilmentmodel",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "lanetype",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "lane",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "originstate",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "destinationregion",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "destinationtier",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "couriergroup",
        "dataUrl": "",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "couriertype",
        "dataUrl": "",
        "dependentOptions": null,
        "type": "multiSelect",
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }, {
        "name": "delivereddate",
        "dataUrl": "",
        "type": "date",
        "dependentOptions": null,
        "default": "MY2",
        "options": ["MY","MY2","MY1"],
        "dependsOn": null
    }],
    "defaultFilters": [{
        "name": "something",
        "value": ""
    }]
}