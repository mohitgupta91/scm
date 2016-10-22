define({
  name: "controltower.content",
  extend: "spamjs.view",
  modules: ["jQuery"]
}).as(function(CONTENT, jQuery) {
  return {
    _init_: function() {
      var self = this;
      self.$$.loadTemplate(self.path("controltower.content.html"));
    }
  };
});
