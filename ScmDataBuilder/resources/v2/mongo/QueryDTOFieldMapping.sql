use scm;
db.query_dto_field_mapping.update({
	"jobName": "COURIER_GROUP"
}, {
	"_id": ObjectId("56d558b1487f905c6c6014ac"),
	"_class": "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName": "COURIER_GROUP",
	"jobClass": "com.snapdeal.scm.core.dto.impl.CourierGroupDTO",
	"fieldMapping": [{
		"field": "integrated",
		"fieldType": "java.lang.Boolean",
		"queryFieldIndex": 9
	}, {
		"field": "wmsRlCode",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 8
	}, {
		"field": "courierGroup",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 7
	}, {
		"field": "courierType",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 6
	}, {
		"field": "transitTypeCode",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 5
	}, {
		"field": "shippingModeCode",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 4
	}, {
		"field": "code",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 3
	}, {
		"field": "name",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 2
	}, {
		"field": "shippingProvideSid",
		"fieldType": "java.lang.Integer",
		"queryFieldIndex": 1
	}, {
		"field": "id",
		"fieldType": "java.lang.Integer",
		"queryFieldIndex": 0
	}],
	"created": ISODate("2016-03-01T08:54:09.364Z"),
	"updated": ISODate("2016-03-01T08:54:09.364Z")
});


db.query_dto_field_mapping.insert({
	"_class": "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName": "PINCODE_DC_MAPPING",
	"jobClass": "com.snapdeal.scm.core.dto.impl.PincodeDCMappingDTO",
	"fieldMapping": [{
			"field": "pincode",
			"fieldType": "java.lang.String",
			"queryFieldIndex": 0
		}, {
			"field": "courierGroup",
			"fieldType": "java.lang.String",
			"queryFieldIndex": 1
		}, {
			"field": "shippingModeCode",
			"fieldType": "java.lang.String",
			"queryFieldIndex": 2
		}, {
			"field": "deliveryCenter",
			"fieldType": "java.lang.String",
			"queryFieldIndex": 3
		}, {
			"field": "dcCity",
			"fieldType": "java.lang.String",
			"queryFieldIndex": 4
		}, {
			"field": "dcState",
			"fieldType": "java.lang.String",
			"queryFieldIndex": 5
		},

	],
	"created": new Date(),
	"updated": new Date()
});

db.query_dto_field_mapping.insert({
	"_class": "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName": "ORIGIN_CITY_EXIT_MAPPING",
	"jobClass": "com.snapdeal.scm.core.dto.impl.OriginCityExitMappingDTO",
	"fieldMapping": [{
		"field": "courierGroup",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 0
	}, {
		"field": "shippingMode",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 1
	}, {
		"field": "firstLocationCity",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 2
	}, {
		"field": "firstLocationState",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 3
	}, {
		"field": "courierStatus",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 4
	}, {
		"field": "courierRemarks",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 5
	}, {
		"field": "snapdealStatus",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 6
	}, {
		"field": "firstLocation",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 7
	}, {
		"field": "destinationCity",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 8
	}, {
		"field": "destinationState",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 9
	}, {
		"field": "originCityExit",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 10
	}],
	"created": new Date(),
	"updated": new Date()
});
db.query_dto_field_mapping.insert({
	"_class": "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName": "COURIER_LOCATION_SNAPDEAL_LOCATION_MAPPING",
	"jobClass": "com.snapdeal.scm.core.dto.impl.CourierLocationSnapdealLocationMappingDTO",
	"fieldMapping": [{
		"field": "id",
		"fieldType": "java.lang.Integer",
		"queryFieldIndex": 0
	}, {
		"field": "courierCode",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 1
	}, {
		"field": "locationCode",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 2
	}, {
		"field": "snapdealLocationCity",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 3
	}, {
		"field": "snapdealLocationState",
		"fieldType": "java.lang.String",
		"queryFieldIndex": 4
	}, {
		"field": "enabled",
		"fieldType": "java.lang.Boolean",
		"queryFieldIndex": 5
	}],
	"created": new Date(),
	"updated": new Date()
});

db.query_dto_field_mapping.remove({"jobName" : "SUB_ORDER_TP_STATUS_CODES"})

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SUB_ORDER_TP_STATUS_CODES",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.SubOrderTpStatusCodesDTO",
	"fieldMapping" : [
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		},
		{
			"field" : "newStatusCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "newStatusDate",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 2
		},
		{
			"field" : "currentLocationCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 3
		},
		{
			"field" : "currentLocationPincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 4
		},
		{
			"field" : "remarks",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 5
		},
		{
			"field" : "courierStatus",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 6
		},
		{
			"field" : "originHubLocationPincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 7
		},
		{
			"field" : "destinationHubLocationPincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 8
		},
		{
			"field" : "currentStatus",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 9
		},
		{
			"field" : "currentStatusDate",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 10
		},
		{
			"field" : "rejectCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 11
		},
		{
			"field" : "oldStatusCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 12
		},
		{
			"field" : "courierCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 13
		},
		{
			"field" : "description",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 14
		}
	],
	"created": new Date(),
	"updated": new Date()
});

db.query_dto_field_mapping.remove({"jobName" : "SHIPPING_SOI_SD"});


db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SHIPPING_SOI_SD",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.ShippingSoiSdDTO",
	"fieldMapping" : [
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		},
		{
			"field" : "supc",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "customerDestinationPincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 2
		},
		{
			"field" : "centerCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 3
		},
		{
			"field" : "fullfilmentModel",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 4
		},
		{
			"field" : "orderCreatedDate",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 5
		},
		{
			"field" : "vendorCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 6
		},
		{
			"field" : "onHold",
			"fieldType" : "java.lang.Boolean",
			"queryFieldIndex" : 7
		},
		{
			"field" : "itemPrice",
			"fieldType" : "java.lang.Double",
			"queryFieldIndex" : 8
		},
		{
			"field" : "paymentMode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 9
		},
		{
			"field" : "created",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 10
		}
	],
	"created": new Date(),
	"updated": new Date()
});
