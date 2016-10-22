define({
    name: "controltower.buttonConfig",
    extend: "spamjs.view",
    modules: ["jQuery", "ResourceUtil"]
}).as(function(buttonConfig, jq, ResourceUtil) {
    return {
        _init_: function() {
            var self = this;
            var element = self.options.element;
            _.each(self.options.data, function(item) {
                item.displayName = ResourceUtil.getLang(item.label);
            });
            element.loadTemplate(self.path("controltower.buttonConfig.html"), {
                config: self.options.data
            });
        }
    };
});