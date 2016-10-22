define({
  name: "controltower.sidebar",
  extend: "spamjs.view",
  modules: ["jQuery", "ResourceUtil", "jqrouter", "UserService"]
}).as(function(SIDEBAR, jQuery, ResourceUtil, jqrouter, UserService) {
  return {
    routerEvents : {
      "/controltower/{module}/{submodule}/{metrics}/*" : "moduleOnChange",
      "/controltower/{module}/*" : "moduleOnChange",
      "/controltower/*" : "moduleOnChange"
    },
    _init_: function() {
      this.router = jqrouter.instance(this);
    },
    moduleOnChange : function(e){
      var self = this;
      var _submodule = e.params.submodule || e.params._;
      self.$$.loadTemplate(self.path("controltower.sidebar.html"),ResourceUtil.initNavs().then(function(resp){
        var tabs = [];
        for(var i in resp.modules){
          if(UserService.isAllowed(resp.modules[i].name)){
            tabs.push({
              name : resp.modules[i].name,
              label : resp.modules[i].label,
              selected : (resp.modules[i].name === e.params.module),
              icon : resp.modules[i].icon,
              tabs : (resp.modules[i].tabs || []).map(function(tabName){
                return resp.submodules.filter(function(submodule){
                  return submodule.name == tabName;
                })[0];
              }).map(function(submodule){
                return {
                  name : submodule.name,
                  label : submodule.label,
                  selected : (submodule.name === _submodule)
                };
              }).filter(function(tab){
                return UserService.isAllowed(resp.modules[i].name,tab.name);
              })
            });
          }
        }
        return {
          tabs : tabs
        };
      }));
    }
  };
});
