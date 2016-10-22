define({
	name : "controltower.metrics",
	extend : "spamjs.view",
	modules : ["jQuery", "Select2", "DataService", "spamjs.datatable", "jqtags.tab", "controltower.charts",
			   "controltower.filters", "controltower.buttonConfig", "ResourceUtil", "MetricsDataService",
			   "controltower.toast", "jsutils.i18n"],
}).as(function(METRICS, jq, Select2, DataService, datatable, jqTabs, Charts, Filters,
			   buttonConfig, ResourceUtil, MetricsDataService, Toast, i18n) {
	return {
		_i18n : null,
		metricsName : null,
		metricsMetaData : null,
		chartData : null,
		currentLevel : null,
		selectedFilters : null,
		selectedChartFilters : {},
		filterInstance : null,
		chartFilterInstance : null,
		apiBaseUrl : "",
		chartRequestOptions : {},
		levels : [],
		events : {
			"click .clear-filters" : "clearFilters",
			"change #matricstab" : "levelChange",
			"click .apply-filters" : "applyFilters",
			"click .chart-apply-filters" : "applyChartFilters",
			"chartPointClick" : "chartPointClick",
			"click .chartTableRow" : "chartTableRowClick",
			"input #search-metric" : "searchInput",
			"filtersAdded" : "filtersAdded"
		},
		_init_ : function() {
			var self = this;
			self._i18n = i18n.instance();
			self.reqBreadCrumbData = null;
			if (self.options.metrics) {
				self.apiBaseUrl = "metrics/" + self.options.module + "/" + self.options.submodule + "/" + self.options.metrics + "/";
			}
			if (self.options.metricslist) {
				self.$$.loadTemplate(self.path("controltower.metrics.html"), {
					metricslist : self.options.metricslist,
					module : self.options.module,
					submodule : self.options.submodule
				});
			} else if (!self.options.metricsList && self.options.metrics) {
				self.metricsName = self.options.metrics;
				self.getMetricsMetaData(self.options.module, self.options.submodule, self.options.metrics);
			}
		},
		getMetricsMetaData : function(moduleName, submoduleName, metricsName) {
			var self = this;
			MetricsDataService.initChartData(moduleName, submoduleName, metricsName).done(function(resp) {
				self.metricsMetaData = resp;
				self.currentLevel = 0;
				self.renderTemplate();
			}).fail(function(resp) {
				console.error("Failed to load Data: ", resp);
			});
		},
		renderTemplate : function() {
			var self = this;
			self.tabsInfo = self.metricsMetaData.tabsInfo;
			_.each(self.tabsInfo, function(obj, i) {
				self.levels[i] = obj.id;
			});
			_.each(self.tabsInfo, function(item) {
				item.displayName = ResourceUtil.getLang(item.name);
			});
			self.$$.loadTemplate(self.path("controltower.metrics.html"), {
				tabsInfo : self.tabsInfo,
				metricslist : self.options.metricslist,
				module : self.options.module,
				submodule : self.options.submodule
			}).done(function() {
				self.renderFilters();
			});
		},
		applyChartFilters : function() {
			var self = this;
			self.selectedChartFilters[self.levels[self.currentLevel]] = self.chartFilterInstance.fetchFilters();
			self.selectedFilters = self.filterInstance.fetchFilters();
			if(self.selectedChartFilters[self.levels[self.currentLevel]] && self.selectedFilters) {
				// prepare chart api request data
				self.getChartData();
			} else {
				Toast.raise({
					type: "warning",
					message: "Please select all mandatory fields."
				});
			}
		},
		applyFilters : function() {
			var self = this;
			// check for custom level and process accordingly
			if(self.metricsMetaData[self.levels[self.currentLevel]].filters) {
				var filters = self.metricsMetaData[self.levels[self.currentLevel]].filters;
				self.setChartFilters(JSON.parse(JSON.stringify(filters)));
				self.changeTabsState();
			} else {
				self.selectedFilters = self.filterInstance.fetchFilters();
				if(self.selectedFilters) {
					self.getChartData();
					self.renderDatatable();
				} else {
					Toast.raise({
						type: "warning",
						message: "Please select all mandatory fields."
					});
				}
			}
		},
		changeTabsState : function() {
			var self = this;
			self.$$.find("#matricstab").attr("value", self.levels[self.currentLevel]);
			self.$$.find("#"+self.levels[self.currentLevel]).removeAttr("disabled");
		},
		clearFilters : function() {
			var self = this;
			self.filterInstance.clearFilters();
		},
		renderFilters : function() {
			var self = this;
			var config = {
				eventFor : "main-filters",
				id : "main-filters",
				filters : DataService.get(self.apiBaseUrl + "filter").then(function(resp) {
					return resp.data;
				})
			};
			self.filterInstance = Filters.instance(config);
			self.add(self.filterInstance).done(function() {
			});
		},
		filtersAdded : function(data) {
			var self = this,
			    eventFor = data.detail.eventFor;
			switch(eventFor) {
			case "main-filters":
				self.renderButtonsRow();
				break;
			case "chart-filters":
				self.applyChartFilters();
				break;
			}
		},
		renderButtonsRow : function() {
			var self = this;
			var config = {
				element : self.$$.find(".button-container"),
				data : [{
					label : "Reset Filters",
					class : "clear-filters"
				}, {
					label : "Apply Filters",
					class : "apply-filters"
				}]
			};
			self.add(buttonConfig.instance(config));
		},
		mapRequestData : function() {
			var self = this,
			    requestData = {};
			requestData.filters = self.selectedFilters;
			requestData.stage = self.levels[self.currentLevel];
			requestData.options = {};
			requestData.chartFilters = {};
			if (!_.isEmpty(self.chartRequestOptions)) {
				for(key in self.chartRequestOptions) {
					if(key === requestData.stage) break;
					requestData.options[self.chartRequestOptions[key].type] = self.chartRequestOptions[key].selected;
					requestData.chartFilters[key] = self.selectedChartFilters[key]?self.selectedChartFilters[key]:null;
				}
			}
			requestData.chartFilters[requestData.stage] = self.selectedChartFilters[requestData.stage]?self.selectedChartFilters[requestData.stage]:null;
			return requestData;
		},
		setChartRequestOptions : function() {
			var self = this,
			    first = true;
			if(!self.chartRequestOptions[self.levels[self.currentLevel]]) {
				self.chartRequestOptions[self.levels[self.currentLevel]] = {};
			}
			self.chartRequestOptions[self.levels[self.currentLevel]].type = self.chartData.type;
		},
		getChartData : function() {
			var self = this,
			    requestData = {},
			    config = {};
			requestData = self.mapRequestData();
			self.filterInstance.setFilters(requestData.options);
			self.reqBreadCrumbData = requestData;
			self.renderBreacrumbData(requestData.options);
			DataService.post(self.apiBaseUrl + "chartdata", requestData, config).done(function(resp) {
				if(resp.code == 200) {
					if(resp[self.levels[self.currentLevel]]) {
						self.chartData = resp[self.levels[self.currentLevel]];
					} else {
						self.chartData = resp.data;
					}
					self.validateDataAndProcessChart();
				} else {
					self.reportError(resp.code, resp);
				}
			});
		},
		validateDataAndProcessChart : function(){
			var self = this;
			if(self.chartData.dataset.length < 1) {
				self.$$.find("#chart-area").html("No data found.");
				return;
			} else {
				self.$$.find("#chart-area").empty();
			}
			self.setChartRequestOptions();
			self.setChartFilters(self.chartData.filters);
			self.changeTabsState();
			self.prepareChartData();
		},
		reportError : function(errorCode, resp){
			Toast.raise({
				type: "error",
				title: errorCode,
				message: resp.message
			});
		},
		renderBreacrumbData : function(breadCrumbData){
			var self = this;
			var htmlStr = '<ul>';
			_.each(breadCrumbData,function(item){
				if(item) {
					htmlStr += "<li>"+self._i18n.get(item)+"</li>";
				}
			});
			self.$$.find("#breadDataContainer").html(htmlStr+"</ul>");
		},
		setChartFilters : function(filtersObj) {
			var self = this;
			if (filtersObj && filtersObj.length > 0) {
				var config = {
					eventFor : "chart-filters",
					id : "chart-filters",
					filters : filtersObj
				};
				self.chartFilterInstance = Filters.instance(config);
				self.add(self.chartFilterInstance).done(function() {
					var config = {
						element : self.$$.find(".chart-filters-buttons"),
						data : [{
							label : "Fetch",
							class : "chart-apply-filters"
						}]
					};
					self.add(buttonConfig.instance(config));
				});
			}
		},
		chartTableRowClick : function(event) {
			var self = this,
				clickedRow = jQuery(event.target).closest("tr"),
				clickedColumn = jQuery(event.target).closest("td"),
				dataFor = jQuery(clickedRow.find("td")[0]).html(),
				clickedRowData = {
					detail: {}
				};
				
			clickedRowData.detail.title = dataFor;
			clickedRowData.detail.tableColumn = clickedColumn.data("column");
			if(self.metricsMetaData[self.levels[self.currentLevel]].clickableDataRow !== false) {
				self.chartPointClick(clickedRowData);
			}
		},
		prepareTabularChartData : function(metricsMetaData, nthSeries) {
			var self = this,
			    tableHTML = '',
			    i,
			    k,
			    rowNo,
			    pieTotal; 
			_.each(self.chartData.dataset, function(chartTableData, tableNo) {
				var chartTableSeries = chartTableData.series;
				self.$$.find("#chart-area").html("<table id='chartTable-"+tableNo+"' class='chartTable'></table>");
				_.each(chartTableData.columns, function(column, index) {
					tableHTML += "<td data-column='" + column + "'>" + self._i18n.get(column) + "</td>";
				});
				self.$$.find("#chartTable-" + tableNo).append("<tr>"+tableHTML+"</tr>");
				tableHTML = '';
				rowNo = 0;
				_.map(chartTableSeries, function(rowData, dataFor) {
					rowNo++;
					self.$$.find("#chartTable-" + tableNo).append("<tr id='row-"+rowNo+"' class='chartTableRow'><td data-column='" + chartTableData.columns[1] + "'>"+self._i18n.get(dataFor)+"</td></tr>");
					_.each(chartTableData.columns, function(column, index) {
						if(index>0) {
							i = 0;
							k = 0;
							pieTotal = 0;
							self.$$.find("#row-" + rowNo).append("<td data-column='" + column + "' align='center'><div align='center'><div id='chart-" + rowNo +"-"+ index + "'></div></div></td>");
							metricsMetaData.dataset.series = JSON.parse(JSON.stringify(self.metricsMetaData[self.levels[self.currentLevel]].dataset.series));
							var chartSeries = _.where(rowData, {title: column})[0];
							console.log("chartSeries", chartSeries);
							if (nthSeries) {
								switch(metricsMetaData.chart.type) {
									case "pie":
										if(_.isEmpty(chartSeries.chartData)) {
											self.$$.find("#chart-" + rowNo +"-"+ index).html("<span class='info'>No Data.</span>");
										} else {
											_.each(chartSeries.chartData, function(yVal, key) {
												if(k===0) {
													metricsMetaData.dataset.series[i] = JSON.parse(JSON.stringify(metricsMetaData.seriesObj));
													// metricsMetaData.dataset.series[i].name = self._i18n.get(chartSeries.title);
													metricsMetaData.dataset.series[i].name = "Value";
												}
												metricsMetaData.dataset.series[i].dataList[k] = {};
												metricsMetaData.dataset.series[i].dataList[k].name = self._i18n.get(key);
												metricsMetaData.dataset.series[i].dataList[k].color = self.getColor(key.toLowerCase());
												metricsMetaData.dataset.series[i].dataList[k].y = yVal;
												pieTotal += yVal;
												k++;
											});
											self.add(Charts.instance({
												id : "chart-" + rowNo +"-"+ index,
												options : {
													response : metricsMetaData
												}
											}));
											self.$$.find("#chart-" + rowNo +"-"+ index).after("<div>"+pieTotal+"</div>");
										}
										break;
									default:
										break;
								}
							}
						}
					});
				});
			});
			self.scrollTop();
		},
		prepareChartData : function() {
			var self = this;
			try {
				var xAxisLabels = [],
				    i = 0,
				    k = 0,
				    pieTotal = 0,
					metricsMetaData = jq(self.$).extend(true, {}, self.metricsMetaData[self.levels[self.currentLevel]]),
					nthSeries = false;
				metricsMetaData.chart = metricsMetaData.chart?metricsMetaData.chart:{};
				if (metricsMetaData.clickableDatapoints) {
					metricsMetaData.clickCallback = "chartPointClick";
				}
				if (metricsMetaData.dataset.series.length < 1) {
					nthSeries = true;
				}
				if(metricsMetaData.tabularCharts) {
					self.prepareTabularChartData(metricsMetaData, nthSeries);
					return;
				}
				_.each(self.chartData.dataset, function(chartSeries, chartNumber) {
					i = 0;
					k = 0;
					pieTotal = 0;
					metricsMetaData.dataset.series = jq(self.$).extend(true, [], self.metricsMetaData[self.levels[self.currentLevel]].dataset.series);
					metricsMetaData.title = self._i18n.get(chartSeries.title);
					if (nthSeries) {
						_.map(chartSeries.series, function(data, key) {
							switch(metricsMetaData.chart.type) {
								case "pie":
									if(k===0) {
										metricsMetaData.dataset.series[i] = JSON.parse(JSON.stringify(metricsMetaData.seriesObj));
										metricsMetaData.dataset.series[i].name = self._i18n.get(chartSeries.title);
									}
									metricsMetaData.dataset.series[i].dataList[k] = {};
									metricsMetaData.dataset.series[i].dataList[k].name = self._i18n.get(key);
									metricsMetaData.dataset.series[i].dataList[k].color = self.getColor(key.toLowerCase());
									metricsMetaData.dataset.series[i].dataList[k].y = data[0][1];
									pieTotal += data[0][1];
									metricsMetaData.subtitle = pieTotal;
									k++;
									break;
								default:
									metricsMetaData.dataset.series[i] = JSON.parse(JSON.stringify(metricsMetaData.seriesObj));
									metricsMetaData.dataset.series[i].dataList = [];
									metricsMetaData.dataset.series[i].name = self._i18n.get(key);
									for (var j = 0; j < data.length; j++) {
										xAxisLabels[j] = data[j][0];
										metricsMetaData.dataset.series[i].dataList[j] = data[j][1];
									}
									i++;
									break;
							}
						});
					} else {
						_.map(chartSeries.series, function(data, key) {
							switch(metricsMetaData.chart.type) {
								default:
									var meta = _.where(metricsMetaData.dataset.series, {name: key})[0];
									if(meta) {
										meta.dataList = [];
										meta.name = self._i18n.get(key);
										for (var j = 0; j < data.length; j++) {
											xAxisLabels[j] = data[j][0];
											meta.dataList[j] = data[j][1];
										}
									}
									i++;
									break;
							}
						});
					}
					metricsMetaData.xAxis[0].labels = xAxisLabels;
					if(metricsMetaData.chart.type == "pie") {
						self.$$.find("#chart-area").addClass("floating-charts");
					} else {
						self.$$.find("#chart-area").removeClass("floating-charts");
					}
					self.$$.find("#chart-area").append("<div id='chart-"+chartNumber+"'></div>");
					self.add(Charts.instance({
						id : "chart-"+chartNumber,
						options : {
							response : metricsMetaData
						}
					}));
				});
				self.scrollTop();
			} catch(e) {
				console.log("Chart data preparation error:", e);
			}
		},
		getColor : function(option) {
			var self = this;
			switch(option) {
				case "critical":
					return "red";
					break;
				case "normal":
					return "yellow";
					break;
				case "good":
					return "green";
					break;
			}
		},
		renderDatatable : function() {
			var self = this;
			if (self.metricsMetaData[self.levels[self.currentLevel]].datatable) {
				DataService.get(self.apiBaseUrl + "datatable").done(function(resp) {
					resp.ele = self.$$.find(".grid-container");
					self.add(datatable.instance(resp));
				});
			} else {
				self.$$.find(".grid-container").empty();
			}
		},
		scrollTop : function() {
			var self = this,
				scrollHeight = 20 + self.$$.find(".controltower-filters").height() + self.$$.find(".button-container").height();
			self.$$.find('.controltower-metrics').animate({
				scrollTop: scrollHeight
			}, 500);
		},
		levelChange : function(event) {
			var self = this;
			self.currentLevel = self.levels.indexOf(self.$$.find("#matricstab").val());
			self.$$.find(".chart-filters").empty();
			self.$$.find(".chart-filters-buttons").empty();
			self.$$.find(".chart-area").empty().removeClass("floating-charts");
			self.applyFilters();
		},
		chartPointClick : function(data) {
			var self = this,
			    dataPoint = data.detail,
			    clickedCategory,
			    requestValueType = self.metricsMetaData[self.levels[self.currentLevel]].requestValueType;
		    
		    if(requestValueType == false) {
				self.chartRequestOptions[self.levels[self.currentLevel]].selected = null;
		    } else {
			    requestValueType = requestValueType? requestValueType:"series"; 
				self.chartRequestOptions[self.levels[self.currentLevel]].selected = self._i18n.key(dataPoint[requestValueType]);
		    }
			    
			self.$$.find(".jq-tab-head").attr("disabled", true);
			for(var i=0;i<=self.currentLevel;i++) {
				self.$$.find("#"+self.levels[i]).removeAttr("disabled");
			}
			self.$$.find("#matricstab").attr("value", self.levels[self.currentLevel + 1]);
			// alert('Series: ' + dataPoint.series + ', Category: ' + dataPoint.category + ', value: ' + dataPoint.y);
			self.levelChange();
		},
		searchInput : function(event) {
			var self = this,
				searchString;
			searchString = event.target.value.split(" ").join("|");
			self.searchItem(searchString);
		},
		searchItem : function(searchString) {
			var self = this;
			var searchRegEx = new RegExp('(' + searchString + ')+', 'ig');
			var listAfterSearch = _.filter(self.options.metricslist, function(metric) {
				console.log(metric);
				return metric.label.match(searchRegEx) != null ? true : false;
			});
			self.$$.find(".controltower-metrics-list").loadTemplate(self.path("controltower.metricslist.html"), {
				metricslist : listAfterSearch,
				module : self.options.module,
				submodule : self.options.submodule
			});
		}
	};
});
