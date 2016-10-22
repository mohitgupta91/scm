use scm; 
db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SHIPPING_SOI_SOID",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.ShippingSoiSoidDTO",
	"fieldMapping" : [
		{
			"field" : "attributeValue",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 2
		},
		{
			"field" : "attributeName",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SHIPPING_SOI_SD",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.ShippingSoiSdDTO",
	"fieldMapping" : [
		{
			"field" : "vendorCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 6
		},
		{
			"field" : "orderCreatedDate",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 5
		},
		{
			"field" : "fullfilmentModel",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 4
		},
		{
			"field" : "centerCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 3
		},
		{
			"field" : "destinationPincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 2
		},
		{
			"field" : "supc",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SHIPPING_SOI_SP",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.ShippingSoiSpDTO",
	"fieldMapping" : [
		{
			"field" : "expectedDeliveryDateRangeStart",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 3
		},
		{
			"field" : "expectedDeliveryDate",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 2
		},
		{
			"field" : "courierCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SUB_ORDER_SOI_STATUS_CODES",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.SubOrderSoiStatusCodesDTO",
	"fieldMapping" : [
		{
			"field" : "created",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 2
		},
		{
			"field" : "statusCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SUB_ORDER_TP_STATUS_CODES",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.SubOrderTpStatusCodesDTO",
	"fieldMapping" : [
		{
			"field" : "created",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 2
		},
		{
			"field" : "statusCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "SUB_ORDER_SP_STATUS_CODES",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.SubOrderSpStatusCodesDTO",
	"fieldMapping" : [
		{
			"field" : "created",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 2
		},
		{
			"field" : "statusCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "subOrderCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "CAMS_SUPC_SUPER_CATEGORY",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.CamsSupcSuperCategoryDTO",
	"fieldMapping" : [
		{
			"field" : "superCategory",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "supc",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "CENTER_MASTER",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.CenterMasterDTO",
	"fieldMapping" : [
		{
			"field" : "centerType",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 19
		},
		{
			"field" : "shipNearZone",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 18
		},
		{
			"field" : "scZone",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 17
		},
		{
			"field" : "scState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 16
		},
		{
			"field" : "scCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 15
		},
		{
			"field" : "bdeZone",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 14
		},
		{
			"field" : "logiopsZone",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 13
		},
		{
			"field" : "logiopsState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 12
		},
		{
			"field" : "logiopsCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 11
		},
		{
			"field" : "scoreState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 10
		},
		{
			"field" : "scoreTier",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 9
		},
		{
			"field" : "scoreCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 8
		},
		{
			"field" : "updated",
			"fieldType" : "java.util.Date",
			"queryFieldIndex" : 7
		},
		{
			"field" : "state",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 6
		},
		{
			"field" : "city",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 5
		},
		{
			"field" : "pincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 4
		},
		{
			"field" : "type",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 3
		},
		{
			"field" : "enabled",
			"fieldType" : "java.lang.Boolean",
			"queryFieldIndex" : 2
		},
		{
			"field" : "name",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "code",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "COURIER_GROUP",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.CourierGroupDTO",
	"fieldMapping" : [
		{
			"field" : "wmsRlCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 8
		},
		{
			"field" : "courierGroup",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 7
		},
		{
			"field" : "courierType",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 6
		},
		{
			"field" : "transitTypeCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 5
		},
		{
			"field" : "shippingModeCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 4
		},
		{
			"field" : "code",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 3
		},
		{
			"field" : "name",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 2
		},
		{
			"field" : "shippingProvideSid",
			"fieldType" : "java.lang.Integer",
			"queryFieldIndex" : 1
		},
		{
			"field" : "id",
			"fieldType" : "java.lang.Integer",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "FILMS_SUPC_MTO",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.FilmsSupcMtoDTO",
	"fieldMapping" : [
		{
			"field" : "mto",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "supc",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});


db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "PINCODE_MASTER",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.PincodeMasterDTO",
	"fieldMapping" : [
		{
			"field" : "redexState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 17
		},
		{
			"field" : "redexCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 16
		},
		{
			"field" : "gjState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 15
		},
		{
			"field" : "gjCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 14
		},
		{
			"field" : "bdState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 13
		},
		{
			"field" : "bdCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 12
		},
		{
			"field" : "dvlState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 11
		},
		{
			"field" : "dvlCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 10
		},
		{
			"field" : "ecomState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 9
		},
		{
			"field" : "ecomCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 8
		},
		{
			"field" : "scZone",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 7
		},
		{
			"field" : "scState",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 6
		},
		{
			"field" : "scCity",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 5
		},
		{
			"field" : "bdeZone",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 4
		},
		{
			"field" : "zone",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 3
		},
		{
			"field" : "state",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 2
		},
		{
			"field" : "city",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		},
		{
			"field" : "pincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});

db.query_dto_field_mapping.insert({
	"_class" : "com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping",
	"jobName" : "VENDOR_CONTACT",
	"jobClass" : "com.snapdeal.scm.core.dto.impl.VendorContactDTO",
	"fieldMapping" : [
		{
			"field" : "pincode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 0
		},
		{
			"field" : "vendorCode",
			"fieldType" : "java.lang.String",
			"queryFieldIndex" : 1
		}
	],
	"created" : new Date(),
	"updated" : new Date()
});
