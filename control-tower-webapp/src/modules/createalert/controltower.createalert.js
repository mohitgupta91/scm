define({
    name: "controltower.createalert",
    extend: "spamjs.view",
    modules: ["jQuery", "Select2", "DataService", "jqtags.tab", "controltower.filters", "controltower.buttonConfig", "ResourceUtil","controltower.toast"],
}).as(function(createalert, jq, Select2, DataService, jqTabs, filters, buttonConfig, ResourceUtil, Toast) {
    return {
        events : {
        	"change .select2-alert-Type": "onAlertTypeChange",
        	"change .select2-group-logic": "onGroupLogicChange",
        	"click #submit": "submitData",
        },
        groupLogicData:{
        	data:null,
        	ruleTime: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]
        },
        _init_: function() {
            var self = this;
            self.alerts = self.options.alerts;
	        self.$$.loadTemplate(self.path("controltower.createalert.html"),{alerts:self.alerts});
        },
        onAlertTypeChange: function(event){
        	var self = this;
        	var alertType = $(event.target);
        	self.selectedAlertVal = alertType.val();
        	DataService.get("alerts/"+self.selectedAlertVal+"/grouplogic").done(function(resp) {
        		self.groupLogicData.data = resp.data["alertGroupLogics"];
				self.renderCreateAlert(self.groupLogicData);
			});
        },
        renderCreateAlert: function(){
        	var self = this;
        	self.$$.find(".create-alert-criteria").loadTemplate(self.path("controltower.createalertcriteria.html"),{groupLogicData:self.groupLogicData});
		},
		onGroupLogicChange: function(event){
			var self = this;
			var groupLogicEle = $(event.target);
			var groupLogicVal = groupLogicEle.val();
			var data = _.find(self.groupLogicData.data,function(item,index){
				if(item.groupLogicName == groupLogicVal){
					return item;
				}
			});
			self.changeCriteriaEle(data);
		},
		changeCriteriaEle: function(data){
			var self = this;
			var criteriaContainer = self.$$.find(".criteria");
			if(data.isFileType){
				var fileEle = '<li id = "file"><label class="title">File</label><input type="file" data-input="file" id="fileInput" ></li>';
				criteriaContainer.find("#value").after(fileEle);
			}else{
				criteriaContainer.find("#file").remove();
			};
		},
		notification: function(type, message){
			Toast.raise({
				type: type,
        		message: message
        	});
		},
		submitData: function() {
		  	var self = this;
		  	var data = new FormData();
		  	var dataElements = self.$$.find('[data-input]');
		  	var isValidated = true;
		  	data.append("alertTitle",$(".select2-alert-Type").text().trim());
		  	var validated = _.after(dataElements.length, function(){
	          	self.postRequest(data);
	        });
		  	$.each(dataElements,function(index,item){
			  	var key = $(item).data("input");
			  	var value = $(item).val();
			  	var isIntGroup = _.indexOf(["historicalDateRangeStart","historicalDateRangeEnd","currentDateRangeStart","currentDateRangeEnd","ruleTime","value"], key);
			  	//var isStrGroup = _.indexOf(["alertTypeKey","groupLogic","operator","emailId"], key);
			  	
			  	if(!value){
			  		if(key == "file"){
		  				self.notification("warning","Please select a file");
			  		}else{
			  			self.notification("warning","Please enter a valid value for "+key);	
			  		}
		  			return false;
			  	};

			  	if(isIntGroup > (-1)){
			  		value = Nubmer(value);
			  	};

			  	if(isIntGroup > (-1)){
			  		if(!$.isNumeric( value )){
			  			self.notification("warning","Please enter a valid numeric value for "+key);
			  			return false;
		  			};
			  	};

			  	if(key == "emailId"){
			  		var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
					if (!filter.test(value)) {
						self.notification("warning","Please enter a valid email");
						return false;
					};
			  	};

			  	if(key == "file"){
			  		data.append(key,$(item)[0].files[0]);
			  	}else{
			  		data.append(key,value);
			  	};
			  	validated();
		  	}); 
		 
		},
		postRequest: function(data){
			var self = this;
		    $.ajax({
		    	type: "POST",
		    	data: data,
		    	url: "/data/alerts/"+self.selectedAlertVal+"/instances",
		    	enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				success: function(resp){
					console.log(resp);
					if(typeof resp == "object"){
						if(resp.code == 200){
							$(".select2-alert-Type").val("");
							self.$$.find(".create-alert-criteria").html('');
						}else{
							self.notification("error",resp.errors.message);
						};
					}else{
						self.notification("error","something went wrong");
					};
				},
				error: function(resp){
					self.notification("error", resp.statusText);
				}
		    });
		}

	};
});