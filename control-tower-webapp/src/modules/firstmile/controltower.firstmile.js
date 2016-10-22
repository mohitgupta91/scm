define({
  name: "controltower.firstmile",
  extend: "spamjs.view",
  modules: ["jQuery"]
}).as(function(FIRSTMILE, jQuery) {
  return {
    _init_: function() {
      var self = this;
      self.$$.loadTemplate(self.path("controltower.firstmile.html"));
    }
  };
});