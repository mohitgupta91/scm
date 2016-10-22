define({
    name: "controltower.alerts",
    extend: "spamjs.view",
    modules: ["jQuery", "Select2", "DataService", "jqtags.tab", "controltower.filters", "controltower.buttonConfig", "ResourceUtil", "UserService"],
}).as(function(alerts, jq, Select2, DataService, jqTabs, filters, buttonConfig, ResourceUtil, UserService) {
    return {
        events : {
        },
        _init_: function() {
            var self = this;
        	self.fetchAlertTypeData();
        },
        fetchAlertTypeData: function(){
        	var self = this;
            self.allowedAlerts = [];
        	DataService.get("alerts").done(function(resp) {
                self.alerts = resp.data["alerts"];
                var renderModule = _.after(self.alerts.length, function(){
                    self.renderModule(self.allowedAlerts);
                })
                _.each(self.alerts,function(item){
                    if(UserService.isAllowed("alerts",item.alertId)){
                        self.allowedAlerts.push(item);
                    };
                    renderModule();
                });
			});
        },
        renderModule: function(data) {
            var self = this,
	        	submodule = "controltower."+self.options.submodule;
	        self.$$.loadTemplate(self.path("controltower.alerts.html")).done(function() {
	        	module(submodule, function(myModule) {
					self.add(myModule.instance({
						id: "controltower-alerts-container",
						options: {
                            alerts: data
						}
					}));
				}); 
	        });
        }
    };
});