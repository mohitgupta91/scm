define({
  name: "controltower.tabs",
  extend: "spamjs.view",
  modules: ["jQuery", "ResourceUtil", "UserService"]
}).as(function(TABS, jQuery, ResourceUtil, UserService) {
  return {
    _init_: function() {
      var self = this;
      self.$$.loadTemplate(self.path("controltower.tabs.html"),ResourceUtil.initNavs().then(function(resp){
        var tabs = [];
        for(var i in resp.tabs){
          if(UserService.isAllowed(self.options.data.module,self.options.data.submodule,resp.tabs[i].name)){
            tabs.push({
              name : resp.tabs[i].name,
              label : resp.tabs[i].label,
              selected : (resp.tabs[i].name == self.options.data.tab)
            });
          }
        }
        return {
          context : self.options.data.context,
          tabs : tabs
        };
      }));
    }
  };
});
