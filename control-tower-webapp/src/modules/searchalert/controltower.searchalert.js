define({
    name: "controltower.searchalert",
    extend: "spamjs.view",
    modules: ["jQuery", "Select2", "DataService", "jqtags.tab", "controltower.filters", "controltower.buttonConfig", "ResourceUtil"],
}).as(function(searchalert, jq, Select2, DataService, jqTabs, filters, buttonConfig, ResourceUtil) {
    return {
        events : {
        },
        _init_: function() {
            var self = this;
	        self.$$.loadTemplate(self.path("controltower.searchalert.html"));
        }
    };
});