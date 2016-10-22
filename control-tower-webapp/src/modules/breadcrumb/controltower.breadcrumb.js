define({
  name: "controltower.breadcrumb",
  extend: "spamjs.view",
  modules: ["jQuery", "ResourceUtil"]
}).as(function(BREADCRUMB, jQuery, ResourceUtil) {
  return {
    _init_: function() {
      var self = this;
      self.$$.loadTemplate(self.path("controltower.breadcrumb.html"),ResourceUtil.initNavs().then(function(resp){
        var tabs = [];
        tabs.push(resp.modules.filter(function(module) {
          return module.name==self.options.module;
        }).map(function(module){
          return {
            name : module.name,
            label : module.label
          };
        })[0] || {},resp.submodules.filter(function(submodules) {
          return submodules.name==self.options.submodule;
        }).map(function(submodules){
          return {
            name : submodules.name,
            label : submodules.label
          };
        })[0]) || {};
        if(self.options.module == "alerts" && self.options.metrics) {
          tabs.push({
            name: self.options.metrics,
            label: self.options.metrics 
          });
        } else if(self.options.metrics) {
	    	  tabs.push(resp.metrics.filter(function(metric) {
	        	return metric.name == self.options.metrics;
	        }).map(function(metric) {
	        	return {
	        		name: metric.name,
	        		label: metric.label
	        	};
	        })[0])[0] || {
            name: self.options.metrics,
            label: self.options.metrics 
          };
        }
        console.log("tabs", tabs);
        return {
          context : self.options.context,
          tabs : tabs
        };
      }));
    }
  };
});
