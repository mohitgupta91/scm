define({
	name: "controltower.filters",
	extend: "spamjs.view",
	modules: ["jQuery", "Select2", "DataService", "ResourceUtil","lazy", "controltower.toast"]
}).as(function(filters, jq, Select2, DataService, ResourceUtil,lazy, Toast) {


	return {
		metrics: null,
		module: null,
		submodule: null,
		events: {
			"change .data-filters": "onFilterChange",
      		"click .multiremote-filter" : "open_multiremote_filter"
		},
		_init_: function() {
			var self = this;
			self.depandsOnObj = {};
			self.multiRemoteFilterData = {};
			lazy.promise(self.options.filters)().done(function(resp) {
				if(resp && resp.length > 0) {
					self.filtersObj = resp;
					self.addFilters();	
				}
			});
		},
	    open_multiremote_filter : function(event){
			var self = this;
			var ele = $(event.target);
			var url = ele.data("url");
			var filterName = ele.data("filtername");
			var targetFilterData = _.filter(self.filtersObj,function(item){if(item.name == filterName){return item;}})[0];
			var dependsOnName = targetFilterData.dependsOn;
			var dependsOnVal = self.$$.find('*[data-filtername='+dependsOnName+']').val();
			module("spamjs.modal", function(modal){
			module("multiselectremote", function(multiselectremote){
			  	self.add(modal.instance({
					id : "my_module2",
					options : {
				        title : "Select " + filterName,
				        module: multiselectremote.instance({
				        	dataUrl: url, 
				        	dependsOnVal: dependsOnVal,
				        	preSelectedFilters: self.multiRemoteFilterData[filterName]?self.multiRemoteFilterData[filterName]:[],
				        	filterName: filterName,
				        	onSaveMultiRemoteFilter: self.onSaveMultiRemoteFilter,
				        	parentModule: self
				        })
			      	}
			    }));
			});
			});
	    },
		setFilters: function(filterValues) {
			var self = this;
			_.each(filterValues, function(val, key) {
				var filterObj = _.where(self.filtersObj, {name: key})[0];
				if(filterObj && _.where(filterObj.options, {id: val})[0]) {
					var filterEle = self.$$.find('*[data-filtername=' + key + ']');
					if(val) {
						filterEle.val(val);
						filterEle.removeClass("error").addClass('highlight');
					}
				}
			});
		},
		addFilters: function() {
			var self = this;
			_.each(self.filtersObj, function(val) {
				val.label = ResourceUtil.getLang(val.name);
				if(val.dependsOn){
					if(self.depandsOnObj[val.dependsOn]){
						self.depandsOnObj[val.dependsOn].push(val.name);
					}else{
						self.depandsOnObj[val.dependsOn] = [val.name];
					}
				}
				if(val.range){
					var range = val.range.split("-");
					var currentDate = new Date();
	       			val.minDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate()-parseInt(range[0]));
	       			val.maxDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate()+parseInt(range[1]));
				};
				
				var options = [];
				_.each(val.options, function(option) {
					options.push(self.formatFilterVal(option, option));
				});
				val.options = options;
			});
			self.$$.loadTemplate(self.path("controltower.filters.html"), {
				filtersObj: self.filtersObj
			}).done(function() {
				self.trigger("filtersAdded", {
					eventFor: self.options.eventFor
				});
			});
		},
		onFilterChange: function(event) {
			event.stopPropagation();
			var self = this;
			var changedFilter = $(event.target);
			if(changedFilter[0].tagName == "JQ-DATE"){
				var selectedFilterDate = new Date(changedFilter.val()).getTime();
				var currentDate = new Date().getTime();
				if(selectedFilterDate > currentDate){
					Toast.raise({
						type: "warning",
						message: "Selected date can not be greater than current date."
					});
					return;
				}
			}
			changedFilter.removeClass("error").addClass('highlight');
			var filterName = changedFilter.data("filtername");
			var dependentFilters = this.depandsOnObj[filterName];
			_.each(dependentFilters, function(item){
				var changedVal = $(event.target).val();
				var eleName = $(event.target).data("filtername");
				var filter = _.find(self.filtersObj,function(val){
					if(val.name == item){
						return val;
					}
				});
				if(filter.dataUrl){
					self.$$.find('*[data-filtername=' + filter.name + ']').prop('disabled',false);
					return;
				}
				var select2Data = filter.dependentOptions[changedVal];
				var options = [];
				_.each(select2Data, function(option) {
					options.push(self.formatFilterVal(option, option));
				});
				self.$$.find('*[data-filtername='+item+']')[0].setOptions(options);
				if(options.length > 0) {
					self.$$.find('*[data-filtername='+item+']').removeClass("error").addClass('highlight');
				} else {
					self.$$.find('*[data-filtername='+item+']').removeClass("highlight");
				}
			});
		},
		clearFilters: function() {
			var self = this;
			self.$$.find(".highlight").removeClass("highlight");
			self.$$.find(".error").removeClass("error");
			self.$$.loadTemplate(self.path("controltower.filters.html"), {
				filtersObj: self.filtersObj
			});
		},
		formatFilterVal: function(key, code) {
			return {
				id: code,
				text: ResourceUtil.getLang(key)
			};
		},
		fetchFilters: function() {
			var filters = {},
				isValidated = true,
			    self = this;
			_.each(self.filtersObj, function(item, index) {
				var filterEle = self.$$.find('*[data-filtername=' + item.name + ']');
				var val = filterEle.val();
				var mandatory = filterEle.data("mandatory");
				if(mandatory && !val){
					//alert(item.name+" is a mandatory filter so please select a value for this");
					isValidated = false;
					filterEle.removeClass("highlight").addClass("error");
					console.log("filterEle", filterEle);
					//return false;
				}
				if (val && filterEle.hasClass("date-filter")) {
					val = new Date(val).getTime().toString();
				}
				val = val ? val : "";
				if(val.trim()) {
					filters[item.name] = val.split(",");
				}
			});
			if(isValidated){
				return filters;
			}else{
				return false;
			};
			
		},
		onSaveMultiRemoteFilter: function(filterName, selectedFilters){
			var self = this;
			console.log(filterName,"....",selectedFilters);
			self.multiRemoteFilterData[filterName] = selectedFilters;
			self.$$.find('*[data-filtername='+filterName+']').val(selectedFilters)
														     .removeClass("error")
														     .addClass("highlight")
														     .attr("title", selectedFilters.join(", "));
		}
	};
}); 