define({
    name: "ResourceUtil",
    modules: ["DataService", "jQuery", "jsutils.file", "jsutils.i18n"]
}).as(function(ResourceUtil, DataService, jq, fileUtil, i18n) {
    return {
        $langMap: null,
        langMap: null,
        langMapReverse: {},
        $Navigation: null,
        Navigation: null,
        langVersion: "1.0.4",
        _ready_: function() {
            this.initNavs();
            this.initLang();
        },
        initNavs: function() {
            var self = this;
            if (!self.$Navigation) {
                self.$Navigation = fileUtil.getJSON(self.path("navigation.json")).done(function(resp) {
                  self.Navigation = resp;
                });
            }
            return self.$Navigation.then(function() {
                return self.Navigation;
            });
        },
        initLang: function() {
            var self = this;
            var langMap = localStorage.getItem("_lang");
            if (!langMap || self.langVersion != JSON.parse(langMap).version) {
                self.$langMap = i18n.add(self.path("keywords.json")).done(function(resp) {
                    self.langMap = resp;
                    localStorage.setItem("_lang", JSON.stringify(resp));
                    self.$langMap = null;
                });
            } else {
                i18n.add(JSON.parse(langMap)).then(function(resp) {
		            self.langMap = resp;
                });
        	}
        },
        getLang: function(key) {
            var self = this;
            if(self.langMap && self.langMap.map[key]) {
                return self.langMap.map[key];
            } else if(self.$langMap) {
                return self.$langMap;
            } else {
                return key;
            }
        },
        getKey: function(lang) {
            var self = this;
            if(self.langMap && self.langMap.map) {
            	var returnKey = lang;
            	_.each(self.langMap.map, function(val, key) {
            		if(val == lang) {
            			returnKey = key;
            		}
            	});
            	return returnKey;
            } else {
            	return lang;
            }
        }
    };
});
