define({
  name: "controltower.app",
  extend: "spamjs.view",
  modules: ["jqrouter", "jQuery", "jsutils.file","ResourceUtil"]
}).as(function(app, jqrouter, jQuery, fileUtil, ResourceUtil) {
  return {
    routerEvents: {
      "/boot/*": "openDevSection",
      "/boot/{mod}/*": "openDevSection"
    },
    events : {
      "header.init .view-controltower-header" : "header_init"
    },
    header_init : function(){
      console.error("header_init",arguments);
    },
    globalEvents: {},
    _init_: function() {
      var self = this;
      jqrouter.start();
      _importStyle_("controltower/style");
      if (document.location.pathname.startsWith("/boot")) {
        this.openDevSection();
      } else {
        console.error("===",this.path("controltower.app.html"));
        this.$$.loadTemplate(
            this.path("controltower.app.html"),
            jQuery.when(ResourceUtil.initNavs(), ResourceUtil.initLang())
        ).done(function() {
            // module("controltower.filters", function(myModule) {
            //      self.add(myModule.instance({
		    //       id: "filters-section"
		    //     }));
            // });
        });
      }
      this.router = jqrouter.instance();
      this.router.bind(this);
      //this.router.defaultRoute("app/nomodule/nosubmodule/notab/");
      this.$style = jQuery("<style type='text/css'/>");
      jQuery("body").append(this.$style).removeClass("loading");
      this.refreshStyle();
    },
    openDevSection: function() {
      var self = this;
      module("spamjs.bootconfig", function(myModule) {
        self.add(myModule.instance({
          id: "bootconfig",
          routerBase: "/boot/"
        }));
      });
    },
    _routerEvents_ : function(e, target, data, params){
      console.error("_routerEvents_",e, target, data, params);
      var self = this;
      if(!data) {
      	data = {};
      }
      module(target, function(myModule) {
      	console.log("myModule--------", target);
        self.add(myModule.instance({
          id: "app-container", options : {data: data, params: e.params}
        }));
      });
    },
    refreshStyle : debounce(function(e){
      var self = this;
      fileUtil.loadView(self.path("custom.css"), {
        pathname : document.location.pathname.replace(self.router.appContext,"/")
      }).then(function (OBJ) {
        self.$style.html(OBJ.html.replace(/:jqr-go/g,"[jqr-go='"+OBJ.data.pathname+"']").replace(/:jqr-active/g,"[jqr-active^='"+OBJ.data.pathname+"']"));
      });
    }),
    _ready_: function() {
      window.GLOBAL = {
      	ResourceUtil: ResourceUtil
      };
      this.instance().addTo(jQuery("body"));
    }
  };
});