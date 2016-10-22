define({
	name : "DataService",
	extend : "jsutils.server",
	modules : ["jQuery", "controltower.toast"]
}).as(function(DataService, jQuery, Toast) {
	return {
		urlMap : {
			"userDetails" : "users/current"
		},
		get : function(url, data, config) {
			var self = this;
			return this.parent().get.apply(this, arguments).fail(function(error) {
				self.errorHandler(error);
			});
		},
		post : function(url, data, config) {
			var self = this;
			return this.parent().post.apply(this, arguments).fail(function(error) {
				self.errorHandler(error);
			});
		},
		put : function(url, data, config) {
			var self = this;
			return this.parent().put.apply(this, arguments).fail(function(error) {
				self.errorHandler(error);
			});
		},
		delete : function(url, data, config) {
			var self = this;
			return this.parent().delete.apply(this, arguments).fail(function(error) {
				self.errorHandler(error);
			});
		},
		errorHandler : function(error) {
			var self = this;
			switch(error.status) {
				case 401:
					jQuery("#modalPopupContainer").loadTemplate(self.path("controltower.login.html")).done(function(){
	                    jQuery("#loginModal").modal({backdrop: 'static', keyboard: false});
	                });
					self.notify("error", error.status, "Session expired.");
					break;
				case 403:
					self.notify("error", error.status, "Access Forbidden! Please contact your administrator.");
					break;
				default: 
					self.notify("error", error.status, "Something went wrong.");
					break;
			}
		},
		notify : function(type, title, message) {
			var self = this;
			Toast.raise({
				type : type,
				title : title,
				message : message
			});
		}
	};
});
