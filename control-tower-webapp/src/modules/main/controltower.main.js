define({
  name: "controltower.main",
  extend: "spamjs.view",
  modules: ["jQuery","jqrouter", "ResourceUtil", "UserService"]
}).as(function(CONTENT, jQuery,jqrouter, ResourceUtil, UserService) {
  return {
    routerEvents : {
      "/controltower/{module}/{submodule}/{metrics}" : "openMainModule",
      "/controltower/{module}/{submodule}" : "openMainModule",
      "/controltower/{page}" : "defaultActions",
    },
    _init_: function() {
      var self = this;
      self.router = jqrouter.instance();
      self.router.bind(self);
      self.router.defaultRoute("controltower/home");
    },
    defaultActions : function(e){
      var self = this;
      this.$$.loadTemplate(this.path("controltower.settings.html"),{
      	module: e.params.page
      });
    },
    openMainModule : function(e){
      var self = this,
      		metricslist = [];
    	ResourceUtil.initNavs().done(function(resp){
	      if(e.params.metrics == undefined) {
	      	var availableMetrics = resp.submodules.filter(function(submodules) {
	          return submodules.name==e.params.submodule;
	        }).map(function(submodule){
	          return submodule.metrics;
	        });
	        for(i in availableMetrics[0]) {
          	if(UserService.isAllowed(e.params.module,e.params.submodule,availableMetrics[0][i])){
			        metricslist.push(resp.metrics.filter(function(metrics) {
			        	return metrics.name == availableMetrics[0][i];
			        })[0]);
		        }
	        }
	      }
	      self.$$.loadTemplate(self.path("controltower.main.html"),{
	        nav : {
	          module : e.params.module,
	          submodule : e.params.submodule,
	          metrics : e.params.metrics?e.params.metrics:undefined,
	          context : ["/controltower",e.params.module,e.params.submodule].join("/")
	        }
	      }).done(function(){
	      	if(e.params.module == "alerts") {
				module("controltower.alerts", function(myModule) {
					self.add(myModule.instance({
						id: "app-container",
						options: {
							submodule: e.params.submodule
						}
					}));
				}); 
	      	} else {
				module("controltower.metrics", function(myModule) {
					self.add(myModule.instance({
						id: "app-container",
						options: {
				          module : e.params.module,
				          submodule : e.params.submodule,
				          metrics : e.params.metrics?e.params.metrics:undefined,
				          metricslist : metricslist.length>0?metricslist:undefined,
						}
					}));
				}); 
	      	}
	      });
    	});
    }
  };
});
