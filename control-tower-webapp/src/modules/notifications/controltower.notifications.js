define({
    name: "controltower.notifications",
    extend: "spamjs.view",
    modules: ["jQuery", "Select2", "DataService", "jqtags.tab", "controltower.filters", "controltower.buttonConfig", "ResourceUtil", "UserService","controltower.toast"],
}).as(function(notifications, jq, Select2, DataService, jqTabs, filters, buttonConfig, ResourceUtil, UserService, Toast) {
    return {
        events : {
            "click #saveChanges": "postNotificationsStatus",
            "click #showFileData": "showFileData",
            "click #backButton": "renderModule"
        },
        notificationsPopupData: {
            instanceData: null,
            notificationsItemData: null
        },
        _init_: function() {
            var self = this;
            var self = this;
            self.userDetails = UserService.getUserDetails();
            self.renderModule();
	        self.$$.loadTemplate(self.path("controltower.notifications.html"));
        },
        renderModule: function(){
            var self = this;
            DataService.get("users/"+self.userDetails.email+"/notifications").done(function(resp) {
                self.notificationsList = resp.data;
                self.$$.loadTemplate(self.path("controltower.notifications.html"),{alertInstanceList:self.notificationsList}).then(function(){
                    self.renderGrid();
                });
            });
            
        },
        renderGrid: function(){
            var self = this;
            self.jsGridData = {
                loadData: function(filter) {
                    return $.grep(this.notificationsList, function(client) {
                        return (!filter.notificationId || client.notificationId === filter.notificationId)&& 
                            (!filter.alertId || client.alertId === filter.alertId) &&
                            (!filter.alertInstanceId || client.alertInstanceId === filter.alertInstanceId) &&
                            (!filter.alertInstanceTitle || client.alertInstanceTitle.indexOf(filter.alertInstanceTitle) > -1) &&
                            (!filter.createdBy || client.createdBy.indexOf(filter.createdBy) > -1) &&
                            (!filter.assignedTo || client.assigneeTo.indexOf(filter.assigneeTo) > -1) &&
                            (!filter.createdOn || client.createdOn.indexOf(filter.createdOn) > -1) &&
                            (!filter.updatedOn || client.updatedOn.indexOf(filter.updatedOn) > -1) &&
                            (!filter.status || client.status.indexOf(filter.status) > -1)
                    });
                },
                updateItem: function(updatingClient) {
                    console.log(updatingClient);
                }
            };

            self.jsGridData.notificationsList = self.notificationsList;
            $(".notifications-list").jsGrid({
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
                    { name: "notificationId", type: "number", title: "Notification Id"},
                    { name: "alertId", type: "number", title: "Alert Id"},
                    { name: "alertInstanceId", type: "number", title: "Instance Id"},
                    { name: "alertInstanceTitle", type: "text", title: "Instance Title"},
                    { name: "createdBy", type: "text", title: "Created By"},
                    { name: "assignedTo", type: "text", title: "Assignee to"},
                    { name: "createdOn", type: "text", title: "Created"},
                    { name: "updatedOn", type: "text", title: "Updated"},
                    { name: "status", type: "text", title: "Status"},
                    { type: "control", itemTemplate: function(value, item) {
                        return $("<button>").text("Edit").attr("id",item.notificationId)
                            .on("click", function(event) {
                                self.launchNotificationsPopup(item);
                                return false;
                            });
                        } 
                    }
                ]
            });
        },
        launchNotificationsPopup: function(item){
            var self = this;
            self.popupItem = item;
            self.notificationsPopupData.notificationsItemData = item;
            self.notificationsPopupData.userDetails = self.userDetails
            DataService.get("alerts/"+item.alertId+"/instances/"+item.alertInstanceId).done(function(resp) {
                self.notificationsPopupData.instanceData = resp.data["alertInstanceList"][0];
                self.$$.loadTemplate(self.path("controltower.notificationsPopup.html"),{"popupData": self.notificationsPopupData}).then(function(){
                    self.$$.find(".modal-body").find("input").prop("readonly",true);
                    self.$$.find("#myModal").modal('show');
                    self.$$.find("#myModal").on('hidden.bs.modal', function(){
                        $(this).remove();
                    });
                });
            })
        },
        showFileData: function(event){
            var self = this;
            var ele = self.$$.find("#showFileData");
            if(ele.text() == "Hide Data"){
                self.$$.find("#fileDataList").slideUp();
                ele.text("Show Data");
                return;
            };
            ele.text("Hide Data");
            if(self.$$.find("#fileDataList").html()){
                self.$$.find("#fileDataList").slideDown();
                return;
            };
            DataService.get("alerts/"+self.popupItem.alertId+"/instances/"+self.popupItem.alertInstanceId+"/filedata").done(function(resp) {
                //var data = {"fileData":[{"col1":"abc","col2":"abc1","col3":"abc2","col4":"abc3"},{"col1":"xyz","col2":"xyz1","col3":"xyz2","col4":"xyz3"},{"col1":"data","col2":"data1","col3":"data2","col4":"data3"},{"col1":"test","col2":"test1","col3":"test2","col4":"test3"}]}
                self.fileData = resp.data["fileData"];
                var fields = [];
                var headers = Object.keys(self.fileData[0]);
                var renderFile = _.after(headers.length,function(){
                    //fields.push({ type: "control" });
                    self.renderFileDataGrid(self.fileData, fields);
                });
                _.each(headers,function(item, val){
                    if(item != "rowId"){
                        fields.push({ name: item, type: "text"})
                    };
                    renderFile();
                });
            })
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
                pageSize: 2,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete this row?",
                controller: self.fileGridData,
                fields: fields
            });
            self.$$.find("#resetFileTable").show();
            if(self.$$.find(".jsgrid-pager").length > 0){
                self.$$.find("#resetFileTable").css("margin-top","-20px");
            }
        },
        postNotificationsStatus: function(){
            var self = this;
            var comment = self.$$.find(".comments-continer").find("textArea").val();
            if(!comment){
                Toast.raise({
                    type: "warning",
                    message: "Please enter your comment in comment box"
                });
                return;
            }
            var requestData = {
                notificationId: self.popupItem.notificationId,
                status: self.$$.find("#saveChanges").data("status").toUpperCase(),
                comment: comment,
                updatedBy: self.userDetails.email
            }
            DataService.post("notifications/"+requestData.notificationId, requestData, {} ).done(function(resp){
                Toast.raise({
                    type: "success",
                    message: "Status has been changed successfully."
                });
                self.$$.find("#closeBtn").trigger("click");
            })
        }
    };
});