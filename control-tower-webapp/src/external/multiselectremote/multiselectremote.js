define({
  name : "multiselectremote",
  extend : "spamjs.view",
  modules : ["DataService","jqtag"]
}).as(function(statescities,DataService,jqtag){

    return {
        events : {
          "click .showCities" : "showCities",
          "change input[type='checkbox']" : "checkDestination",
          "click .save_destinations" : "save_destinations",
          "input .search_cities" : "search_cities"
        },
        selectStateIndex : "SELECTEED",
        _init_ : function(){
          var self = this;
          self.selectedFilters = [];
          self.$$.loadTemplate(self.path("statescities.html"),{depandsOn:self.options.dependsOnVal}).then(function(){
          	DataService.get(self.options.dataUrl).then(function(resp) {
	            console.log(resp.data,"........");
	            self.filterData = resp.data[0].dependentOptions[self.options.dependsOnVal];
	            self.listData = [];
	            var renderPopup = _.after(self.filterData.length, function(){
	              self.renderTemplate('');
	            });
	            _.each(self.filterData,function(item, val){
	              console.log(item,".....",val);
	              var data = {
	                val: item,
	                selected: false
	              };
	              self.listData.push(data);
	              renderPopup();
            	});
          	});
          });
          
      },
      renderTemplate: function(search){
        var self = this;
        self.$$.find(".statescities_list").loadTemplate(self.path("statescities.list.html"),{
          filterListData: self.listData,
          search:search,
          filterName: self.options.dependsOnVal
        });
        self.preFilterSlection();
      },
      preFilterSlection: function(){
        var self = this;
        _.each(self.options.preSelectedFilters,function(item, val){
	         var ele = $(document.getElementById(item));
	         if(ele.length > 0){
	           ele.prop("checked",true);  
	         }
        });
      },
      get_selected_cities : function(resp){
        var selected = [];
        for(var i in resp.destinations){
          for(var j in resp.destinations[i].cities){
            if(resp.destinations[i].cities[j].selected){
              selected.push(resp.destinations[i].cities[j])
            }
          }
        }
        return selected;
      },
      search_cities : debounce(function(e,target,data){
          var self = this;
          self.renderTemplate(self.$$.find(".search_cities").val().toLowerCase());
      }),
      showCities : function(e,target,data){
        var self = this;
        self.$$.find(".search_cities").val("");
        this.$$.find(".state_item").removeClass("selected");
        jQuery(target).closest(".state_item").addClass("selected");
        var listData = {};
        listData.options = this.filterData.dependentOptions[target.id];
        self.$$.find(".statescities_list").loadTemplate(this.path("statescities.list.html"),listData);
      },
      checkDestination : function(e,target,data){
        var self = this;
        var checked = target.checked;
        if(checked){
          self.options.preSelectedFilters.push(target.id);
        }else{
          self.options.preSelectedFilters = jQuery.grep(self.options.preSelectedFilters, function(value) {
            return value != target.id;
          });
        };
        console.log(self.options.preSelectedFilters);
      },
      save_destinations: function(){
        var self = this;
        self.selectedFilters = [];
        var selectedFilters = $('input:checkbox:checked');
        _.each(selectedFilters, function(item){
          self.selectedFilters.push($(item).attr("id"));
        });
        self.options.onSaveMultiRemoteFilter.call(self.options.parentModule, self.options.filterName, self.selectedFilters);
      }
    };

});