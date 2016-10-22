define({
  name: "controltower.charts",
  extend: "spamjs.view",
  modules: ["jQuery"]
}).as(function(CHARTS, jQuery) {
  return {
  	object: function() {
  		return {
  			colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']	,
	        chart: {
	            zoomType: 'x',
	          	plotBackgroundColor: 'rgba(255, 255, 255, .9)',
		        plotShadow: true,
	            plotBorderWidth: 1,
	            panning: true,
	            panKey: 'ctrl'
	        },
	        title: {
		          "text": '',
		          style: {
		            color: '#000',
		            font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
		          },
		          align: "left"
	        },
	        subtitle: {
	          	"text": '',
		        style: {
		          color: '#000',
		          font: 'normal 18px "Trebuchet MS", Verdana, sans-serif'
		        }
	        },
	        xAxis: [{
		          "categories": [],
		          "title": {
		            "text": '',
		            style: {
		              color: '#333',
		              fontWeight: 'bold',
		              fontSize: '12px',
		              fontFamily: 'Trebuchet MS, Verdana, sans-serif'
		            }
		          },
		          gridLineWidth: 1,
		          lineColor: '#000',
		          tickColor: '#000',
		          labels: {
		            style: {
		              color: '#000',
		              font: '11px Trebuchet MS, Verdana, sans-serif'
		            }
		          },
	            crosshair: true
	        }],
	        yAxis: [{ // Primary yAxis
	          "title": {
	            "text": '',
	            "align": 'middle',
	            style: {
	              color: '#333',
	              fontWeight: 'bold',
	              fontSize: '12px',
	              fontFamily: 'Trebuchet MS, Verdana, sans-serif'
	            }
	          },
	          "labels": {
	            "overflow": 'justify',
	            style: {
	              color: '#000',
	              font: '11px Trebuchet MS, Verdana, sans-serif'
	            }
	          },
	          minorTickInterval: 'auto',
	          lineColor: '#000',
	          lineWidth: 1,
	          tickWidth: 1,
	          tickColor: '#000'
	        }, { // Secondary yAxis
	          "title": {
	            "text": '',
	            "align": 'middle',
	            style: {
	              color: '#333',
	              fontWeight: 'bold',
	              fontSize: '12px',
	              fontFamily: 'Trebuchet MS, Verdana, sans-serif'
	            }
	          },
	          "labels": {
	            "overflow": 'justify',
	            style: {
	              color: '#000',
	              font: '11px Trebuchet MS, Verdana, sans-serif'
	            }
	          },
	          minorTickInterval: 'auto',
	          lineColor: '#000',
	          lineWidth: 1,
	          tickWidth: 1,
	          tickColor: '#000',
	          opposite: true
	        }],
	        tooltip: {
	            shared: true
	        },
	        lang: {
	            noData: "No data to display."
	        },
	        legend: {
		          "layout": 'horizontal',
		          "align": 'right',
		          "verticalAlign": 'bottom',
		          "x": 0,
		          "y": 0,
		          // "floating": true,
		          // "borderWidth": 1,
		          // "backgroundColor": '#FFFFFF',
		          // "shadow": true,
		          itemStyle: {
		            font: '9pt Trebuchet MS, Verdana, sans-serif',
		            color: 'black'
		          },
		          itemHoverStyle: {
		            color: '#039'
		          },
		          itemHiddenStyle: {
		            color: 'gray'
		          }
	        },
	        series: [],
	        navigation: {
	          buttonOptions: {
	            theme: {
	              stroke: '#CCCCCC'
	            }
	          }
	        },
	        credits: {
	        	text: ""
	        },
	        plotOptions: {
	            series: {
	                cursor: 'pointer',
	                point: {
	                    events: {
	                        click: undefined
	                    }
	                },
	                marker: {
	                	enabled: true
	                }
	            }
	        },
	        seriesObj: {
	            name: '',
	            type: '',
	            yAxis: 1, // 0 or 1
	            data: [],
	            tooltip: {
	                valueSuffix: ''
	            },
				dataLabels: {
					enabled: true
				}
	        }
  		};
  	},
    _init_: function(data) {
      var self = this, chartData, responseData;
      responseData = data.options.response;
      chartData = jQuery(self.$).extend(true, {}, self.object());
      chartData.title.text = responseData.title!=undefined?responseData.title:'';
      chartData.subtitle.text = responseData.subtitle!=undefined?responseData.subtitle:'';
      if(responseData.plotOptions) {
      	for (var attrname in responseData.plotOptions) { chartData.plotOptions[attrname] = responseData.plotOptions[attrname]; }
      }
      if(responseData.chart) {
      	for (var attrname in responseData.chart) { chartData.chart[attrname] = responseData.chart[attrname]; }
      }
      _.each(responseData.xAxis, function(v, i) {
      	chartData.xAxis[i].categories = v.labels;
      	chartData.xAxis[i].title.text = v.title;
      });
      _.each(responseData.yAxis, function(v, i) {
      	chartData.yAxis[i].title.text = v.title;
      	chartData.yAxis[i].max = v.max;
      	chartData.yAxis[i].tickAmount = v.tickAmount;
      	chartData.yAxis[i].labels.format = v.format;
      });
      _.each(responseData.dataset.series, function(v, i) {
      	  chartData.series[i] = jQuery(self.$).extend(true, {}, chartData.seriesObj);
  		  for (var attrname in v) { chartData.series[i][attrname] = v[attrname]; }
		  chartData.series[i].data = v.dataList;
		  delete chartData.series[i].dataList;
      });
      self.setSeriesFormatter(chartData);
      chartData.plotOptions.series.point.events.click = function() {
      	var that = this, chartTitle = "";
      	if(that.series.chart.title) {
      		chartTitle = that.series.chart.title.textStr;
      	}
      	self.trigger(responseData.clickCallback, {
      		seriesName: that.name,
      		series: that.series.name,
      		category: that.category,
      		chartTitle: chartTitle,
      		y: that.y
    		});
    	};
      console.error("Chart Data", chartData);
      self.plotChart(chartData);
    },
    setSeriesFormatter: function(chartData) {
    	var self = this;
        switch(chartData.plotOptions.dataLabelFormatter) {
        	case "percentage":
        		chartData.plotOptions.series.dataLabels = chartData.plotOptions.series.dataLabels?chartData.plotOptions.series.dataLabels:{};
        		chartData.plotOptions.series.dataLabels.formatter = function() {
        			return this.percentage.toFixed(2) + "%";
        		};
        		break;
    		default:
    			break;
        }
    },
    plotChart: function(chartData) {
	  var self = this;
      self.$$.loadTemplate(self.path("controltower.charts.html")).done(function() {
      	self.$$.find(".chart-wrapper").highcharts(chartData);
      });
    }
  };
});