define({
	name: "MetricsDataService",
	modules: ["DataService", "jQuery", "jsutils.file"]
}).as(function(MetricsDataService, DataService, jQuery, fileUtil) {
	return {
    $MetricsChartData: null,
    MetricsChartData: null,
		_ready_: function() {
		},
		initChartData: function(moduleName, submoduleName, metricsName) {
			var self = this;
			self.$MetricsChartData = fileUtil.getJSON(self.path("data/" + moduleName + "/" + submoduleName + "/" + metricsName + "/" + "metadata.json")).done(function(resp) {
				self.MetricsChartData = resp;
			});
			return self.$MetricsChartData.then(function() {
				return self.MetricsChartData;
			});
		},
	};
});
