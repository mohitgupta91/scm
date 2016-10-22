define({
  name: "controltower.header",
  extend: "spamjs.view",
  modules: ["jQuery", "UserService"]
}).as(function(HEADER, jQuery, UserService) {
  return {
    _init_: function() {
      var self = this;
      UserService.init().done(function(MetaData) {
        console.error("_init_");
        self.$$.loadTemplate(self.path("controltower.header.html"), MetaData).done(function() {
	      	self.bindEvents();
        });
      });
    },
    bindEvents: function() {
    	var self = this;
    	self.$$.find(".user-menu").unbind("click").bind("click", self, self.toggleUserMenu);
    },
    toggleUserMenu: function(event) {
    	var self = event.data;
    	if(self.$$.find(".user-menu").hasClass("active")) {
    		self.$$.find(".user-menu").removeClass("active");
    	} else {
    		self.$$.find(".user-menu").addClass("active");
    	}
    }
  };
});
