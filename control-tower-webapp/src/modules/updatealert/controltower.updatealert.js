define({
    name: "controltower.updatealert",
    extend: "spamjs.view",
    modules: ["jQuery", "Select2", "DataService", "jqtags.tab", "controltower.filters", "controltower.buttonConfig", "ResourceUtil", "UserService","controltower.toast"],
}).as(function(updatealert, jq, Select2, DataService, jqTabs, filters, buttonConfig, ResourceUtil, UserService, Toast) {
    return {
        events : {
            "click .listItem" : "launchUpdatePopup",
            "click #deleteBtn": "deleteAlertItem",
            "click #saveChanges": "saveChanges",
            "click #editFileData": "editFileItems",
            "click #resetFileTable": "resetFileTable",
            "click #backButton": "renderModule",
            "change .select2-group-logic": "onGroupLogicChange"
        },
        groupLogicData:{
            data:null,
            ruleTime: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23],
            itemData: null
        },
        jsGridData: null,
        _init_: function() {
            var self = this;
            self.dirtyFileItems = [];
            self.deletedFileItems = [];
            self.userDetails = UserService.getUserDetails();
            self.renderModule();
        },
        renderModule: function(){
            var self = this;
            DataService.get("users/"+self.userDetails.email+"/alerts/instances").done(function(resp) {
                self.alertInstanceList = resp.data["alertInstanceList"];
                self.$$.loadTemplate(self.path("controltower.updatealert.html"),{alertInstanceList:self.alertInstanceList}).then(function(){
                    self.gridDataManipulation();
                });
            });
            
        },
        gridDataManipulation: function(){
            var self = this;
            self.jsGridData = {
                loadData: function(filter) {
                    console.log(filter);
                    return $.grep(this.alertInstanceList, function(client) {
                        console.log(client);
                        return (!filter.alertId || client.alertId === filter.alertId) &&
                            (!filter.alertInstanceId || client.alertInstanceId === filter.alertInstanceId) &&
                            (!filter.alertInstanceTitle || client.alertInstanceTitle.indexOf(filter.alertInstanceTitle) > -1) &&
                            (!filter.createdBy || client.createdBy.indexOf(filter.createdBy) > -1) &&
                            (!filter.emailId || client.assigneeTo.indexOf(filter.emailId) > -1) &&
                            (!filter.createdOn || client.createdOn.indexOf(filter.createdOn) > -1) &&
                            (!filter.updatedOn || client.updatedOn.indexOf(filter.updatedOn) > -1)
                    });
                }
            };

            self.jsGridData.alertInstanceList = self.alertInstanceList;
            $(".alerts-list").jsGrid({
                height: "90%",
                width: "100%",
                filtering: true,
                //editing: true,
                //inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 7,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the client?",
                controller: self.jsGridData,
                fields: [
                    { name: "alertId", type: "number", title: "Alert Id"},
                    { name: "alertInstanceId", type: "number", title: "Instance Id"},
                    { name: "alertInstanceTitle", type: "text", title: "Instance Title"},
                    { name: "createdBy", type: "text", title: "Created By"},
                    { name: "emailId", type: "text", title: "Assignee to"},
                    { name: "createdOn", type: "text", title: "Created"},
                    { name: "updatedOn", type: "text", title: "Updated"},
                    { type: "control", itemTemplate: function(value, item) {
                        return $("<button>").text("Edit").attr("id",item.alertInstanceId)
                            .on("click", function(event) {
                                self.launchUpdatePopup(item);
                                return false;
                            });
                        } 
                    }
                ]
            });
        },
        launchUpdatePopup: function(item){
            var self = this;
            self.popupItem = item;
            var alertId = item.alertId;
            var alertInstanceId = item.alertInstanceId;
            self.groupLogicData.itemData = item;
            DataService.get("alerts/"+alertId+"/grouplogic").done(function(resp) {
                self.groupLogicData.data = resp.data["alertGroupLogics"];
                self.$$.loadTemplate(self.path("controltower.updatealertpopup.html"),{popupData: self.groupLogicData}).then(function(){
                    self.$$.find("#operator").find("jq-select2").val(self.groupLogicData.itemData.operator);
                    self.onGroupLogicChange();
                    // self.$$.find("#myModal").modal('show');
                    // self.$$.find("#myModal").on('hidden.bs.modal', function(){
                    //     $(this).remove();
                    // });
                });
            });
        },
        onGroupLogicChange: function(event){
            var self = this;
            var groupLogicEle = self.$$.find("#groupingLogic").find("jq-select2");
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
                var fileEle = '<li id = "file" style=""><label class="title">File</label><input class="file-input" type="file" style="" data-input="file" id="fileInput" ><button id="editFileData" class="btn btn-default">Edit Data</button></li>';
                criteriaContainer.find("#value").after(fileEle);
            }else{
                criteriaContainer.find("#file").remove();
            };
        },
        editFileItems: function(event){
            var self = this;
            var ele = self.$$.find("#editFileData");
            if(ele.text() == "Hide File"){
                self.$$.find("#fileDataList").slideUp();
                ele.text("Edit File");
                self.$$.find("#resetFileTable").hide();
                return;
            };
            ele.text("Hide File");
            if(self.$$.find("#fileDataList").html()){
                self.$$.find("#fileDataList").slideDown();
                self.$$.find("#resetFileTable").show();
                return;
            };
            DataService.get("/alerts/"+self.popupItem.alertId+"/instances/"+self.popupItem.alertInstanceId+"/filedata").done(function(resp) {
                self.fileData = resp.data["fileData"];
                //var data = {"fileData":[{"col1":"abc","col2":"abc1","col3":"abc2","col4":"abc3"},{"col1":"xyz","col2":"xyz1","col3":"xyz2","col4":"xyz3"},{"col1":"data","col2":"data1","col3":"data2","col4":"data3"},{"col1":"test","col2":"test1","col3":"test2","col4":"test3"}]}
                var fields = [];
                var headers = Object.keys(self.fileData[0]);
                var renderFile = _.after(headers.length,function(){
                    fields.push({ type: "control" });
                    self.renderFileDataGrid(self.fileData, fields);
                });
                _.each(headers,function(item, val){
                    if(item != "rowId"){
                        fields.push({ name: item, type: "text"})
                    };
                    renderFile();
                });
                
            });
            
        },
        renderFileDataGrid: function(data, fields){
            var self = this;
            self.fileGridData = {
                loadData: function(filter) {
                    return $.grep(this.fileDataList, function(client) {
                        return client;
                    });
                },
                updateItem: function(updatingClient) {
                    self.dirtyFileItems = _.reject(self.dirtyFileItems, function(item){ return item.rowId == updatingClient.rowId; });
                    self.dirtyFileItems.push(updatingClient);
                },
                deleteItem: function(deletingClient) {
                    self.deletedFileItems.push(deletingClient.rowId);
                }
            };

            self.fileGridData.fileDataList = data;
            $("#fileDataList").jsGrid({
                height: "70%",
                width: "70%",
                editing: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 7,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete this row?",
                controller: self.fileGridData,
                fields: fields
            });
            self.$$.find("#resetFileTable").show();
            if(self.$$.find(".jsgrid-pager-container").css("display") != "none"){
                self.$$.find("#resetFileTable").css("margin-top","-20px");
            }
        },
        resetFileTable: function(){
            var self = this;
            self.$$.find("#fileDataList").jsGrid("destroy");
            self.$$.find("#editFileData").text("Edit File");
            self.dirtyFileItems = [];
            self.deletedFileItems = [];
            this.editFileItems();
        },
        deleteAlertItem: function(){
            var self = this;
            var alertId = self.popupItem.alertId;
            var alertInstanceId = self.popupItem.alertInstanceId;
            DataService.delete("alerts/"+alertId+"/instances/"+alertInstanceId).done(function(resp){
                _.delay(function(){self.renderModule()},500);
            })
        },
        notification: function(type, message){
            Toast.raise({
                type: type,
                message: message
            });
        },
        saveChanges: function(){
            var self = this;
            var selectedFile;
            var data = new FormData();
            var dataElements = self.$$.find('[data-input]');
            var isValidated = true;
            if($("#fileInput").length > 0){
                selectedFile = $("#fileInput")[0].files[0];    
            };
            data.append("alertTitle",self.groupLogicData.itemData.alertTitle);
            data.append("alertId",self.groupLogicData.itemData.alertId);
            data.append("alertInstanceId",self.groupLogicData.itemData.alertInstanceId);
            //data.append("alertInstanceTitle",self.groupLogicData.itemData.alertInstanceTitle);
            if(!selectedFile){
                if(self.dirtyFileItems.length > 0){
                    data.append("editedRows", JSON.stringify(self.dirtyFileItems));
                };
                if(self.deletedFileItems.length > 0){
                    data.append("deletedRowsId", JSON.stringify(self.deletedFileItems));
                };
            };
            
            var validated = _.after(dataElements.length, function(){
              self.postRequest(data);
            });
            $.each(dataElements,function(index,item){
                var key = $(item).data("input");
                var value = $(item).val();
                var isIntGroup = _.indexOf(["historicalDateRangeStart","historicalDateRangeEnd","currentDateRangeStart","currentDateRangeEnd","ruleTime","value"], key);
                
                if(!value){
                    if(key == "file"){
                        //self.notification("warning","Please select a file");
                    }else{
                        self.notification("warning","Please enter a valid value for "+key);
                        return false;
                    }
                    
                };

                if(isIntGroup > (-1)){
                    value = Number(value);
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
                    if(selectedFile){
                        data.append(key,$(item)[0].files[0]);    
                    };
                }else{
                    data.append(key,value);
                };
                console.log(key,"......",value);
                validated();
            }); 
         
        },
        postRequest: function(data){
            var self = this;
            $.ajax({
                type: "POST",
                data: data,
                url: "/data/alerts/"+self.popupItem.alertId+"/instances/_edit",
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                success: function(resp){
                    console.log(resp);
                    if(typeof resp == "object"){
                        if(resp.code == 200){
                            self.$$.find("#closeBtn").trigger("click");
                            _.delay(function(){self.renderModule()},500);
                        }else{
                            self.notification("error",resp.errors.message);
                        };
                    }else{
                        self.notification("error","error..!!");
                    };
                },
                error: function(resp){
                    self.notification("error","error..!!");
                }
            })
        }
    };
}); 