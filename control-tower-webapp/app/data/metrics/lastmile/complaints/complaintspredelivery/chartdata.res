{
	"code": 200,
	"ONE": {
		"type": "category",
		"dataset": [{
			"series": {
				"before_sla": [
					["Dec 26", 10000], ["Dec 27", 4000], ["Dec 28", 3000], ["Dec 29", 1000], ["Dec 30", 1300], ["Dec 31", 1010]
				],
				"after_sla-stuck": [
					["Dec 26", 1800], ["Dec 27", 4000], ["Dec 28", 3000], ["Dec 29", 1000], ["Dec 30", 1300], ["Dec 31", 1010]
				],
				"after_sla_awb_not_traceable": [
					["Dec 26", 1800], ["Dec 27", 4000], ["Dec 28", 3000], ["Dec 29", 1000], ["Dec 30", 1300], ["Dec 31", 1010]
				],
				"cancellation_request_pre_deliver": [
					["Dec 26", 1800], ["Dec 27", 4000], ["Dec 28", 3000], ["Dec 29", 1000], ["Dec 30", 1300], ["Dec 31", 1010]
				]
			}
		}]
	},
	"TWO": {
		"type": "state",
		"dataset": [{
			"title": "ITR",
			"series": {
				"itr1": [
					["Dec 26", 1800], ["Dec 27", 4000], ["Dec 28", 3000], ["Dec 29", 1000], ["Dec 30", 1300], ["Dec 31", 1010]
				],
				"itr2": [
					["Dec 26", 1800], ["Dec 27", 4000], ["Dec 28", 3000], ["Dec 29", 1000], ["Dec 30", 1300], ["Dec 31", 1010]
				],
				"itr3": [
					["Dec 26", 1800], ["Dec 27", 4000], ["Dec 28", 3000], ["Dec 29", 1000], ["Dec 30", 1300], ["Dec 31", 1010]
				]
			}
		}, {
			"title": "UD",
			"series": {
				"ud1": [
					["Dec 26", 1200], ["Dec 27", 14000], ["Dec 28", 1300], ["Dec 29", 1100], ["Dec 30", 300], ["Dec 31", 1000]
				],
				"ud2": [
					["Dec 26", 1200], ["Dec 27", 14000], ["Dec 28", 1300], ["Dec 29", 1100], ["Dec 30", 300], ["Dec 31", 1000]
				],
				"ud3": [
					["Dec 26", 1200], ["Dec 27", 14000], ["Dec 28", 1300], ["Dec 29", 1100], ["Dec 30", 300], ["Dec 31", 1000]
				]
			}
		}]
	},
	"THREE": {
		"type": "fulfilmentmodel",
		"dataset": [
			{
				"title": "",
				"series": {
					"count": [
						["c1", 1800], ["c2", 4000], ["c3", 3000], ["c4", 1000], ["c5", 1300], ["others", 1010]
					]
				}
			}
		]
	},
	"FOUR": {
		"type": "fulfilmentmodel",
		"dataset": [
			{
				"title": "Ageing from Shipped Date",
				"series": {
					"critical": [["value", 22353]],
					"normal": [["value", 41233]],
					"good": [["value", 63488]]
				}
			}
		]
	}
}